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
    
    private int x, y;
    private float velocityX = 2.0f;
    private float velocityY = 0.0f;
    private final float GRAVITY = 0.3f;
    private boolean onGround = false;
    private boolean left, right, up, running;
    private final int WIDTH = 32;
    private final int HEIGHT = 64;
    private final double IMAGE_WIDTH = 64;
    private final double IMAGE_HEIGHT = 64;
    private double spriteX, spriteY, spriteFrame = 0;
    private final Image image = new Image( "sprite_sheet.png" );
    private final Inventory inventory = new Inventory();
    
    /**
     * Update player's state.
     * 
     * @since 1.0
     */
    public void update() {
        move();
        Collision.preventCollision(this);
        Collision.controlItems(this);
    }
    
    /**
     * Update player's position in the world.
     * 
     * @since 1.0
     */
    public void move() {
        if (running) { velocityX = 4.0f; } else { velocityX = 2.0f; }
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
        }
        if (up) { jump(); }
        fall();
    }
    
    private void fall() {
        velocityY += GRAVITY;
        y += velocityY;
    }
    private void jump() {
        if (onGround) {
            velocityY = -6.0f;
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
     * @return player's X velocity
     * @since 1.0
     */
    public float getVelocityX() {
        return velocityX;
    }

    /**
     * @return player's X position in pixels
     * @since 1.0
     */
    public int getX() {
        return x;
    }
    
    /**
     * @return player's X2 position in pixels
     * @since 1.0
     */
    public int getX2() {
        return x+WIDTH;
    }

    /**
     * @return player's Y position in pixels
     * @since 1.0
     */
    public int getY() {
        return y;
    }
    
    /**
     * @return player's Y2 position in pixels
     * @since 1.0
     */
    public int getY2() {
        return y+HEIGHT;
    }

    /**
     * @return player's width in pixels
     * @since 1.0
     */
    public int getWIDTH() {
        return WIDTH;
    }

    /**
     * @return player's height in pixels
     * @since 1.0
     */
    public int getHEIGHT() {
        return HEIGHT;
    }

    public double getIMAGE_WIDTH() {
        return IMAGE_WIDTH-32;
    }

    public double getIMAGE_HEIGHT() {
        return IMAGE_HEIGHT;
    }

    public Image getImage() {
        return image;
    }

    public double getSpriteX() {
        return spriteX+16;
    }

    public double getSpriteY() {
        return spriteY;
    }

    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Set player's X position in pixels.
     *
     * @param x
     * @since 1.0
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set player's Y position in pixels.
     *
     * @param y
     * @since 1.0
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Set player's vertical velocity.
     * 
     * @param velocityY
     * @since 1.0
     */
    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
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
