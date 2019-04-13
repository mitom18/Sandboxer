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
 * Class for tools, extends item class.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class Tool extends Item {
    
    private static final Image IMAGE = new Image( "spritesheet_items.png" );
    private final ItemType toolType;

    /**
     * Create new item on given coordinates.
     *
     * @param x
     * @param y
     * @param type
     * @since 1.0
     */
    public Tool(double x, double y, ItemType type) {
        super(x, y, type, IMAGE);
        toolType = type;
    }

    /**
     * @return true if item is pickaxe, false otherwise
     * @since 1.0
     */
    public boolean isPickaxe() {
        return toolType.equals(ItemType.PICKAXE);
    }
    
}
