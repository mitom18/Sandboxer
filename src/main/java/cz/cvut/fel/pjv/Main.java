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

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, Color.BLACK);
        stage.setScene(scene);
        stage.setTitle("Basic Game");

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        
        final Draw draw = new Draw(WIDTH, HEIGHT);
        
        final Game game = new Game();
        final World world = game.getWorld();
        final Player player = game.getPlayer();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    player.setUp(true); break;
                    case LEFT:  player.setLeft(true); break;
                    case RIGHT: player.setRight(true); break;
                    case SHIFT: player.run(true); break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    player.setUp(false); break;
                    case LEFT:  player.setLeft(false); break;
                    case RIGHT: player.setRight(false); break;
                    case SHIFT: player.run(false); break;
                }
            }
        });
        
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double clickX = event.getSceneX();
                double clickY = event.getSceneY();
                boolean destroying = false;
                //destroy block
                for (Block block : world.getBlocks()) {
                    if (block.isDestroyed()) { continue; }
                    if (Collision.collides(clickX, clickY, block)) {
                        block.destroy();
                        destroying = true;
                    }
                }
                //build block
                if (!destroying) {
                    double blockX = clickX - (clickX - draw.getCameraOffsetX()) % Block.block_width;
                    double blockY = clickY - (clickY - draw.getCameraOffsetY()) % Block.block_height;
                    world.getBlocks().add(new Block(blockX, blockY));
                }
            }
        });
        
        scene.setOnScroll(new EventHandler<ScrollEvent>() {
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

        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 10_000_000) { //update each 10 miliseconds
                    player.update(world);
                    draw.shiftCamera(game);
                    draw.render(gc, game);
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }
    
}
