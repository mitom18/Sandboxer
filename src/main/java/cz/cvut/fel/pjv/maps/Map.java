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

import java.util.ArrayList;
import java.util.List;

/**
 * Class Map generates a map. The map defines how a game world is going to look.
 * The constructor takes 2 integer arguments (width and height of the map) or
 * if no arguments are given width and height are set to default values.
 *
 * @author Zdeněk
 * @version 1.0
 */
public class Map {

    private final int WIDTH;
    private final int HEIGHT;

    private List<List<Integer>> terrain;
    private List<List<BlockType>> map;

    /**
     * Create new map with default size.
     * 
     * @since 1.0
     */
    public Map() {
        WIDTH = 1024;
        HEIGHT = 64;
        generateTerrain();
    }

    /**
     * Create new map with given size.
     *
     * @param WIDTH width of the map in blocks
     * @param HEIGHT height of the map in blocks
     * @since 1.0
     */
    public Map(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        generateTerrain();
    }

    /**
     * @return width of the map in blocks
     * @since 1.0
     */
    public int getWIDTH() {
        return WIDTH;
    }

    /**
     * @return height of the map in blocks
     * @since 1.0
     */
    public int getHEIGHT() {
        return HEIGHT;
    }

    /**
     * @return list of 1 and 0 (1 for block, 0 for empty space)
     * @since 1.0
     */
    public List<List<Integer>> getTerrain() {
        return terrain;
    }

    /**
     * @return list of block types
     * @since 1.0
     */
    public List<List<BlockType>> getMap() {
        return map;
    }
    
    /**
     * Contains a periodical mathematical function.
     * 
     * @param x the variable x
     * @return the functional value
     * @since 1.0
     */
    private double calculateSkyline(double x) {
        double y = 8 * Math.sin((1.0 / 8.0) * x) + 16;
        return y;
    }
    
    /**
     * Fills the 2D ArrayList terrain with values that represent terrain (1)
     * and void (0) using a mathematical function.
     * 
     * @since 1.0
     */
    private void generateTerrain() {
        terrain = new ArrayList<>(WIDTH);

        for (int i = 0; i < WIDTH; i++) {
            terrain.add(new ArrayList<Integer>(HEIGHT));

            double x = i;
            double skyline = calculateSkyline(x);

            for (int j = 0; j < HEIGHT; j++) {
                if (j >= skyline) {
                    terrain.get(i).add(1);
                } else {
                    terrain.get(i).add(0);
                }
            }
        }
    }
}
