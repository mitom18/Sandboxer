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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for managing game saving and loading.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class GameSaver {
    public static void saveGame(Game gameToSaveToFile) {
        new File("saves").mkdirs();
        try (
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saves/savedWorld.txt"))
        ) {
            oos.writeObject(gameToSaveToFile);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("An error occured during file saving.");
        }
    }
    
    public static Game loadSavedGame() {
        try (
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saves/savedWorld.txt"))
        ) {
            Game game = (Game) ois.readObject();
            return game;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.err.println("Game could not be loaded. Loaded file doesn't contain game object.");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("An error occured during file loading.");
        }
        return null;
    }
}
