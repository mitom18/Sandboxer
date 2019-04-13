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
 * Block class. Block is basic part of the world.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class Block {
 
    /**
     * Actual width of the block in pixels.
     */
    public static double block_width = 32;

    /**
     * Actual height of the block in pixels.
     */
    public static double block_height = 32;
    
    private double x, y;
    private double width = block_width;
    private double height = block_height;
    private static final Image image = new Image( "spritesheet_tiles.png" );
    private final double IMAGE_X;
    private final double IMAGE_Y;
    private final double IMAGE_WIDTH = 128;
    private final double IMAGE_HEIGHT = 128;
    private final BlockType blockType;
    private boolean destroyed = false;

    /**
     * Create new block on given coordinates.
     *
     * @param x
     * @param y
     * @param type
     * @since 1.0
     */
    public Block(double x, double y, BlockType type) {
        this.x = x;
        this.y = y;
        this.IMAGE_X = type.getSpritesheetX();
        this.IMAGE_Y = type.getSpritesheetY();
        blockType = type;
    }

    /**
     * @return block's X position in pixels
     * @since 1.0
     */
    public double getX() {
        return x;
    }
    
    /**
     * @return block's X2 position in pixels
     * @since 1.0
     */
    public double getX2() {
        return x+width;
    }

    /**
     * @return block's Y position in pixels
     * @since 1.0
     */
    public double getY() {
        return y;
    }
    
    /**
     * @return block's Y2 position in pixels
     * @since 1.0
     */
    public double getY2() {
        return y+height;
    }

    /**
     * @return block's width in pixels
     * @since 1.0
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return block's height in pixels
     * @since 1.0
     */
    public double getHeight() {
        return height;
    }
    
    /**
     * Set block's X position in pixels.
     * 
     * @param x
     * @since 1.0
     */
    public void setX(double x) {
        this.x = x;
    }
    
    /**
     * Set block's Y position in pixels.
     * 
     * @param y
     * @since 1.0
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Set block's width in pixels.
     *
     * @param width
     * @since 1.0
     */
    public void setWidth(double width) {
        this.width = width;
        block_width = width;
    }

    /**
     * Set block's height in pixels.
     *
     * @param height
     * @since 1.0
     */
    public void setHeight(double height) {
        this.height = height;
        block_height = height;
    }

    /**
     * Destroy block.
     * 
     * @since 1.0
     */
    public void destroy() {
        this.destroyed = true;
    }

    /**
     * @return true if block is destroyed, false otherwise
     * @since 1.0
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * @return type of the block
     * @since 1.0
     */
    public BlockType getBlockType() {
        return blockType;
    }

    /**
     * @return spritesheet for blocks
     * @since 1.0
     */
    public static Image getImage() {
        return image;
    }

    /**
     * @return X position of block sprite in the spritesheet in pixels
     * @since 1.0
     */
    public double getIMAGE_X() {
        return IMAGE_X;
    }

    /**
     * @return Y position of block sprite in the spritesheet in pixels
     * @since 1.0
     */
    public double getIMAGE_Y() {
        return IMAGE_Y;
    }

    /**
     * @return width of block sprite in the spritesheet in pixels
     * @since 1.0
     */
    public double getIMAGE_WIDTH() {
        return IMAGE_WIDTH;
    }

    /**
     * @return height of block sprite in the spritesheet in pixels
     * @since 1.0
     */
    public double getIMAGE_HEIGHT() {
        return IMAGE_HEIGHT;
    }

}
