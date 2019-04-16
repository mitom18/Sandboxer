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
import cz.cvut.fel.pjv.blocks.LiquidBlock;
import cz.cvut.fel.pjv.blocks.Block;
import cz.cvut.fel.pjv.creatures.Creature;
import cz.cvut.fel.pjv.items.Item;

/**
 * Collision management.
 * 
 * @author Michal-jr
 * @version 1.0
 */
public abstract class Collision {
    
    /**
     * Check if player is colliding with other entity.
     * If it is colliding than edit player's coordinates.
     * 
     * @param player instance of player
     * @param world instance of world where player exists
     * @since 1.0
     */
    public static void preventCollision(Creature creature, World world) {
        for (Block block : world.getBlocks()) {
            if (block.isDestroyed() || block instanceof LiquidBlock) { continue; }
            if (!(block.getX() > creature.getX()-block.getWidth()*4 && block.getX2() < creature.getX2()+block.getWidth()*4)) {
                continue;
            }
            if (collides(creature, block)) {
                double bottomCollision = block.getY2() - creature.getY();
                double topCollision = creature.getY2() - block.getY();
                double leftCollision = creature.getX2() - block.getX();
                double rightCollision = block.getX2() - creature.getX();

                if(topCollision < bottomCollision && topCollision < leftCollision && topCollision < rightCollision) {
                    creature.setY(block.getY()-creature.getHeight()); //top collision
                    creature.setVelocityY(0.0); //set player's velocity to 0
                    creature.setOnGround(true); //player is standing on the ground
                }
                if(bottomCollision < topCollision && bottomCollision < leftCollision && bottomCollision < rightCollision)
                    creature.setY(block.getY2()); //bottom collision
                if(leftCollision < rightCollision && leftCollision < topCollision && leftCollision < bottomCollision)
                    creature.setX(block.getX()-creature.getWidth()); //left collision
                if(rightCollision < leftCollision && rightCollision < topCollision && rightCollision < bottomCollision)
                    creature.setX(block.getX2()); //right collision
            }
        }
    }
    
    private static boolean collides(Creature creature, Block block) {
        return creature.getX() < block.getX2() && creature.getX2() > block.getX() && creature.getY() < block.getY2() && creature.getY2() > block.getY();
    }
    
    public static void creatureIsInLiquid(Creature creature, World world) {
        boolean collidesWithBlock = false;
        for (Block block : world.getBlocks()) {
            if (block.isDestroyed()) { continue; }
            if (!(block.getX() > creature.getX()-block.getWidth()*4 && block.getX2() < creature.getX2()+block.getWidth()*4)) {
                continue;
            }
            if (collides(creature, block)) {
                collidesWithBlock = true;
                if (block instanceof LiquidBlock) {
                    creature.setSwimming(true);
                } else {
                    creature.setSwimming(false);
                }
            }
        }
        if (!collidesWithBlock) { creature.setSwimming(false); }
    }
    
    /**
     * Check if user clicked on block.
     *
     * @param clickX
     * @param clickY
     * @param block
     * @return true if user clicked on block, false otherwise
     * @since 1.0
     */
    public static boolean collides(double clickX, double clickY, Block block) {
        if (block.isDestroyed()) { return block instanceof LiquidBlock; }
        return clickX < block.getX2() && clickX > block.getX() && clickY < block.getY2() && clickY > block.getY();
    }
    
    /**
     * Check if player can pick any of the items in the world.
     * If he can, item is added to player's inventory.
     *
     * @param player instance of player
     * @param world instance of world where player exists
     * @since 1.0
     */
    public static void controlItems(Player player, World world) {
        for (Item item : world.getItems()) {
            if (canPickItem(player, item)) {
                player.getInventory().add(item);
                item.setPicked(true);
            }
        }
    }
    
    private static boolean canPickItem(Player player, Item item) {
        if (item.isPicked()) { return false; }
        return player.getX() < item.getX()+12 && player.getX2() > item.getX()+2 && player.getY() < item.getY()+12 && player.getY2() > item.getY()+2;
    }
    
}
