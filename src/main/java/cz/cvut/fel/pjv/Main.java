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

import cz.cvut.fel.pjv.fileio.FileLogger;
import cz.cvut.fel.pjv.creatures.Player;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * Main class, starts the game.
 * 
 * @author Michal-jr
 * @version 1.1
 */
public class Main extends Application {
    
    private static final double WIDTH = 1280;
    private static final double HEIGHT = 640;
    static Scene gameMenu, gameScreen, respawnMenu;
    static Game savedGame;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            FileLogger.setup();
        } catch (IOException ex) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, "Logger could not be initialized.", ex);
        }
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
        stage.setTitle("Sandboxer");
        
        UIControls ui = new UIControls(WIDTH, HEIGHT, stage);
        gameMenu = new Scene(ui.createStartMenu(), WIDTH, HEIGHT, Color.BLACK);
        gameScreen = new Scene(ui.createGameScreen(), Color.BLACK);
        respawnMenu = new Scene(ui.createRespawnMenu(), WIDTH, HEIGHT, Color.BLACK);

        stage.setScene(gameMenu);
        stage.show();
    }
    
    /**
     * Starts the game.
     *
     * @param givenStage
     * @param savedGame
     * @throws java.io.IOException
     * @since 1.0
     */
    public static void startGame(Stage givenStage, Game savedGame) throws IOException {
        final Stage stage = givenStage;
        final Game game;
        if (savedGame == null) {
            game = new Game(WIDTH);
            saveGame(game);
        } else {
            game = savedGame;
        }
        final World world = game.getWorld();
        final Player player = game.getPlayer();
        
        final Draw draw = new Draw(WIDTH, HEIGHT, world.getWIDTH(), game.getZoomScale(), game.getSavedCameraOffsetX(), game.getSavedCameraOffsetY());
        Canvas gameCanvas = (Canvas) gameScreen.getRoot().getChildrenUnmodifiable().get(0);
        final GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        GameAnimationTimer timer = new GameAnimationTimer(stage, draw, gc, game);
        
        EventHandlers eventHandlers = new EventHandlers(gameScreen, timer, draw, gc, game, player);
        eventHandlers.create();
        
        stage.setScene(gameScreen);
        draw.zoom(game); //center camera to player
        
        timer.start();
    }
    
    /**
     * Save the game temporarily. This is not saving game to file.
     *
     * @param gameToSave instance of game to be temporarily saved
     * @since 1.1
     */
    public static void saveGame(Game gameToSave) {
        savedGame = gameToSave;
    }
}
