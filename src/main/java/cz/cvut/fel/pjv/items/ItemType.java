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

/**
 * Enumeration class representing all types of items that can appear in the game.
 * 
 * @author Michal-jr
 * @version 1.0
 */
public enum ItemType {
    
    /**
     * Type of pickaxe.
     */
    PICKAXE(0, 0, 1170),
     
    /**
     * Type of stored dirt block.
     */
    DIRT(1, 650, 130),

    /**
     * Type of stored stone block.
     */
    STONE(2, 260, 650),

    /**
     * Type of stored diamond ore block.
     */
    DIAMOND_ORE(4, 260, 0);

    private final int id;
    private final double spritesheetX;
    private final double spritesheetY;


    private ItemType(int id, double spritesheetX, double spritesheetY) {
        this.id = id;
        this.spritesheetX = spritesheetX;
        this.spritesheetY = spritesheetY;
    }

    /**
     * @return X position of item sprite in the spritesheet in pixels
     * @since 1.0
     */
    public double getSpritesheetX() {
        return spritesheetX;
    }

    /**
     * @return Y position of item sprite in the spritesheet in pixels
     * @since 1.0
     */
    public double getSpritesheetY() {
        return spritesheetY;
    }
    
}
