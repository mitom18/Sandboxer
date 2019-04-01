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
    
    private final double WIDTH = 640;
    private final double HEIGHT = 480;
    private double zoomScale = 1;
    private double oldZoomScale = 1;
    private final double MIN_ZOOM_SCALE = 0.5;
    private final double MAX_ZOOM_SCALE = 2;
    
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

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    Instances.player.setUp(true); break;
                    case LEFT:  Instances.player.setLeft(true); break;
                    case RIGHT: Instances.player.setRight(true); break;
                    case SHIFT: Instances.player.run(true); break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    Instances.player.setUp(false); break;
                    case LEFT:  Instances.player.setLeft(false); break;
                    case RIGHT: Instances.player.setRight(false); break;
                    case SHIFT: Instances.player.run(false); break;
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
                for (Block block : Instances.blocks) {
                    if (block.isDestroyed()) { continue; }
                    if (Collision.collides(clickX, clickY, block)) {
                        //Instances.blocks.remove(block);
                        block.destroy();
                        destroying = true;
                    }
                }
                //build block
                if (!destroying) {
                    double blockX = clickX - clickX % Block.block_width; //todo: better coords computing
                    double blockY = clickY - clickY % Block.block_height;
                    Instances.blocks.add(new Block(blockX, blockY));
                }
            }
        });
        
        scene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getEventType() == ScrollEvent.SCROLL) {
                    zoomScale += event.getDeltaY()/1000;
                    if (zoomScale > MAX_ZOOM_SCALE) { zoomScale = MAX_ZOOM_SCALE; }
                    if (zoomScale < MIN_ZOOM_SCALE) { zoomScale = MIN_ZOOM_SCALE; }
                    zoom(oldZoomScale, zoomScale);
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
                    Instances.player.update();
                    shiftCamera();
                    render(gc);
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }
    
    /**
     * If player is moving outside from canvas, move whole world,
     * so player stays in the canvas.
     * 
     * @since 1.0
     */
    private void shiftCamera() {
        double playerVelocityX = Instances.player.getVelocityX();
        double playerVelocityY = Instances.player.getVelocityY();
        
        if (Instances.player.getX() < 90 && Instances.player.movingLeft()) {
            Instances.player.setX(Instances.player.getX() + playerVelocityX);
            if (!Instances.item.isPicked()) {
                Instances.item.setX(Instances.item.getX() + playerVelocityX);
            }
            for (Block block : Instances.blocks) {
                if (block.isDestroyed()) { continue; }
                block.setX(block.getX() + playerVelocityX);
            }
        }
        
        if (Instances.player.getX() > WIDTH-90 && Instances.player.movingRight()) {
            Instances.player.setX(Instances.player.getX() - playerVelocityX);
            if (!Instances.item.isPicked()) {
                Instances.item.setX(Instances.item.getX() - playerVelocityX);
            }
            for (Block block : Instances.blocks) {
                if (block.isDestroyed()) { continue; }
                block.setX(block.getX() - playerVelocityX);
            }
        }
        
        if ((Instances.player.getY() < 90 && Instances.player.jumping()) || (Instances.player.getY() > HEIGHT-90 && Instances.player.jumping())) {
            Instances.player.setY(Instances.player.getY() - playerVelocityY);
            if (!Instances.item.isPicked()) {
                Instances.item.setY(Instances.item.getY() - playerVelocityY);
            }
            for (Block block : Instances.blocks) {
                if (block.isDestroyed()) { continue; }
                block.setY(block.getY() - playerVelocityY);
            }
        }
    }
    
    /**
     * Change the size of entities in the world and move the whole world
     * (x and y coords of all entities), so player is in the middle of the canvas.
     * 
     * @param oldScale old zoom scale
     * @param newScale new zoom scale
     * @since 1.0
     */
    private void zoom(double oldScale, double newScale) {
        double zoomScale;
        oldScale = 1/oldScale; //zoom reset
        for (int i = 0; i < 2; i++) {
            //first reset the view to default zoom
            if (i == 0) { zoomScale = oldScale; }
            //then zoom the view by set scale
            else { zoomScale = newScale; }
            
            double playerX = WIDTH/2;
            double playerY = HEIGHT/2;
            double offsetX = Instances.player.getX()*zoomScale - playerX;
            double offsetY = Instances.player.getY()*zoomScale - playerY;
            Instances.player.setVelocityMultiplier(zoomScale);
            Instances.player.setX(playerX);
            Instances.player.setY(playerY);
            Instances.player.setWidth(Instances.player.getWidth()*zoomScale);
            Instances.player.setHeight(Instances.player.getHeight()*zoomScale);
            if (!Instances.item.isPicked()) {
                Instances.item.setX(Instances.item.getX()*zoomScale - offsetX);
                Instances.item.setY(Instances.item.getY()*zoomScale - offsetY);
                Instances.item.setWidth(Instances.item.getWidth()*zoomScale);
                Instances.item.setHeight(Instances.item.getHeight()*zoomScale);
            }
            for (Block block : Instances.blocks) {
                if (block.isDestroyed()) { continue; }
                block.setX(block.getX()*zoomScale - offsetX);
                block.setY(block.getY()*zoomScale - offsetY);
                block.setWidth(block.getWidth()*zoomScale);
                block.setHeight(block.getHeight()*zoomScale);
            }
        }
        oldZoomScale = newScale; //store for zoom reset
    }

    /**
     * Draw the world and entities in it.
     * 
     * @param g a canvas 2D rendering context
     * @since 1.0
     */
    protected void render(GraphicsContext g) {
        //clear the canvas
        g.clearRect(0, 0, WIDTH, HEIGHT);
        //draw
        Player player = Instances.player;
        g.drawImage(player.getImage(), player.getSpriteX(), player.getSpriteY(), player.getIMAGE_WIDTH(), player.getIMAGE_HEIGHT(), 
                player.getX(), player.getY(), player.getWidth(), player.getHeight());
        for (Block block : Instances.blocks) {
            if (block.isDestroyed()) { continue; }
            g.setFill(block.getColor());
            g.fillRect(block.getX(), block.getY(), block.getWidth(), block.getHeight());
        }
        if (!Instances.item.isPicked()) {
            g.setFill(Instances.item.getColor());
            g.fillRect(Instances.item.getX(), Instances.item.getY(), Instances.item.getWidth(), Instances.item.getHeight());
        }
    }
    
}
