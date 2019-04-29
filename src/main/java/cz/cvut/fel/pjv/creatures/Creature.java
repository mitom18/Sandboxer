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

import javafx.scene.image.Image;

/**
 * Creature is a living entity which can move around the world and interact with it.
 *
 * @author Michal-jr
 * @version 1.0
 */
public abstract class Creature {
    private double x, y;
    private double velocityX = 2;
    private double velocityY = 0;
    private double velocityMultiplier = 1;
    private final double GRAVITY = 0.3;
    private boolean onGround, attacking = false;
    private boolean left, right, up, down, running, swimming, leftAttack, rightAttack;
    private double width = 32;
    private double height = 52;
    private final double IMAGE_WIDTH = 64;
    private final double IMAGE_HEIGHT = 64;
    private double spriteX, spriteY, spriteFrame, frameLimit, attackAnimationLimit = 0;
    private final Image image;
    private final CreatureType type;
    private int hp;
    private boolean killed = false;

    /**
     * Create new creature on given coordinates.
     *
     * @param x
     * @param y
     * @param type
     * @since 1.0
     */
    public Creature(double x, double y, CreatureType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.image = type.getSpritesheet();
        this.hp = type.getHp();
    }
    
    /**
     * Update creature's position in the world and animate its movement.
     * 
     * @since 1.0
     */
    public void move() {
        frameLimit++;
        if (running) { velocityX = 4*velocityMultiplier; frameLimit = 0; } else { velocityX = 2*velocityMultiplier; }
        if (swimming) { velocityX = 1*velocityMultiplier; }
        if (left) {
            x -= velocityX;
            spriteY = 9 * IMAGE_HEIGHT;
            if (frameLimit % 2 == 0) { spriteFrame--; }
            if (spriteFrame < 0) { spriteFrame = 8; }
            if (!onGround) { spriteFrame = 1; }
            spriteX = spriteFrame * IMAGE_WIDTH;
        }
        if (right) {
            x += velocityX;
            spriteY = 11 * IMAGE_HEIGHT;
            if (frameLimit % 2 == 0) { spriteFrame++; }
            if (spriteFrame > 8) { spriteFrame = 0; }
            if (!onGround) { spriteFrame = 1; }
            spriteX = spriteFrame * IMAGE_WIDTH;
        }
        if (!left && !right) {
            spriteX = IMAGE_WIDTH;
            spriteY = 10 * IMAGE_HEIGHT;
            if (!onGround) {
                spriteX = 6 * IMAGE_WIDTH;
                spriteY = 2 * IMAGE_HEIGHT;
            }
        }
        if (!swimming) {
            if (up) { jump(); }
            fall();
        } else {
            swim();
        }
        if (frameLimit < 1) { frameLimit = 0; }
    }
    
    private void fall() {
        onGround = false;
        velocityY += GRAVITY*velocityMultiplier;
        y += velocityY;
    }
    
    private void jump() {
        if (onGround) {
            velocityY = -6.0*velocityMultiplier;
            onGround = false;
        }
    }
    
    private void swim() {
        if (up) { y -= velocityX; }
        if (down) { y += velocityX; }
        if (!up && !down) { y += GRAVITY/1.5; }
        onGround = false;
    }
    
    public void animateAttack() {
        spriteX = attackAnimationLimit*IMAGE_WIDTH;
        if (leftAttack) { spriteY = 13*IMAGE_HEIGHT; }
        else if (rightAttack) { spriteY = 15*IMAGE_HEIGHT; }
        if (attackAnimationLimit < 5) {
            attackAnimationLimit++;
        } else {
            attackAnimationLimit = 0;
            attacking = false;
        }
    }
    
    public void revive() {
        hp = type.getHp();
        killed = false;
    }
    
    /**
     * Set if creature is moving left.
     * 
     * @param left
     * @since 1.0
     */
    public void setLeft(boolean left) {
        this.left = left;
    }

    /**
     * Set if creature is moving right.
     * 
     * @param right
     * @since 1.0
     */
    public void setRight(boolean right) {
        this.right = right;
    }

