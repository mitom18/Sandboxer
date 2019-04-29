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
package cz.cvut.fel.pjv.creatures;

import cz.cvut.fel.pjv.Collision;
import cz.cvut.fel.pjv.World;

/**
 * Character that is not controlled by player.
 *
 * @author Michal-jr
 * @version 1.0
 */
public abstract class NPC extends Creature {

    /**
     * Create new NPC on given coordinates.
     *
     * @param x
     * @param y
     * @param type
     * @since 1.0
     */
    public NPC(double x, double y, CreatureType type) {
        super(x, y, type);
    }
    
    /**
     * Update NPC's state.
     * 
     * @param world instance of the world
     * @param player instance of the player
     * @since 1.0
     */
    public void update(World world, Player player) {
        Collision.creatureIsInLiquid(this, world);
        setUp(true);
        move();
        Collision.preventCollision(this, world);
    }
  
}
