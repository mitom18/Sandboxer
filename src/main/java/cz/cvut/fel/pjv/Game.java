/*
 * The MIT License
 *
 * Copyright 2019 Zdenek.
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

import cz.cvut.fel.pjv.blocks.Block;
import cz.cvut.fel.pjv.creatures.Player;
import java.io.IOException;
import java.io.Serializable;

/**
 * Contains all entities in the game.
 *
 * @author Zdenek
 * @version 1.2
 */
public class Game implements Serializable {
    
    private final Player player;
    private final World world;
    private double zoomScale = 1;
    private double savedCameraOffsetX = 0;
    private double savedCameraOffsetY = 0;

    /**
     * Create new game.
     *
     * @param SCREEN_WIDTH width of the canvas in pixels
     * @throws java.io.IOException
     * @since 1.0
     */
    public Game(double SCREEN_WIDTH) throws IOException {
        // Create a new world.
        world = new World();
        
        int playerX = world.getWorldMap().getPlayerX();
        int playerY = (int) (world.getWorldMap().getPlayerY() * Block.block_height);
        // Create a new player at the given pixel coordinates.
        player = new Player(playerX, playerY);
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
    
    /**
     * Respawn dead player.
     *
     * @since 1.1
     */
    public void respawnPlayer() {
        player.setX(world.getPlayerSpawnX());
        player.setY(world.getHighestBlockY(world.getPlayerSpawnX()));
        player.revive();
    }

    /**
     * @return saved camera offset in X axis
     * @since 1.2
     */
    public double getSavedCameraOffsetX() {
        return savedCameraOffsetX;
    }

    /**
     * Save new camera offset in X axis.
     *
     * @param savedCameraOffsetX
     * @since 1.2
     */
    public void setSavedCameraOffsetX(double savedCameraOffsetX) {
        this.savedCameraOffsetX = savedCameraOffsetX;
    }

    /**
     * @return saved camera offset in Y axis
     * @since 1.2
     */
    public double getSavedCameraOffsetY() {
        return savedCameraOffsetY;
    }

    /**
     * Save new camera offset in Y axis.
     *
     * @param savedCameraOffsetY
     * @since 1.2
     */
    public void setSavedCameraOffsetY(double savedCameraOffsetY) {
        this.savedCameraOffsetY = savedCameraOffsetY;
    }

    /**
     * @return saved zoom scale
     * @since 1.2
     */
    public double getZoomScale() {
        return zoomScale;
    }

    /**
     * Save new zoom scale.
     *
     * @param zoomScale
     * @since 1.2
     */
    public void setZoomScale(double zoomScale) {
        this.zoomScale = zoomScale;
    }
    
}
