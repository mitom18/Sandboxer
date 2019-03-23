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

import javafx.scene.paint.Color;


/**
 * Block class. Block is basic part of the world.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class Block {
 
    private int x, y;
    private final int WIDTH = 15;
    private final int HEIGHT = 15;
    private final Color color = Color.RED;

    /**
     * Create new block on given coordinates.
     *
     * @param x
     * @param y
     * @since 1.0
     */
    public Block(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return block's X position in pixels
     * @since 1.0
     */
    public int getX() {
        return x;
    }
    
    /**
     * @return block's X2 position in pixels
     * @since 1.0
     */
    public int getX2() {
        return x+WIDTH;
    }

    /**
     * @return block's Y position in pixels
     * @since 1.0
     */
    public int getY() {
        return y;
    }
    
    /**
     * @return block's Y2 position in pixels
     * @since 1.0
     */
    public int getY2() {
        return y+HEIGHT;
    }

    /**
     * @return block's width in pixels
     * @since 1.0
     */
    public int getWIDTH() {
        return WIDTH;
    }

    /**
     * @return block's height in pixels
     * @since 1.0
     */
    public int getHEIGHT() {
        return HEIGHT;
    }

    /**
     * @return block's color
     * @since 1.0
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Set block's position in pixels.
     * 
     * @param x
     * @since 1.0
     */
    public void setX(int x) {
        this.x = x;
    }
    
}
