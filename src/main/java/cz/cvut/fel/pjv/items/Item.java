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
package cz.cvut.fel.pjv.items;

import javafx.scene.image.Image;

/**
 * Item class. Item lays in the world or is in the player's inventory.
 *
 * @author Michal-jr
 * @version 1.0
 */
public abstract class Item {
    private double x, y;
    private double width = 24;
    private double height = 24;
    private boolean picked;
    private final Image IMAGE;
    private final double IMAGE_X;
    private final double IMAGE_Y;
    private final double IMAGE_WIDTH = 128;
    private final double IMAGE_HEIGHT = 128;

    /**
     * Create new item on given coordinates.
     *
     * @param x
     * @param y
     * @param type
     * @param image spritesheet of items
     * @since 1.0
     */
    public Item(double x, double y, ItemType type, Image image) {
        this.x = x+2;
        this.y = y+2;
        this.IMAGE = image;
        IMAGE_X = type.getSpritesheetX();
        IMAGE_Y = type.getSpritesheetY();
        picked = false;
    }

    /**
     * @return item's X position in pixels
     * @since 1.0
     */
    public double getX() {
        return x;
    }

    /**
     * @return item's Y position in pixels
     * @since 1.0
     */
    public double getY() {
        return y;
    }

    /**
     * @return item's width in pixels
     * @since 1.0
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return item's height in pixels
     * @since 1.0
     */
    public double getHeight() {
        return height;
    }
    
    /**
     * @return true if item was picked by player, false otherwise
     * @since 1.0
     */
    public boolean isPicked() {
        return picked;
    }

    /**
     * Set item's X position in pixels.
     *
     * @param x
     * @since 1.0
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Set item's Y position in pixels.
     *
     * @param y
     * @since 1.0
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Set item's width in pixels.
     *
     * @param width
     * @since 1.0
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Set item's height in pixels.
     *
     * @param height
     * @since 1.0
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Set if item was picked by player.
     *
     * @param picked
     * @since 1.0
     */
    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    /**
     * @return spritesheet for items
     * @since 1.0
     */
    public Image getImage() {
        return IMAGE;
    }

    /**
     * @return X position of item sprite in the spritesheet in pixels
     * @since 1.0
     */
    public double getIMAGE_X() {
        return IMAGE_X;
    }

    /**
     * @return Y position of item sprite in the spritesheet in pixels
     * @since 1.0
     */
    public double getIMAGE_Y() {
        return IMAGE_Y;
    }

    /**
     * @return width of item sprite in the spritesheet in pixels
     * @since 1.0
     */
    public double getIMAGE_WIDTH() {
        return IMAGE_WIDTH;
    }

    /**
     * @return height of item sprite in the spritesheet in pixels
     * @since 1.0
     */
    public double getIMAGE_HEIGHT() {
        return IMAGE_HEIGHT;
    }
    
}
