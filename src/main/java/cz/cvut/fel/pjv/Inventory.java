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

import java.util.Arrays;

/**
 * Player's inventory.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class Inventory {
    Item[] inv = new Item [64];
    int activeItemIndex = 0;

    public Inventory() {
        add(new Item(0,0));
    }
    
    /**
     * Add item to inventory.
     *
     * @param itemToAdd
     * @since 1.0
     */
    public void add(Item itemToAdd) {
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] == null) {
                inv[i] = itemToAdd;
                break;
            }
        }
    }

    /**
     * @return inventory items
     * @since 1.0
     */
    public Item[] getInv() {
        return inv;
    }
    
    public Item[] getHotbarItems() {
        return Arrays.copyOfRange(inv, 0, 10);
    }

    /**
     * @return index of active item
     * @since 1.0
     */
    public int getActiveItemIndex() {
        return activeItemIndex;
    }

    public void setActiveItemIndex(int activeItemIndex) {
        this.activeItemIndex = activeItemIndex;
    }
    
}