    /**
     * Set if creature is moving up.
     * 
     * @param up
     * @since 1.0
     */
    public void setUp(boolean up) {
        this.up = up;
    }
    
    /**
     * Set if creature is moving down.
     * 
     * @param down
     * @since 1.0
     */
    public void setDown(boolean down) {
        this.down = down;
    }

    /**
     * Set if creature is running.
     *
     * @param running
     * @since 1.0
     */
    public void run(boolean running) {
        this.running = running;
    }
    
    /**
     * Set if creature is swimming.
     *
     * @param swimming
     * @since 1.0
     */
    public void setSwimming(boolean swimming) {
        this.swimming = swimming;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setLeftAttack(boolean leftAttack) {
        this.leftAttack = leftAttack;
    }

    public void setRightAttack(boolean rightAttack) {
        this.rightAttack = rightAttack;
    }
    
    /**
     * @return true if creature is moving left, false otherwise
     * @since 1.0
     */
    public boolean movingLeft() {
        return left;
    }
    
    /**
     * @return true if creature is moving right, false otherwise
     * @since 1.0
     */
    public boolean movingRight() {
        return right;
    }
    
    /**
     * @return true if creature is jumping, false otherwise
     * @since 1.0
     */
    public boolean jumping() {
        return velocityY < 0;
    }
    
    /**
     * @return true if creature is falling, false otherwise
     * @since 1.0
     */
    public boolean falling() {
        return velocityY > 0;
    }

    public boolean isKilled() {
        return killed;
    }

    public boolean isAttacking() {
        return attacking;
    }

    /**
     * @return creature's X velocity
     * @since 1.0
     */
    public double getVelocityX() {
        return velocityX;
    }
    
    /**
     * @return creature's X velocity
     * @since 1.0
     */
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * @return creature's health point
     * @since 1.0
     */
    public int getHp() {
        return hp;
    }

    /**
     * @return creature's X position in pixels
     * @since 1.0
     */
    public double getX() {
        return x;
    }
    
    /**
     * @return creature's X2 position in pixels
     * @since 1.0
     */
    public double getX2() {
        return x+width;
    }

    /**
     * @return creature's Y position in pixels
     * @since 1.0
     */
    public double getY() {
        return y;
    }
    
    /**
     * @return creature's Y2 position in pixels
     * @since 1.0
     */
    public double getY2() {
        return y+height;
    }

    /**
     * @return creature's width in pixels
     * @since 1.0
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return creature's height in pixels
     * @since 1.0
     */
    public double getHeight() {
        return height;
    }

    /**
     * @return creature's actual animation frame width in pixels
     * @since 1.0
     */
    public double getIMAGE_WIDTH() {
        return IMAGE_WIDTH-32;
    }

    /**
     * @return creature's actual animation frame height in pixels
     * @since 1.0
     */
    public double getIMAGE_HEIGHT() {
        return IMAGE_HEIGHT-12;
    }

    /**
     * @return creature's spritesheet
     * @since 1.0
     */
    public Image getImage() {
        return image;
    }

    /**
     * @return X position of actual animation frame in spritesheet
     * @since 1.0
     */
    public double getSpriteX() {
        return spriteX+16;
    }

    /**
     * @return Y position of actual animation frame in spritesheet
     * @since 1.0
     */
    public double getSpriteY() {
        return spriteY+12;
    }

    /**
     * Set creature's X position in pixels.
     *
     * @param x
     * @since 1.0
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Set creature's Y position in pixels.
     *
     * @param y
     * @since 1.0
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Set creature's width in pixels.
     *
     * @param width
     * @since 1.0
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Set creature's height in pixels.
     *
     * @param height
     * @since 1.0
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Set creature's vertical velocity.
     * 
     * @param velocityY
     * @since 1.0
     */
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    /**
     * Set creature's velocity multiplier.
     *
     * @param velocityMultiplier
     * @since 1.0
     */
    public void setVelocityMultiplier(double velocityMultiplier) {
        this.velocityMultiplier = velocityMultiplier;
    }

    /**
     * Set if creature is standing on the ground.
     * 
     * @param onGround
     * @since 1.0
     */
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
    
    public void die() {
        killed = true;
    }
    
}
