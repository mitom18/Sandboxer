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
 * NPC that is attacking player.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class Enemy extends NPC {
    
    private int attackRate = 20;
    private int attackCounter = 0;
    private int attackPower = 1;

    public Enemy(double x, double y, CreatureType type) {
        super(x, y, type);
    }
    
    @Override
    public void update(World world, Player player) {
        Collision.creatureIsInLiquid(this, world);
        if (player.getX2() < getX()) {
            setRight(false);
            setLeft(true);
        }
        else if (player.getX() > getX2()) {
            setLeft(false);
            setRight(true);
        }
        else {
            setLeft(false);
            setRight(false);
        }
        setUp(true);
        move();
        Collision.preventCollision(this, world);
        if (attackCounter == attackRate) {
            attack(player);
            attackCounter = 0;
        }
        attackCounter++;
        if (isAttacking()) { animateAttack(); }
    }
    
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
    
}
