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
package cz.cvut.fel.pjv.blocks;

import cz.cvut.fel.pjv.Collision;
import cz.cvut.fel.pjv.World;
import java.util.ArrayList;
import java.util.List;

/**
 * Special type of block. Liquids can expand.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class LiquidBlock extends Block {

    /**
     * Create new liquid block on given coordinates.
     *
     * @param x
     * @param y
     * @param type
     * @since 1.0
     */
    public LiquidBlock(double x, double y, BlockType type) {
        super(x, y, type);
    }
    
    /**
     * Liquid can expand downward if there is no solid block underneath.
     *
     * @param world instance of the world
     * @return list of new liquid blocks
     * @since 1.0
     */
    public List<Block> expand(World world) {
        List<Block> newLiquid = new ArrayList<>();
        boolean blockUnder = false;
        int i = 0;
        double y = getY();
        while (!blockUnder) {
            i++;
            for (Block block : world.getBlocks()) {
                if (block.isDestroyed() || block.getX() != getX()) { continue; }
                if (Collision.collides(getX()+getWidth()/2, y+getHeight()*1.5, block)) {
                    blockUnder = true;
                    break;
                }
            }
            if (!blockUnder) { newLiquid.add(new LiquidBlock(getX(), getY()+getHeight()*i, BlockType.WATER)); }
            y += getHeight();
        }
        
        return newLiquid;
    }
    
}
