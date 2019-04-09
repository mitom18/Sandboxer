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

import javafx.scene.image.Image;


/**
 * Player class.
 * 
 * @author Michal-jr
 * @version 1.0
 */
public class Player {
    
    private double x, y;
    private double velocityX = 2;
    private double velocityY = 0;
    private double velocityMultiplier = 1;
    private final double GRAVITY = 0.3;
    private boolean onGround = false;
    private boolean left, right, up, running;
    private double width = 32;
    private double height = 52;
    private final double IMAGE_WIDTH = 64;
    private final double IMAGE_HEIGHT = 64;
    private double spriteX, spriteY, spriteFrame = 0;
    private final Image image = new Image( "spritesheet_hero.png" );
    private final Inventory inventory = new Inventory();

    /**
     * Create player on given coordinates.
     *
     * @param x
     * @param y
     * @since 1.0
     */
    public Player(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Update player's state.
     * 
     * @param world instance of the world
     * @since 1.0
     */
    public void update(World world) {
        move();
        Collision.preventCollision(this, world);
        Collision.controlItems(this, world);
    }
    
    /**
     * Update player's position in the world and animate his movement.
     * 
     * @since 1.0
     */
    public void move() {
        if (running) { velocityX = 4*velocityMultiplier; } else { velocityX = 2*velocityMultiplier; }
        if (left) {
            x -= velocityX;
            spriteY = 9 * IMAGE_HEIGHT;
            spriteFrame--;
            if (spriteFrame < 0) { spriteFrame = 8; }
            if (!onGround) { spriteFrame = 1; }
            spriteX = spriteFrame * IMAGE_WIDTH;
        }
        if (right) {
            x += velocityX;
            spriteY = 11 * IMAGE_HEIGHT;
            spriteFrame++;
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
        if (up) { jump(); }
        fall();
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

    /**
     * Set if player is moving to the left.
     * 
     * @param left
     * @since 1.0
     */
    public void setLeft(boolean left) {
        this.left = left;
    }

    /**
     * Set if player is moving to the right.
     * 
     * @param right
     * @since 1.0
     */
    public void setRight(boolean right) {
        this.right = right;
    }

    /**
     * Set if player is jumping.
     * 
     * @param up
     * @since 1.0
     */
    public void setUp(boolean up) {
        this.up = up;
    }

    /**
     * Set if player is running.
     *
     * @param running
     * @since 1.0
     */
    public void run(boolean running) {
        this.running = running;
    }
    
    public void changeActiveItem(int moveBy) {
        int moveTo = (inventory.getActiveItemIndex() + moveBy) % 10;
        if (moveTo < 0) { moveTo = 9; }
        inventory.setActiveItemIndex(moveTo);
    }
    
    /**
     * @return true if player is moving left, false otherwise
     * @since 1.0
     */
    public boolean movingLeft() {
        return left;
    }
    
    /**
     * @return true if player is moving right, false otherwise
     * @since 1.0
     */
    public boolean movingRight() {
        return right;
    }
    
    /**
     * @return true if player is jumping, false otherwise
     * @since 1.0
     */
    public boolean jumping() {
        return velocityY < 0;
    }
    
    /**
     * @return true if player is falling, false otherwise
     * @since 1.0
     */
    public boolean falling() {
        return velocityY > 0;
    }

    /**
     * @return player's X velocity
     * @since 1.0
     */
    public double getVelocityX() {
        return velocityX;
    }
    
    /**
     * @return player's X velocity
     * @since 1.0
     */
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * @return player's X position in pixels
     * @since 1.0
     */
    public double getX() {
        return x;
    }
    
    /**
     * @return player's X2 position in pixels
     * @since 1.0
     */
    public double getX2() {
        return x+width;
    }

    /**
     * @return player's Y position in pixels
     * @since 1.0
     */
    public double getY() {
        return y;
    }
    
    /**
     * @return player's Y2 position in pixels
     * @since 1.0
     */
    public double getY2() {
        return y+height;
    }

    /**
     * @return player's width in pixels
     * @since 1.0
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return player's height in pixels
     * @since 1.0
     */
    public double getHeight() {
        return height;
    }

    /**
     * @return player actual animation frame width in pixels
     * @since 1.0
     */
    public double getIMAGE_WIDTH() {
        return IMAGE_WIDTH-32;
    }

    /**
     * @return player actual animation frame height in pixels
     * @since 1.0
     */
    public double getIMAGE_HEIGHT() {
        return IMAGE_HEIGHT-12;
    }

    /**
     * @return player's sprite sheet
     * @since 1.0
     */
    public Image getImage() {
        return image;
    }

    /**
     * @return X position of actual animation frame in sprite sheet
     * @since 1.0
     */
    public double getSpriteX() {
        return spriteX+16;
    }

    /**
     * @return Y position of actual animation frame in sprite sheet
     * @since 1.0
     */
    public double getSpriteY() {
        return spriteY+12;
    }

    /**
     * @return player's inventory
     * @since 1.0
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Set player's X position in pixels.
     *
     * @param x
     * @since 1.0
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Set player's Y position in pixels.
     *
     * @param y
     * @since 1.0
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Set player's width in pixels.
     *
     * @param width
     * @since 1.0
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Set player's height in pixels.
     *
     * @param height
     * @since 1.0
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Set player's vertical velocity.
     * 
     * @param velocityY
     * @since 1.0
     */
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    /**
     * Set player's velocity multiplier.
     *
     * @param velocityMultiplier
     * @since 1.0
     */
    public void setVelocityMultiplier(double velocityMultiplier) {
        this.velocityMultiplier = velocityMultiplier;
    }

    /**
     * Set if player is standing on the ground.
     * 
     * @param onGround
     * @since 1.0
     */
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
    
}
