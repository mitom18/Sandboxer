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
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        GridPane startMenuGrid = new GridPane();
        startMenuGrid.setBackground(Background.EMPTY);
        startMenuGrid.setAlignment(Pos.CENTER);
        ColumnConstraints columnConstrains = new ColumnConstraints(200, 200, 200);
        startMenuGrid.getColumnConstraints().add(columnConstrains);
        Button startButton = new Button("Start game");
        startButton.setPrefSize(WIDTH/10, HEIGHT/10);
        startButton.setDefaultButton(true);
        startMenuGrid.add(startButton, 0, 0);
        GridPane.setHalignment(startButton, HPos.CENTER);
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                startGame(stage, savedGame);
            }
        });
        gameMenu = new Scene(startMenuGrid, WIDTH, HEIGHT, Color.BLACK);
        
        //gameScreen scene
        Group rootGameScreen = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        rootGameScreen.getChildren().add(canvas);
        gameScreen = new Scene(rootGameScreen, Color.BLACK);
        
        //respawnMenu scene
        GridPane respawnMenuGrid = new GridPane();
        respawnMenuGrid.setBackground(Background.EMPTY);
        respawnMenuGrid.setAlignment(Pos.CENTER);
        //ColumnConstraints columnConstrains = new ColumnConstraints(200, 200, 200);
        respawnMenuGrid.getColumnConstraints().add(columnConstrains);
        Label headerLabel = new Label("You died.");
        headerLabel.setTextFill(Color.RED);
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 38));
        respawnMenuGrid.add(headerLabel, 0, 0);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(0, 0, 150, 0));
        Button respawnButton = new Button("Respawn");
        respawnButton.setPrefSize(WIDTH/10, HEIGHT/10);
        respawnButton.setDefaultButton(true);
        respawnMenuGrid.add(respawnButton, 0, 1);
        GridPane.setHalignment(respawnButton, HPos.CENTER);
        respawnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (savedGame != null) {
                    savedGame.respawnPlayer();
                    startGame(stage, savedGame);
                }
            }
        });
        respawnMenu = new Scene(respawnMenuGrid, WIDTH, HEIGHT, Color.BLACK);

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

        GameAnimationTimer timer = new GameAnimationTimer(stage, draw, gc, game);
        
        EventHandlers eventHandlers = new EventHandlers(gameScreen, timer, draw, gc, game, player);
        eventHandlers.create();
        
        stage.setScene(gameScreen);
        draw.zoom(game); //center camera to player
        
        timer.start();
    }
    
    public static void saveGame(Game gameToSave) {
        savedGame = gameToSave;
    }
    
}
