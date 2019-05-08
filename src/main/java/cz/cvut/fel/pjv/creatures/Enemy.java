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
import cz.cvut.fel.pjv.items.Tool;
import cz.cvut.fel.pjv.items.Item;

/**
 * NPC that is attacking player.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class Enemy extends NPC {
    
    private final int attackRate = 20;
    private int attackCounter = 0;
    private final int attackPower = 1;
    private int movementCounter = 0;
    private boolean wantGoLeft = true;
    private boolean wantGoRight = false;
    private final Item dropItem;

    /**
     * Create new enemy NPC that is attacking the player.
     *
     * @param x creature's X position in pixels
     * @param y creature's Y position in pixels
     * @param type type of the creature from enumeration
     * @param world instance of the world
     * @since 1.0
     */
    public Enemy(double x, double y, CreatureType type, World world) {
        super(x, y, type);
        if (type.getDropItemType() != null) {
            dropItem = new Tool(x, y, type.getDropItemType());
            world.addItem(dropItem);
        } else {
            dropItem = null;
        }
    }
    
    @Override
    public void update(World world, Player player) {
        Collision.creatureIsInLiquid(this, world);
        calculateMovement(world, player);
        move();
        if (dropItem != null) {
            dropItem.setX(getX());
            dropItem.setY(getY());
        }
        Collision.preventCollision(this, world);
        if (attackCounter == attackRate) {
            attack(player);
            attackCounter = 0;
        }
        attackCounter++;
        if (isAttacking()) { animateAttack(); }
    }
    
    private void calculateMovement(World world, Player player) {
        if ( //enemy is not moving if he is more than 150 blocks far from the player
            player.getX() > getX2()+150*Block.block_width ||
            player.getX2() < getX()-150*Block.block_width
        ) { return; }
        if (
            player.getX2() > getX()-10*Block.block_width && 
            player.getX() < getX2()+10*Block.block_width
        ) {
            if (player.getX2() < getX()) {
                setRight(false);
                setLeft(true);
            }
            else if (player.getX() > getX2()) {
                setLeft(false);
                setRight(true);
            }
        } else {
            movementCounter++;
            setUp(false);
            if (movementCounter == 200) {
                wantGoLeft = !wantGoLeft;
                wantGoRight = !wantGoRight;
                movementCounter = 0;
            }
            setLeft(wantGoLeft);
            setRight(wantGoRight);
        }
        if (Collision.creatureHasBlockInFront(this, world) || swimming()) { setUp(true); }
    }
    
    /**
     * Attack player and update his HP if attack was successful.
     *
     * @param player instance of player
     * @since 1.0
     */
    public void attack(Player player) {
        if (player.getX2() > getX()-Block.block_width*4 && player.getX() < getX2()+Block.block_width*4) {
            if (!isAttacking()) { setAttacking(true); }
            double attackWidth;
            setLeftAttack(false);
            setRightAttack(false);
            if (player.getX2() < getX()+getWidth()/2) {
                attackWidth = -getWidth()/2;
                setLeftAttack(true);
            }
            else if (player.getX() > getX()+getWidth()/2){
                attackWidth = getWidth()/2;
                setRightAttack(true);
            }
            else { attackWidth = 0; }
            if (Collision.creatureIsAttacked(getX()+getWidth()/2, getY(), attackWidth, getHeight(), player)) {
                player.setHp(player.getHp()-attackPower);
                if (player.getHp() <= 0) { player.die(); }
            }
        }
    }

    @Override
    public void die() {
        super.die();
        if (dropItem != null) { dropItem.setPicked(false); }
    }
    
}
