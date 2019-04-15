/*
 * The MIT License
 *
 * Copyright 2019 Zdeněk.
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

/**
 * Contains all entities in the game.
 *
 * @author Zdenek
 * @version 1.0
 */
public class Game {
    
    /**
     * Instance of Player.
     */
    private Player player;
    
    /*
     * Instance of World.
     */
    private World world;

    /**
     * Create new game.
     *
     * @param SCREEN_WIDTH width of the canvas in pixels
     * @since 1.0
     */
    public Game(double SCREEN_WIDTH) {
        player = new Player((int) SCREEN_WIDTH/2, 0);
        world = new World();
    }

    /**
     * @return instance of the player
     * @since 1.0
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return instance of the world
     * @since 1.0
     */
    public World getWorld() {
        return world;
    }
    
}
