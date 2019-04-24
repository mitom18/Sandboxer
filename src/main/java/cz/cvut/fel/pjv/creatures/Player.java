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
 * Character that is controlled by player.
 * 
 * @author Michal-jr
 * @version 1.0
 */
public class Player extends Creature {
    
    private final Inventory inventory = new Inventory();
    private int saturation = 10;

    /**
     * Create player on given coordinates.
     *
     * @param x
     * @param y
     * @since 1.0
     */
    public Player(double x, double y) {
        super(x, y, CreatureType.PLAYER);
    }
    
    /**
     * Update player's state.
     * 
     * @param world instance of the world
     * @since 1.0
     */
    public void update(World world) {
        Collision.creatureIsInLiquid(this, world);
        move();
        Collision.preventCollision(this, world);
        Collision.controlItems(this, world);
        if (isAttacking()) { animateAttack(); }
    }
    
    public void attack(World world, double clickX) {
        if (!isAttacking()) { setAttacking(true); }
        double attackX;
        setLeftAttack(false);
        setRightAttack(false);
        if (clickX < getX()+getWidth()/2) {
            attackX = getX()-getWidth();
            setLeftAttack(true);
        } else {
            attackX = getX2()+getWidth();
            setRightAttack(true);
        }
        for (NPC npc : world.getNpcs()) {
            if (!(npc instanceof Enemy)) { continue; }
            if (Collision.creatureIsAttacked(attackX, getY()+getHeight()/2, npc)) {
                npc.setHp(npc.getHp()-1);
                if (npc.getHp() <= 0) { npc.die(); }
            }
        }
    }

    /**
     * Scroll through inventory items in hotbar.
     *
     * @param moveBy
     * @since 1.0
     */
    public void changeActiveItem(int moveBy) {
        int moveTo = (inventory.getActiveItemIndex() + moveBy) % 10;
        if (moveTo < 0) { moveTo = 9; }
        inventory.setActiveItemIndex(moveTo);
    }
    
    /**
     * @return player's inventory
     * @since 1.0
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * @return player's saturation points
     * @since 1.0
     */
    public int getSaturation() {
        return saturation;
    }
    
}
