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
import cz.cvut.fel.pjv.blocks.Block;

/**
 * NPC that is not attacking player.
 *
 * @author Michal-jr
 * @version 1.1
 */
public class Friend extends NPC {

    private int movementCounter = 0;
    private boolean wantGoLeft = true;
    private boolean wantGoRight = false;
    private String somethingToSay;
    
    /**
     * Create new friendly NPC.
     *
     * @param x creature's X position in pixels
     * @param y creature's Y position in pixels
     * @param type type of the creature from enumeration
     * @since 1.0
     */
    public Friend(double x, double y, CreatureType type) {
        super(x, y, type);
    }

    @Override
    public void update(World world, Player player) {
        Collision.creatureIsInLiquid(this, world);
        calculateMovement(world, player);
        move();
        Collision.preventCollision(this, world);
        tellPlayerHeWon(player);
    }
    
    private void calculateMovement(World world, Player player) {
        if ( //friendly NPC is not moving if he is more than 150 blocks far from the player
            player.getX() > getX2()+150*Block.block_width ||
            player.getX2() < getX()-150*Block.block_width
        ) { return; }
        movementCounter++;
        setUp(false);
        if (movementCounter == 100) {
            wantGoLeft = !wantGoLeft;
            wantGoRight = !wantGoRight;
            movementCounter = 0;
        }
        setLeft(wantGoLeft);
        setRight(wantGoRight);
        if (Collision.creatureHasBlockInFront(this, world) || swimming()) { setUp(true); }
    }
    
    private void tellPlayerHeWon(Player player) {
        if (getType() != CreatureType.MONK) { return; }
        if (!player.isWinner() && getY() == player.getY() && player.getX() < getX2()+Block.block_width && player.getX2() > getX()-Block.block_width) {
            somethingToSay = "Greetings young one. Welcome to the chamber of winners. You are now one of them, for you have just won the whole game.";
            player.setHasWon(true);
        }
    }

    /**
     * @return string that should appear on the game screen
     * @since 1.1
     */
    public String getSomethingToSay() {
        return somethingToSay;
    }

    /**
     * Set what friend is supposed to say.
     *
     * @param somethingToSay
     * @since 1.1
     */
    public void setSomethingToSay(String somethingToSay) {
        this.somethingToSay = somethingToSay;
    }
    
}
