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
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * Main class, starts the game.
 * 
 * @author Michal-jr
 * @version 1.0
 */
public class Main extends Application {
    
    private final int WIDTH = 640;
    private final int HEIGHT = 480;
    
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

        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 5_000_000) {
                    Instances.player.update();
                    updateCamera();
                    render(gc);
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }
    
    private void updateCamera() {
        int playerVelocity = (int) Instances.player.getVelocityX();
       
        if (Instances.player.getX() < 90 && Instances.player.movingLeft()) {
            Instances.player.setX(Instances.player.getX() + playerVelocity);
            if (!Instances.item.isPicked()) {
                Instances.item.setX(Instances.item.getX() + playerVelocity);
            }
            for (Block block : Instances.blocks) {
                block.setX(block.getX() + playerVelocity);
            }
        }
        
        if (Instances.player.getX() > WIDTH-90 && Instances.player.movingRight()) {
            Instances.player.setX(Instances.player.getX() - playerVelocity);
            if (!Instances.item.isPicked()) {
                Instances.item.setX(Instances.item.getX() - playerVelocity);
            }
            for (Block block : Instances.blocks) {
                block.setX(block.getX() - playerVelocity);
            }
        }
    }

    /**
     * Draw the world and entities in it.
     * 
     * @param g a canvas 2D rendering context
     * @since 1.0
     */
    protected void render(GraphicsContext g) {
        g.clearRect(0, 0, WIDTH, HEIGHT);
        for (Block block : Instances.blocks) {
            g.setFill(block.getColor());
            g.fillRect(block.getX(), block.getY(), block.getWIDTH(), block.getHEIGHT());
        }
        if (!Instances.item.isPicked()) {
            g.setFill(Instances.item.getColor());
            g.fillRect(Instances.item.getX()+2, Instances.item.getY()+2, 10, 10);
        }
        g.setFill(Instances.player.getColor());
        g.fillRect(Instances.player.getX(), Instances.player.getY(), Instances.player.getWIDTH(), Instances.player.getHEIGHT());
    }
    
}
