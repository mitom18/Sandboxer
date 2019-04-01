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
     * @since 1.0
     */
    public static void preventCollision(Player player) {
        for (Block block : Instances.blocks) {
            if (block.isDestroyed()) { continue; }
            if (!(block.getX() > player.getX()-block.getWidth()*4 && block.getX2() < player.getX2()+block.getWidth()*4)) {
                continue;
            }
            if (collides(player, block)) {
                double bottomCollision = block.getY2() - player.getY();
                double topCollision = player.getY2() - block.getY();
                double leftCollision = player.getX2() - block.getX();
                double rightCollision = block.getX2() - player.getX();

                if(topCollision < bottomCollision && topCollision < leftCollision && topCollision < rightCollision) {
                    player.setY(block.getY()-player.getHeight()); //top collision
                    player.setVelocityY(0.0); //set player's velocity to 0
                    player.setOnGround(true); //player is standing on the ground
                }
                if(bottomCollision < topCollision && bottomCollision < leftCollision && bottomCollision < rightCollision)
                    player.setY(block.getY2()); //bottom collision
                if(leftCollision < rightCollision && leftCollision < topCollision && leftCollision < bottomCollision)
                    player.setX(block.getX()-player.getWidth()); //left collision
                if(rightCollision < leftCollision && rightCollision < topCollision && rightCollision < bottomCollision)
                    player.setX(block.getX2()); //right collision
            }
        }
    }
    
    private static boolean collides(Player player, Block block) {
        return player.getX() < block.getX2() && player.getX2() > block.getX() && player.getY() < block.getY2() && player.getY2() > block.getY();
    }
    
    public static boolean collides(double clickX, double clickY, Block block) {
        return clickX < block.getX2() && clickX > block.getX() && clickY < block.getY2() && clickY > block.getY();
    }
    
    /**
     * Check if player can pick any of the items in the world.
     * If he can, item is added to player's inventory.
     *
     * @param player instance of player
     * @since 1.0
     */
    public static void controlItems(Player player) {
        if (canPickItem(player, Instances.item)) {
            player.getInventory().add(Instances.item);
            Instances.item.setPicked(true);
            System.out.println(player.getInventory());
        }
    }
    
    private static boolean canPickItem(Player player, Item item) {
        if (item.isPicked()) { return false; }
        return player.getX() < item.getX()+12 && player.getX2() > item.getX()+2 && player.getY() < item.getY()+12 && player.getY2() > item.getY()+2;
    }
    
}
