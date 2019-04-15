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

import cz.cvut.fel.pjv.BlockType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class Map generates a map. The map defines how a game world is going to look.
 * The constructor takes 2 integer arguments (width and height of the map) or
 * if no arguments are given width and height are set to default values.
 *
 * @author Zdenek
 * @version 1.1
 */
public class Map {

    private final int WIDTH;
    private final int HEIGHT;
    
    private final double AMPLITUDE_COEFFICIENT_MULTIPLICATOR = 8;
    private final double PERIOD_COEFFICIENT_MULTIPLICATOR = 12;
    
    private final double AMP_MIN = 0.5;
    private final double AMP_MAX = 1.5;
    private final double PER_MIN = 0.5;
    private final double PER_MAX = 1.5;

    private List<List<Integer>> terrain;
    private List<List<BlockType>> map;
    
    private final long seed;
    private Random r;

    /**
     * Create new map with default size.
     * 
     * @since 1.0
     */
    public Map() {
        WIDTH = 1024;
        HEIGHT = 64;
        
        r = new Random();
        seed = r.nextLong();
        r.setSeed(seed);
        System.out.println(seed);
        terrain = generateTerrain(0);
        generateMap();
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
        
        r = new Random();
        seed = r.nextLong();
        r.setSeed(seed);
        System.out.println(seed);
        
        generateMap();
    }
    
    /**
     * @return randomly generated seed for this instance of Map
     * @since 1.1
     */
    public long getSeed() {
        return seed;
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
     * @return list of 1 and 0 (1 for any block, 0 for empty space)
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
     * Generates a random double in a given range.
     * 
     * @param min smallest possible generated number
     * @param max largest possible generated number
     * @return random double in the given range
     * @since 1.1
     */
    private double randomDoubleInRange(double min, double max) {
        return min + (max - min) * r.nextDouble();
    }
    
    /**
     * Contains a periodical mathematical function.
     * 
     * @param x the variable x
     * @return the functional value
     * @since 1.0
     */
    private double calculateSkyline(double x, double amplitudeCoefficient, double periodCoefficient, double previousY, int amp) {
        double y = amplitudeCoefficient * Math.sin((periodCoefficient) * x) + previousY + amp;
        return y;
    }
    
    /**
     * Fills the 2D ArrayList terrain with values that represent terrain (1)
     * and void (0) using a mathematical function.
     * Uses random numbers to calculate the amplitude and period of a sine function.
     * The function is calculated again with new values after each period.
     * 
     * @since 1.0
     */
    private List<List<Integer>> generateTerrain(int amp) {
        List<List<Integer>> terrain = new ArrayList<>(WIDTH);
        
        double amplitudeCoefficient = randomDoubleInRange(AMP_MIN, AMP_MAX) * AMPLITUDE_COEFFICIENT_MULTIPLICATOR;
        double periodCoefficient = 1 / (randomDoubleInRange(PER_MIN, PER_MAX) * PERIOD_COEFFICIENT_MULTIPLICATOR);
        
        double period = (2 * Math.PI) / (periodCoefficient);
        double previousY = 32;
        
        int counter = 1;

        for (int i = 0; i < WIDTH; i++) {
            terrain.add(new ArrayList<Integer>(HEIGHT));
            
            double skyline = calculateSkyline((double) i, amplitudeCoefficient, periodCoefficient, previousY, amp);
            
            if ((int)period == counter) {
                amplitudeCoefficient = randomDoubleInRange(AMP_MIN, AMP_MAX) * AMPLITUDE_COEFFICIENT_MULTIPLICATOR;
                periodCoefficient = 1 / (randomDoubleInRange(PER_MIN, PER_MAX) * PERIOD_COEFFICIENT_MULTIPLICATOR);
                
                period = (2 * Math.PI) / (periodCoefficient);
                double nextSkyline = calculateSkyline((double) i + 1, amplitudeCoefficient, periodCoefficient, previousY, amp);
                previousY += skyline - nextSkyline;
                
                counter = 1;
            }

            for (int j = HEIGHT - 1; j >= 0; j--) {
                
                if ((j == 0) || (j == HEIGHT - 1)) {
                    terrain.get(i).add(1);
                } else if (j <= skyline) {
                    terrain.get(i).add(1);
                } else {
                    terrain.get(i).add(0);
                }
            }
            
            counter++;
        }
        
        return terrain;
    }
    
    private void generateMap(){
        List<List<Integer>> terrain1 = generateTerrain(0);
        List<List<Integer>> terrain2 = generateTerrain(-3);
        map = new ArrayList<>(WIDTH);
        
        for (int i = 0; i < WIDTH; i++) {
            map.add(new ArrayList<BlockType>(HEIGHT));
            
            for (int j = 0; j < HEIGHT; j++) {
                if ((terrain1.get(i).get(j) == 1) && (terrain2.get(i).get(j) == 1)) {
                    map.get(i).add(BlockType.STONE);
                } else if (terrain1.get(i).get(j) == 1) {
                    map.get(i).add(BlockType.DIRT);
                } else {
                    map.get(i).add(null);
                }
            }
        }
    }
}
