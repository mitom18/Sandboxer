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

import cz.cvut.fel.pjv.blocks.Block;
import cz.cvut.fel.pjv.blocks.BlockType;
import cz.cvut.fel.pjv.blocks.LiquidBlock;
import cz.cvut.fel.pjv.creatures.Player;
import cz.cvut.fel.pjv.items.ItemType;
import cz.cvut.fel.pjv.items.StoredBlock;
import cz.cvut.fel.pjv.items.Tool;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Class for initializing the event handlers for keyboard and mouse events.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class EventHandlers {
    private final Scene gameScreen;
    private final GameAnimationTimer timer;
    private final Draw draw;
    private final GraphicsContext gc;
    private final Game game;
    private final Player player;

    public EventHandlers(Scene gameScreen, GameAnimationTimer timer, Draw draw, GraphicsContext gc, Game game, Player player) {
        this.gameScreen = gameScreen;
        this.timer = timer;
        this.draw = draw;
        this.gc = gc;
        this.game = game;
        this.player = player;
    }
    
    public void create() {
        createKeyHandler();
        createMouseHandler();
    }
    
    private void createKeyHandler() {
        gameScreen.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (timer.isRunning()) {
                    switch (event.getCode()) {
                        case W:     player.setUp(true); break;
                        case A:     player.setLeft(true); break;
                        case S:     player.setDown(true); break;
                        case D:     player.setRight(true); break;
                        case SHIFT: player.run(true); break;
                        case Q:     player.changeActiveItem(-1); break;
                        case E:     player.changeActiveItem(1); break;
                        case C:     draw.zoom(game); break;
                        case ESCAPE: {
                            timer.stop();
                            draw.drawPauseMenu(gc);
                            break;
                        }
                    }
                } else {
                    if (event.getCode() == KeyCode.ESCAPE) {
                        timer.start();
                    }
                }
            }
        });

        gameScreen.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (timer.isRunning()) {
                    switch (event.getCode()) {
                        case W:     player.setUp(false); break;
                        case A:     player.setLeft(false); break;
                        case S:     player.setDown(false); break;
                        case D:     player.setRight(false); break;
                        case SHIFT: player.run(false); break;
                    }
                }
            }
        });
    }
    
    private void createMouseHandler() {
        gameScreen.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (timer.isRunning()) {
                    MouseButton button = event.getButton();
                    double clickX = event.getSceneX();
                    double clickY = event.getSceneY();

                    if (button == MouseButton.PRIMARY) {
                        createLeftMouseClickHandler(clickX, clickY);
                    }
                    else if (button == MouseButton.SECONDARY) {
                        createRightMouseClickHandler(clickX, clickY);
                    }
                }
            }
        });
        
        gameScreen.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (timer.isRunning() && event.getEventType() == ScrollEvent.SCROLL) {
                    createMouseScrollHandler(event);
                }
            }
        });
    }
    
    private void createLeftMouseClickHandler(double clickX, double clickY) {
        World world = game.getWorld();
        if (clickX > player.getX()-Block.block_width*4 && clickX < player.getX2()+Block.block_width*4
            && clickY > player.getY()-Block.block_height*4 && clickY < player.getY2()+Block.block_height*4
            && (clickX < player.getX() || clickX > player.getX2() || clickY < player.getY()-12 || clickY > player.getY2())
        ) {
            //destroy block
            if (player.getInventory().getActiveItem() instanceof Tool) {
                Tool tool = (Tool) player.getInventory().getActiveItem();
                if (tool.isPickaxe()) {
                    for (Block block : world.getBlocks()) {
                        if (block.isDestroyed()) { continue; }
                        if (Collision.collides(clickX, clickY, block)) {
                            // Bedrock is indestructible.
                            if (block.getBlockType() != BlockType.BEDROCK && block.getBlockType() != BlockType.WATER) {
                                block.destroy();
                                ItemType blockType = ItemType.valueOf(block.getBlockType().name());
                                player.getInventory().add(new StoredBlock(0, 0, blockType));
                            }
                        }
                    }
                    world.updateLiquids(player);
                }
            }
        }
        player.attack(world, clickX);
    }
    
    private void createRightMouseClickHandler(double clickX, double clickY) {
        World world = game.getWorld();
        if (clickX > player.getX()-Block.block_width*4 && clickX < player.getX2()+Block.block_width*4
            && clickY > player.getY()-Block.block_height*4 && clickY < player.getY2()+Block.block_height*4
            && (clickX < player.getX() || clickX > player.getX2() || clickY < player.getY()-12 || clickY > player.getY2())
        ) {
            //build block
            if (player.getInventory().getActiveItem() instanceof StoredBlock) {
                boolean canBuild = true;
                for (Block block : world.getBlocks()) {
                    if (Collision.collides(clickX, clickY, block)) {
                        if (block instanceof LiquidBlock) {
                            block.destroy();
                        } else {
                            canBuild = false;
                        }
                    }
                }
                if (canBuild) {
                    StoredBlock item = (StoredBlock) player.getInventory().getActiveItem();
                    double blockX = clickX - (clickX - draw.getCameraOffsetX()) % Block.block_width;
                    double blockY = clickY - (clickY - draw.getCameraOffsetY()) % Block.block_height;
                    if (clickX < draw.getCameraOffsetX()) { blockX = draw.getCameraOffsetX() - Block.block_width; }
                    if (clickY < draw.getCameraOffsetY()) { blockY = draw.getCameraOffsetY() - Block.block_height; }
                    world.getBlocks().add(item.place(blockX, blockY));
                    player.getInventory().remove(item);
                }
            }
        }
    }
    
    private void createMouseScrollHandler(ScrollEvent event) {
        draw.setZoomScale(draw.getZoomScale() + event.getDeltaY()/1000);
        if (draw.getZoomScale() > draw.getMAX_ZOOM_SCALE()) { draw.setZoomScale(draw.getMAX_ZOOM_SCALE()); }
        if (draw.getZoomScale() < draw.getMIN_ZOOM_SCALE()) { draw.setZoomScale(draw.getMIN_ZOOM_SCALE()); }
        draw.zoom(game);
    }
    
}
