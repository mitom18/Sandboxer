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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

/**
 * Game loop. Timer extends AnimationTimer and has methods for check its status.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class GameAnimationTimer extends AnimationTimer {
    private volatile boolean running;
    private long lastUpdate = 0;
    private final Stage stage;
    private final Draw draw;
    private final GraphicsContext gc;
    private final Game game;
    private final Player player;
    private final World world;

    /**
     * Create new animation timer used as game loop.
     *
     * @param stage instance of javafx stage (window)
     * @param draw instance of draw class
     * @param gc a canvas 2D rendering context
     * @param game instance of game
     * @since 1.0
     */
    public GameAnimationTimer(Stage stage, Draw draw, GraphicsContext gc, Game game) {
        this.stage = stage;
        this.draw = draw;
        this.gc = gc;
        this.game = game;
        this.player = game.getPlayer();
        this.world = game.getWorld();
    }
    
    @Override
    public void handle(long now) {
        if (now - lastUpdate >= 17_000_000) { //update each 17 miliseconds
            player.update(world);
            world.updateNPCs(player);
            world.updateLayingItems();
            draw.shiftCamera(game);
            draw.render(gc, game);
            lastUpdate = now;
            if (player.isKilled()) {
                stop();
                Main.saveGame(game);
                stage.setScene(Main.respawnMenu);
            }
        }
    }
    
    @Override
    public void start() {
        super.start();
        running = true;
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Game started.");
    }

    @Override
    public void stop() {
        super.stop();
        running = false;
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Game paused.");
    }

    /**
     * @return true if timer thread is running, false otherwise
     * @since 1.0
     */
    public boolean isRunning() {
        return running;
    }

}
