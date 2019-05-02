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

import cz.cvut.fel.pjv.fileio.GameSaver;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Class for creating forms, alerts and other ui controls.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class UIControls {
    private final double WIDTH;
    private final double HEIGHT;
    private final Stage stage;

    public UIControls(double WIDTH, double HEIGHT, Stage stage) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.stage = stage;
    }
    
    public GridPane createStartMenu() {
        GridPane startMenuGrid = new GridPane();
        startMenuGrid.setBackground(Background.EMPTY);
        startMenuGrid.setAlignment(Pos.CENTER);
        ColumnConstraints columnOneConstrains = new ColumnConstraints(200, 200, 200);
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200, 200, 200);
        startMenuGrid.getColumnConstraints().addAll(columnOneConstrains, columnTwoConstrains);
        Button newGameButton = new Button("New game");
        newGameButton.setPrefSize(WIDTH/10, HEIGHT/10);
        startMenuGrid.add(newGameButton, 0, 0);
        GridPane.setHalignment(newGameButton, HPos.CENTER);
        newGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    Main.startGame(stage, null);
                } catch (FileNotFoundException ex) {
                        createErrorAlert("Game could not start. Game config file is missing.");
                } catch (IOException ex) {
                    createErrorAlert("Game could not start. Game config file is corrupted.");
                }
            }
        });
        Button loadGameButton = new Button("Load game");
        loadGameButton.setPrefSize(WIDTH/10, HEIGHT/10);
        startMenuGrid.add(loadGameButton, 1, 0);
        GridPane.setHalignment(loadGameButton, HPos.CENTER);
        if (GameSaver.savesFolderIsEmpty()) { loadGameButton.setDisable(true); }
        loadGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                final String fileName = createSelectDialog(GameSaver.getSavedGames(), "Load game", "Choose the game to load:");
                if (fileName != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Main.gameMenu.getRoot().getChildrenUnmodifiable().get(0).setVisible(false);
                            Main.gameMenu.getRoot().getChildrenUnmodifiable().get(1).setVisible(false);
                            Main.gameMenu.getRoot().getChildrenUnmodifiable().get(2).setVisible(true);
                        }
                    });
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                System.err.println("Game could not be loaded. Loading process was interrupted.");
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Main.savedGame = GameSaver.loadSavedGame(fileName);
                                    try {
                                        Main.startGame(stage, Main.savedGame);
                                    } catch (FileNotFoundException ex) {
                                        createErrorAlert("Game could not start. Game config file is missing.");
                                    } catch (IOException ex) {
                                        createErrorAlert("Game could not start. Game config file is corrupted.");
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });
        Label loadingLabel = new Label("Loading...");
        loadingLabel.setTextFill(Color.WHITE);
        loadingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        startMenuGrid.add(loadingLabel, 0, 0, 2, 1);
        GridPane.setHalignment(loadingLabel, HPos.CENTER);
        GridPane.setMargin(loadingLabel, new Insets(0, 0, 0, 0));
        loadingLabel.setVisible(false);
        
        return startMenuGrid;
    }
    
    public Group createGameScreen() {
        Group rootGameScreen = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        Button saveButton = new Button("Save game");
        saveButton.setLayoutX(WIDTH/2-30);
        saveButton.setLayoutY(HEIGHT-250);
        saveButton.setVisible(false);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Main.gameScreen.getRoot().getChildrenUnmodifiable().get(1).setDisable(true);
                        Main.gameScreen.getRoot().getChildrenUnmodifiable().get(2).setVisible(true);
                    }
                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            createErrorAlert("Game could not be saved. Saving process was interrupted.");
                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                GameSaver.saveGame(Main.savedGame);
                                Main.gameScreen.getRoot().getChildrenUnmodifiable().get(1).setDisable(false);
                                Main.gameScreen.getRoot().getChildrenUnmodifiable().get(2).setVisible(false);
                            }
                        });
                    }
                }).start();   
            }
        });
        Label savingLabel = new Label("Saving...");
        savingLabel.setTextFill(Color.WHITE);
        savingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        savingLabel.setLayoutX(WIDTH/2-35);
        savingLabel.setLayoutY(HEIGHT-200);
        savingLabel.setVisible(false);
        rootGameScreen.getChildren().addAll(canvas, saveButton, savingLabel);
        
        return rootGameScreen;
    }
    
    public GridPane createRespawnMenu() {
        GridPane respawnMenuGrid = new GridPane();
        respawnMenuGrid.setBackground(Background.EMPTY);
        respawnMenuGrid.setAlignment(Pos.CENTER);
        ColumnConstraints columnConstrains = new ColumnConstraints(200, 200, 200);
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
                if (Main.savedGame != null) {
                    Main.savedGame.respawnPlayer();
                    try {
                        Main.startGame(stage, Main.savedGame);
                    } catch (FileNotFoundException ex) {
                        createErrorAlert("Game could not start. Game config file is missing.");
                    } catch (IOException ex) {
                        createErrorAlert("Game could not start. Game config file is corrupted.");
                    } 
                }
            }
        });
        
        return respawnMenuGrid;
    }
    
    public static void createAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static void createErrorAlert(String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static String createInputDialog(String defaultText, String title, String content) {
        TextInputDialog dialog = new TextInputDialog(defaultText);
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()){
            return null;
        }
        return result.get();
    }
    
    public static String createSelectDialog(ArrayList<String> choices, String title, String content) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()){
            return null;
        }
        return result.get();
    }
    
    public static void showSaveButton() {
        Main.gameScreen.getRoot().getChildrenUnmodifiable().get(1).setVisible(true);
    }
    
    public static void hideSaveButton() {
        Main.gameScreen.getRoot().getChildrenUnmodifiable().get(1).setVisible(false);
    }
}
