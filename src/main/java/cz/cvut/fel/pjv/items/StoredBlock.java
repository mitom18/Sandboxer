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

import cz.cvut.fel.pjv.blocks.Block;
import cz.cvut.fel.pjv.blocks.BlockType;
import cz.cvut.fel.pjv.blocks.SolidBlock;
import java.util.Objects;
import javafx.scene.image.Image;

/**
 * Class for stored block which can be placed in the world as blocks, extends item class.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class StoredBlock extends Item implements Placeable {
    
    private static final Image IMAGE = new Image( "spritesheet_tiles.png" );
    private int quantity = 1;
    private final BlockType blockType;

    /**
     * Create new item on given coordinates.
     *
     * @param x
     * @param y
     * @param type
     * @since 1.0
     */
    public StoredBlock(double x, double y, ItemType type) {
        super(x, y, type, IMAGE);
        blockType = BlockType.valueOf(type.name());
    }
    
    /**
     * Add 1 to the quantity of the item. Used for stacking.
     * 
     * @since 1.0
     */
    public void raiseQuantity() {
        quantity++;
    }
    
    /**
     * Subtract 1 from the quantity of the item. Used for stacking.
     * 
     * @since 1.0
     */
    public void decreaseQuantity() {
        quantity--;
    }

    /**
     * @return quantity of the item
     * @since 1.0
     */
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public Block place(double x, double y) {
        return new SolidBlock(x, y, blockType);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.blockType);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StoredBlock other = (StoredBlock) obj;
        if (this.blockType != other.blockType) {
            return false;
        }
        return true;
    }
    
}
