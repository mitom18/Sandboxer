/*
 * The MIT License
 *
 * Copyright 2019 Zdeněk.
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
package cz.cvut.fel.pjv.maps;

import cz.cvut.fel.pjv.blocks.BlockType;
import java.io.Serializable;

/**
 * A vector in the map has 2 coordinates (x and y) and carries the information 
 * about the type of block that will be on these coordinates.
 * 
 * @author Zdenek
 * @version 1.0
 */
public class Vector implements Serializable {
    
    private int x;
    private int y;
    private BlockType blockType;

    /**
     * @param x
     * @param y
     * @param blockType
     * @since 1.0
     */
    public Vector(int x, int y, BlockType blockType) {
        this.x = x;
        this.y = y;
        this.blockType = blockType;
    }

    /**
     * @return x coordinate of the vector
     * @since 1.0
     */
    public int getX() {
        return x;
    }

    /**
     * @return y coordinate of the vector
     * @since 1.0
     */
    public int getY() {
        return y;
    }
    
    /**
     * @param x
     * @since 1.0
     */
    public void setX(int x) {
        this.x = x;
    }
    
    /**
     * @return type of block of this vector
     * @since 1.0
     */
    public BlockType getBlockType() {
        return blockType;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.x;
        hash = 67 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vector other = (Vector) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
    
    
}
