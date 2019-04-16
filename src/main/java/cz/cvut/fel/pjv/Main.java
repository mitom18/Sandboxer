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

import cz.cvut.fel.pjv.creatures.Player;
import cz.cvut.fel.pjv.blocks.BlockType;
import cz.cvut.fel.pjv.blocks.Block;
import cz.cvut.fel.pjv.blocks.LiquidBlock;
import cz.cvut.fel.pjv.items.StoredBlock;
import cz.cvut.fel.pjv.items.ItemType;
import cz.cvut.fel.pjv.items.Tool;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * Main class, starts the game.
 * 
 * @author Michal-jr
 * @version 1.0
 */
public class Main extends Application {
    
    private final double WIDTH = 1280;
    private final double HEIGHT = 640;
    Scene gameMenu, gameScreen;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Starts the application in start menu.
     *
     * @param primaryStage
     * @throws Exception
     * @since 1.0
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        final Stage stage = primaryStage;
        stage.setTitle("Basic Game");
        
        //gameMenu scene
        Group rootGameMenu = new Group();
        Button button1= new Button("Start game");
        button1.setPrefSize(WIDTH/10, HEIGHT/10);
        button1.setLayoutX(WIDTH/2 - WIDTH/20);
        button1.setLayoutY(HEIGHT/2 - HEIGHT/20);
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                startGame(stage);
            }
        });
        rootGameMenu.getChildren().add(button1);
        gameMenu = new Scene(rootGameMenu, WIDTH, HEIGHT, Color.BLACK);
        
        //gameScreen scene
        Group rootGameScreen = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        rootGameScreen.getChildren().add(canvas);
        gameScreen = new Scene(rootGameScreen, Color.BLACK);

        stage.setScene(gameMenu);
        stage.show();
    }
    
    /**
     * Starts the game.
     *
     * @param stage
     * @since 1.0
     */
    private void startGame(Stage stage) {
        final Game game = new Game(WIDTH);
        final World world = game.getWorld();
        final Player player = game.getPlayer();
        
        final Draw draw = new Draw(WIDTH, HEIGHT, world.getWIDTH());
        Canvas gameCanvas = (Canvas) gameScreen.getRoot().getChildrenUnmodifiable().get(0);
        final GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        gameScreen.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:     player.setUp(true); break;
                    case A:     player.setLeft(true); break;
                    case S:     player.setDown(true); break;
                    case D:     player.setRight(true); break;
                    case SHIFT: player.run(true); break;
                    case Q:     player.changeActiveItem(-1); break;
                    case E:     player.changeActiveItem(1); break;
                    case C:     draw.zoom(game); break;
                }
            }
        });

        gameScreen.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:     player.setUp(false); break;
                    case A:     player.setLeft(false); break;
                    case S:     player.setDown(false); break;
                    case D:     player.setRight(false); break;
                    case SHIFT: player.run(false); break;
                }
            }
        });
        
        gameScreen.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double clickX = event.getSceneX();
                double clickY = event.getSceneY();
                if (clickX > player.getX()-Block.block_width*4 && clickX < player.getX2()+Block.block_width*4
                    && clickY > player.getY()-Block.block_height*4 && clickY < player.getY2()+Block.block_height*4
                    && (clickX < player.getX() || clickX > player.getX2() || clickY < player.getY()-12 || clickY > player.getY2())
                ) {
                    boolean destroying = false;
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
                                        destroying = true;
                                    }
                                }
                            }
                        }
                    }
                    //build block
                    if (!destroying && player.getInventory().getActiveItem() instanceof StoredBlock) {
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
        });
        
        gameScreen.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getEventType() == ScrollEvent.SCROLL) {
                    draw.setZoomScale(draw.getZoomScale() + event.getDeltaY()/1000);
                    if (draw.getZoomScale() > draw.getMAX_ZOOM_SCALE()) { draw.setZoomScale(draw.getMAX_ZOOM_SCALE()); }
                    if (draw.getZoomScale() < draw.getMIN_ZOOM_SCALE()) { draw.setZoomScale(draw.getMIN_ZOOM_SCALE()); }
                    draw.zoom(game);
                }
            }
        });
        
        stage.setScene(gameScreen);
        
        AnimationTimer timer = new AnimationTimer() {
            long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 10_000_000) { //update each 10 miliseconds
                    player.update(world);
                    world.updateLiquids(player);
                    world.updateNPCs();
                    draw.shiftCamera(game);
                    draw.render(gc, game);
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }
    
}
