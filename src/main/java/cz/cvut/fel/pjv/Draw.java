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

import javafx.scene.canvas.GraphicsContext;

/**
 * Class for drawing and transforming the world.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class Draw {
    
    private final double WIDTH;
    private final double HEIGHT;
    private double zoomScale = 1;
    private double oldZoomScale = 1;
    private final double MIN_ZOOM_SCALE = 0.5;
    private final double MAX_ZOOM_SCALE = 2;
    private double cameraOffsetX = 0;
    private double cameraOffsetY = 0;

    public Draw(double WIDTH, double HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }
    
    /**
     * If player is moving outside from canvas, move whole world,
     * so player stays in the canvas.
     * 
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
                }
            }
            for (Block block : world.getBlocks()) {
                if (block.isDestroyed()) { continue; }
                block.setX(block.getX() + playerVelocityX);
            }
            cameraOffsetX += playerVelocityX;
            if (cameraOffsetX > Block.block_width) { cameraOffsetX %= Block.block_width; }
        }
        
        if (player.getX() > WIDTH-90-player.getWidth() && player.movingRight()) {
            player.setX(player.getX() - playerVelocityX);
            for (Item item : world.getItems()) {
                if (!item.isPicked()) {
                    item.setX(item.getX() - playerVelocityX);
                }
            }
            for (Block block : world.getBlocks()) {
                if (block.isDestroyed()) { continue; }
                block.setX(block.getX() - playerVelocityX);
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
                cameraOffsetY += playerVelocityY;
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
        oldZoomScale = this.zoomScale; //store for zoom reset
    }

    /**
     * Draw the world and entities in it.
     * 
     * @param g a canvas 2D rendering context
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
            g.setFill(block.getColor());
            g.fillRect(block.getX(), block.getY(), block.getWidth(), block.getHeight());
        }
        for (Item item : world.getItems()) {
            if (!item.isPicked()) {
                g.setFill(item.getColor());
                g.fillRect(item.getX(), item.getY(), item.getWidth(), item.getHeight());
            }
        }
    }

    public double getZoomScale() {
        return zoomScale;
    }

    public void setZoomScale(double zoomScale) {
        this.zoomScale = zoomScale;
    }

    public double getOldZoomScale() {
        return oldZoomScale;
    }

    public void setOldZoomScale(double oldZoomScale) {
        this.oldZoomScale = oldZoomScale;
    }

    public double getMIN_ZOOM_SCALE() {
        return MIN_ZOOM_SCALE;
    }

    public double getMAX_ZOOM_SCALE() {
        return MAX_ZOOM_SCALE;
    }

    public double getCameraOffsetX() {
        return cameraOffsetX;
    }

    public double getCameraOffsetY() {
        return cameraOffsetY;
    }
    
}
