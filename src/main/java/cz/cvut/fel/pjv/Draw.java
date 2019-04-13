/*
 * The MIT License
 *
 * Copyright 2019 Michal-jr.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.items.Item;
import cz.cvut.fel.pjv.items.StoredBlock;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class for drawing and transforming the world.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class Draw {
    
    private final double WIDTH;
    private final double HEIGHT;
    private final int MAP_WIDTH;
    private double zoomScale = 1;
    private double oldZoomScale = 1;
    private final double MIN_ZOOM_SCALE = 0.04;
    private final double MAX_ZOOM_SCALE = 2;
    private double cameraOffsetX = 0;
    private double cameraOffsetY = 0;

    /**
     * Set width and height parameters for drawing.
     *
     * @param WIDTH width of the canvas in pixels
     * @param HEIGHT height of the canvas in pixels
     * @param MAP_WIDTH width of the world in blocks
     * @since 1.0
     */
    public Draw(double WIDTH, double HEIGHT, int MAP_WIDTH) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.MAP_WIDTH = MAP_WIDTH;
    }
    
    /**
     * If player is moving outside from canvas, move whole world,
     * so player stays in the canvas.
     * 
     * @param game instance of the game
     * @since 1.0
     */
    public void shiftCamera(Game game) {
        Player player = game.getPlayer();
        World world = game.getWorld();
        double playerVelocityX = player.getVelocityX();
        double playerVelocityY = player.getVelocityY();
        
        if (player.getX() < 90 && player.movingLeft()) {
            player.setX(player.getX() + playerVelocityX);
            for (Item item : world.getItems()) {
                if (!item.isPicked()) {
                    item.setX(item.getX() + playerVelocityX);
                    if (item.getX() > ((int) MAP_WIDTH/2) * Block.block_width) {
                        //make loop world, take items in one column of world from the right and move them to the left side of the world
                        item.setX(item.getX() - MAP_WIDTH * Block.block_width);
                    }
                }
            }
            for (Block block : world.getBlocks()) {
                if (block.isDestroyed()) { continue; }
                block.setX(block.getX() + playerVelocityX);
                if (block.getX() > ((int) MAP_WIDTH/2) * Block.block_width) {
                    //make loop world, take blocks in one column of world from the right and move them to the left side of the world
                    block.setX(block.getX() - MAP_WIDTH * Block.block_width);
                }
            }
            cameraOffsetX += playerVelocityX;
            if (cameraOffsetX > Block.block_width) { cameraOffsetX %= Block.block_width; }
        }
        
        if (player.getX() > WIDTH-90-player.getWidth() && player.movingRight()) {
            player.setX(player.getX() - playerVelocityX);
            for (Item item : world.getItems()) {
                if (!item.isPicked()) {
                    item.setX(item.getX() - playerVelocityX);
                    if (item.getX() < ((int) -MAP_WIDTH/2) * Block.block_width) {
                        //make loop world, take items in one column of world from the right and move them to the left side of the world
                        item.setX(item.getX() + MAP_WIDTH * Block.block_width);
                    }
                }
            }
            for (Block block : world.getBlocks()) {
                if (block.isDestroyed()) { continue; }
                block.setX(block.getX() - playerVelocityX);
                if (block.getX() < ((int) -MAP_WIDTH/2) * Block.block_width) {
                    //make loop world, take blocks in one column of world from the left and move them to the right side of the world
                    block.setX(block.getX() + MAP_WIDTH * Block.block_width);
                }
            }
            cameraOffsetX += Block.block_width - playerVelocityX;
            if (cameraOffsetX > Block.block_width) { cameraOffsetX %= Block.block_width; }
        }
        
        if ((player.getY() < 90 && player.jumping()) || (player.getY() > HEIGHT-90-player.getHeight() && player.falling())) {
            player.setY(player.getY() - playerVelocityY);
            for (Item item : world.getItems()) {
                if (!item.isPicked()) {
                    item.setY(item.getY() - playerVelocityY);
                }
            }
            for (Block block : world.getBlocks()) {
                if (block.isDestroyed()) { continue; }
                block.setY(block.getY() - playerVelocityY);
            }
            if (playerVelocityY < 0) {
                cameraOffsetY -= playerVelocityY;
            } else {
                cameraOffsetY += Block.block_height - playerVelocityY;
            }
            if (cameraOffsetY > Block.block_height) { cameraOffsetY %= Block.block_height; }
        }
    }
    
    /**
     * Change the size of entities in the world and move the whole world
     * (x and y coords of all entities), so player is in the middle of the canvas.
     * 
     * @param game instance of the game
     * @since 1.0
     */
    public void zoom(Game game) {
        Player player = game.getPlayer();
        World world = game.getWorld();
        double zoomScale;
        oldZoomScale = 1/oldZoomScale; //zoom reset
        for (int i = 0; i < 2; i++) {
            //first reset the view to default zoom
            if (i == 0) { zoomScale = oldZoomScale; }
            //then zoom the view by set scale
            else { zoomScale = this.zoomScale; }
            
            double playerX = WIDTH/2;
            double playerY = HEIGHT/2;
            double offsetX = player.getX()*zoomScale - playerX;
            double offsetY = player.getY()*zoomScale - playerY;
            player.setVelocityMultiplier(zoomScale);
            player.setX(playerX);
            player.setY(playerY);
            player.setWidth(player.getWidth()*zoomScale);
            player.setHeight(player.getHeight()*zoomScale);
            for (Item item : world.getItems()) {
                if (!item.isPicked()) {
                    item.setX(item.getX()*zoomScale - offsetX);
                    item.setY(item.getY()*zoomScale - offsetY);
                    item.setWidth(item.getWidth()*zoomScale);
                    item.setHeight(item.getHeight()*zoomScale);
                }
            }
            for (Block block : world.getBlocks()) {
                if (block.isDestroyed()) { continue; }
                block.setX(block.getX()*zoomScale - offsetX);
                block.setY(block.getY()*zoomScale - offsetY);
                block.setWidth(block.getWidth()*zoomScale);
                block.setHeight(block.getHeight()*zoomScale);
            }
            cameraOffsetX *= zoomScale;
            cameraOffsetY *= zoomScale;
            cameraOffsetX -= offsetX;
            cameraOffsetY -= offsetY;
            if (cameraOffsetX > Block.block_width) { cameraOffsetX %= Block.block_width; }
            if (cameraOffsetY > Block.block_height) { cameraOffsetY %= Block.block_height; }
        }
        oldZoomScale = this.zoomScale; //store zoom for zoom reset
    }
    
    private void drawInventory(GraphicsContext g, Inventory inv) {
        final double INV_HOTBAR_ITEMS_COUNT = 10;
        final double INV_HEIGHT = 50; //size of one hotbar cell in pixels
        final double STROKE_WIDTH = 5;
        final double ITEM_SIZE = 40; //hotbar item size in pixels
        final double INV_START_X = 0+STROKE_WIDTH;
        final double INV_START_Y = 0+STROKE_WIDTH;
        
        double x = 0;
        Item[] items = inv.getHotbarItems();
        for (int i = 0; i < INV_HOTBAR_ITEMS_COUNT; i++) {
            //draw the background
            g.setFill(Color.WHITE);
            g.setStroke(Color.RED);
            g.setLineWidth(STROKE_WIDTH);
            g.fillRect(INV_START_X+x, INV_START_Y, INV_HEIGHT, INV_HEIGHT);
            g.strokeRect(INV_START_X+x, INV_START_Y, INV_HEIGHT, INV_HEIGHT);
            //draw the item
            if (items[i] != null) {
                Item item = items[i];
                g.drawImage(item.getImage(), item.getIMAGE_X(), item.getIMAGE_Y(), item.getIMAGE_WIDTH(), item.getIMAGE_HEIGHT(), 
                        INV_START_X+x+INV_HEIGHT/2-ITEM_SIZE/2, 
                        INV_START_Y+INV_HEIGHT/2-ITEM_SIZE/2, 
                        ITEM_SIZE, ITEM_SIZE);
                if (item instanceof StoredBlock) {
                    StoredBlock storedBlock = (StoredBlock) item;
                    if (storedBlock.getQuantity() > 1) {
                        g.setFill(Color.BLACK);
                        g.fillText(storedBlock.getQuantity().toString(), x+15, INV_HEIGHT-5);
                    }
                }
            }
            x += INV_HEIGHT;
        }
        //highlight active item
        g.setStroke(Color.GOLD);
        g.strokeRect(INV_START_X+INV_HEIGHT*inv.getActiveItemIndex(), INV_START_Y, INV_HEIGHT, INV_HEIGHT);
    }

    /**
     * Draw the world and entities in it.
     * 
     * @param g a canvas 2D rendering context
     * @param game instance of the game
     * @since 1.0
     */
    public void render(GraphicsContext g, Game game) {
        //clear the canvas
        g.clearRect(0, 0, WIDTH, HEIGHT);
        //draw
        World world = game.getWorld();
        Player player = game.getPlayer();
        g.drawImage(player.getImage(), player.getSpriteX(), player.getSpriteY(), player.getIMAGE_WIDTH(), player.getIMAGE_HEIGHT(), 
                player.getX(), player.getY(), player.getWidth(), player.getHeight());
        for (Block block : world.getBlocks()) {
            if (block.isDestroyed()) { continue; }
            g.drawImage(Block.getImage(), block.getIMAGE_X(), block.getIMAGE_Y(), block.getIMAGE_WIDTH(), block.getIMAGE_HEIGHT(), 
                    block.getX(), block.getY(), block.getWidth(), block.getHeight());
        }
        for (Item item : world.getItems()) {
            if (!item.isPicked()) {
                g.drawImage(item.getImage(), item.getIMAGE_X(), item.getIMAGE_Y(), item.getIMAGE_WIDTH(), item.getIMAGE_HEIGHT(), 
                        item.getX(), item.getY(), item.getWidth(), item.getHeight());
            }
        }
        drawInventory(g, player.getInventory());
    }

    /**
     * @return actual zoom scale of the world
     * @since 1.0
     */
    public double getZoomScale() {
        return zoomScale;
    }

    /**
     * Set new zoom scale of the world.
     *
     * @param zoomScale
     * @since 1.0
     */
    public void setZoomScale(double zoomScale) {
        this.zoomScale = zoomScale;
    }

    /**
     * @return old zoom scale of the world
     * @since 1.0
     */
    public double getOldZoomScale() {
        return oldZoomScale;
    }

    /**
     * Rewrite old zoom scale of the world.
     *
     * @param oldZoomScale
     * @since 1.0
     */
    public void setOldZoomScale(double oldZoomScale) {
        this.oldZoomScale = oldZoomScale;
    }

    /**
     * @return minimal zoom scale
     * @since 1.0
     */
    public double getMIN_ZOOM_SCALE() {
        return MIN_ZOOM_SCALE;
    }

    /**
     * @return maximal zoom scale
     * @since 1.0
     */
    public double getMAX_ZOOM_SCALE() {
        return MAX_ZOOM_SCALE;
    }

    /**
     * @return camera offset on x axis
     * @since 1.0
     */
    public double getCameraOffsetX() {
        return cameraOffsetX;
    }

    /**
     * @return camera offset on y axis
     * @since 1.0
     */
    public double getCameraOffsetY() {
        return cameraOffsetY;
    }
    
}
