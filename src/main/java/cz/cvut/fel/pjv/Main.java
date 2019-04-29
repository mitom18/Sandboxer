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
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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
    static Scene gameMenu, gameScreen, respawnMenu;
    static Game savedGame;
    
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
        Button button1 = new Button("Start game");
        button1.setPrefSize(WIDTH/10, HEIGHT/10);
        button1.setLayoutX(WIDTH/2 - WIDTH/20);
        button1.setLayoutY(HEIGHT/2 - HEIGHT/20);
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                startGame(stage, savedGame);
            }
        });
        rootGameMenu.getChildren().add(button1);
        gameMenu = new Scene(rootGameMenu, WIDTH, HEIGHT, Color.BLACK);
        
        //gameScreen scene
        Group rootGameScreen = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        rootGameScreen.getChildren().add(canvas);
        gameScreen = new Scene(rootGameScreen, Color.BLACK);
        
        //respawnMenu scene
        Group rootRespawnMenu = new Group();
        Button button2 = new Button("Respawn");
        button2.setPrefSize(WIDTH/10, HEIGHT/10);
        button2.setLayoutX(WIDTH/2 - WIDTH/20);
        button2.setLayoutY(HEIGHT/2 - HEIGHT/20);
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (savedGame != null) {
                    savedGame.respawnPlayer();
                    startGame(stage, savedGame);
                }
            }
        });
        rootRespawnMenu.getChildren().add(button2);
        respawnMenu = new Scene(rootRespawnMenu, WIDTH, HEIGHT, Color.BLACK);

        stage.setScene(gameMenu);
        stage.show();
    }
    
    /**
     * Starts the game.
     *
     * @param stage
     * @since 1.0
     */
    private void startGame(Stage givenStage, Game savedGame) {
        final Stage stage = givenStage;
        final Game game;
        if (savedGame == null) {
            game = new Game(WIDTH);
        } else {
            game = savedGame;
        }
        final World world = game.getWorld();
        final Player player = game.getPlayer();
        
        final Draw draw = new Draw(WIDTH, HEIGHT, world.getWIDTH());
        Canvas gameCanvas = (Canvas) gameScreen.getRoot().getChildrenUnmodifiable().get(0);
        final GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        EventHandlers eventHandlers = new EventHandlers(gameScreen, draw, game, player);
        eventHandlers.create();
        
        stage.setScene(gameScreen);
        draw.zoom(game); //center camera to player
        
        GameAnimationTimer timer = new GameAnimationTimer(stage, draw, gc, game);
        timer.start();
    }
    
    public static void saveGame(Game gameToSave) {
        savedGame = gameToSave;
    }
    
}
