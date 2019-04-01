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

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing all entities in the world.
 * 
 * @author Michal-jr
 * @version 1.0
 */
public abstract class Instances {
 
    /**
     * Instance of Player.
     */
    public static Player player = new Player(0, 250);

    /**
     * Array of blocks in the world.
     */
    public static List<Block> blocks = generate(50);
    
    /**
     * Item to pick.
     */
    public static Item item = new Item(300, 330);
 
    private static List<Block> generate(int len) {
        List<Block> array = new ArrayList<>();
        int x = -30;
        for (int i = 0; i < len; i++) {
            array.add(new Block(x, 345));
            x += Block.block_width;
        }
        array.add(new Block(210, 330));
        return array;
    }
    
}
