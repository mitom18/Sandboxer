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
package cz.cvut.fel.pjv.fileio;

import cz.cvut.fel.pjv.Game;
import cz.cvut.fel.pjv.UIControls;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert.AlertType;

/**
 * Class for managing game saving and loading.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class GameSaver {

    /**
     * Save game to file.
     *
     * @param gameToSaveToFile instance of the game to be saved in file
     * @since 1.0
     */
    public static void saveGame(Game gameToSaveToFile) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	Date date = new Date();
        String fileName = UIControls.createInputDialog("save_"+dateFormat.format(date), "Save as", "Please enter the name of the file:");
        if (fileName != null) {
            new File("saves").mkdirs();
            try (
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saves/"+fileName+".txt"))
            ) {
                oos.writeObject(gameToSaveToFile);
                UIControls.createAlert(AlertType.INFORMATION, "Success", null, "The game was saved successfully!");
            } catch (IOException ex) {
                UIControls.createErrorAlert("An error occured during file saving.");
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, "An error occured during file saving.", ex);
            }
        }
    }
    
    /**
     * Load saved game from file.
     *
     * @param fileName name of the file in which the game is saved
     * @return instance of the loaded game
     * @since 1.0
     */
    public static Game loadSavedGame(String fileName) {
        if (fileName != null) {
            try (
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saves/"+fileName))
            ) {
                Game game = (Game) ois.readObject();
                return game;
            } catch (ClassNotFoundException ex) {
                UIControls.createErrorAlert("Game could not be loaded. Loaded file doesn't contain game object.");
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, "Game could not be loaded. Loaded file doesn't contain game object.", ex);
            } catch (IOException ex) {
                UIControls.createErrorAlert("An error occured during file loading.");
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, "An error occured during file loading.", ex);
            }
        }
        return null;
    }
    
    /**
     * @return list of names of all saved games
     * @since 1.0
     */
    public static ArrayList<String> getSavedGames() {
        ArrayList<String> results = new ArrayList<>();
        File[] files = new File("saves").listFiles();
        if (files.length > 0) { sortFilesByDateCreated(files); }
        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }
        return results;
    }
    
    /**
     * Sort list of files by date of creation from the newest to the oldest.
     *
     * @param files list of files objects
     * @since 1.0
     */
    public static void sortFilesByDateCreated (File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare (File f1, File f2) {
                long l1 = getFileCreationEpoch(f1);
                long l2 = getFileCreationEpoch(f2);
                return Long.valueOf(l2).compareTo(l1);
            }
        });
    }

    /**
     * Get date of the given file's creation.
     *
     * @param file file object
     * @return date of file's creation as long
     * @since 1.0
     */
    public static long getFileCreationEpoch (File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(),
                    BasicFileAttributes.class);
            return attr.creationTime()
                       .toInstant().toEpochMilli();
        } catch (IOException e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, null, e);
            throw new RuntimeException(file.getAbsolutePath(), e);
        }
    }
    
    /**
     * @return true if save folder is empty, false otherwise
     * @since 1.0
     */
    public static boolean savesFolderIsEmpty() {
        new File("saves").mkdirs();
        return new File("saves").listFiles().length == 0;
    }
}
