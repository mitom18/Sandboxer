/*
 * The MIT License
 *
 * Copyright 2019 Zdenek.
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
    
    /**
     * Represents the probability in percent that DIAMOND_ORE will spawn at any 
     * given place under height 10 in the terrain.
     * Allowed values: 0 - 100
     */
    private final int DIAMOND_PROBABILITY = 2;

    private List<List<Integer>> terrain;
    private List<List<BlockType>> map;
    
    private List<Integer> completeSkyline;
    
    private final long seed;
    private final Random r;

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
        r.setSeed(7451260251423394044L); // This is a nice seed.
        System.out.println(seed);
        
        generateTerrain();
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
     * Generates a random integer in a given range.
     * 
     * @param min smallest possible generated number
     * @param max largest possible generated number
     * @return random int in the given range
     * @since 1.1
     */
    private int randomIntInRange(int min, int max) {
        return min + r.nextInt((max - min) + 1);
    }
    
    /**
     * Contains a periodical mathematical function.
     * 
     * @param x the variable x
     * @return the functional value
     * @since 1.0
     */
    private double calculateSkyline(double x, double amplitudeCoefficient, double periodCoefficient, double previousY) {
        double y = amplitudeCoefficient * Math.sin((periodCoefficient) * x) + previousY;
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
    private void generateTerrain() {
        terrain = new ArrayList<>(WIDTH);
        completeSkyline = new ArrayList<>(WIDTH);
        
        double amplitudeCoefficient = randomDoubleInRange(AMP_MIN, AMP_MAX) * AMPLITUDE_COEFFICIENT_MULTIPLICATOR;
        double periodCoefficient = 1 / (randomDoubleInRange(PER_MIN, PER_MAX) * PERIOD_COEFFICIENT_MULTIPLICATOR);
        
        double period = (2 * Math.PI) / (periodCoefficient);
        double previousY = 32;
        
        int counter = 1;

        for (int i = 0; i < WIDTH; i++) {
            terrain.add(new ArrayList<Integer>(HEIGHT));
            
            double skyline = calculateSkyline((double) i, amplitudeCoefficient, periodCoefficient, previousY);
            completeSkyline.add((int) skyline);
            
            if ((int)period == counter) {
                amplitudeCoefficient = randomDoubleInRange(AMP_MIN, AMP_MAX) * AMPLITUDE_COEFFICIENT_MULTIPLICATOR;
                periodCoefficient = 1 / (randomDoubleInRange(PER_MIN, PER_MAX) * PERIOD_COEFFICIENT_MULTIPLICATOR);
                
                period = (2 * Math.PI) / (periodCoefficient);
                double nextSkyline = calculateSkyline((double) i + 1, amplitudeCoefficient, periodCoefficient, previousY);
                previousY += skyline - nextSkyline;
                
                counter = 1;
            }

            for (int j = 0; j < HEIGHT; j++) {
                
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
    }
    
    /**
     * Fills the 2D ArrayList map with values that represent the actual block, 
     * that will be generated in the World.
     * Uses random numbers and probability to decide the specific blocks that 
     * make up the terrain.
     * 
     * @since 1.1
     */
    private void generateMap(){
        map = new ArrayList<>(WIDTH);
        
        /**
         * Used to define the border between STONE and DIRT.
         */
        int skylineModifier = 0;
        
        int dirtStoneBorder;
        
        for (int i = 0; i < WIDTH; i++) {
            map.add(new ArrayList<BlockType>(HEIGHT));
            
            if (skylineModifier < - 2) {
                skylineModifier += randomIntInRange(0, 2);
            } else if (skylineModifier > 6) {
                skylineModifier += randomIntInRange(-2, 1);
            } else {
                skylineModifier += randomIntInRange(-1, 1);
            }
            
            dirtStoneBorder = completeSkyline.get(i) - skylineModifier - 1;
            
            for (int j = HEIGHT - 1; j >= 0; j--) {
                
                int isDiamond = randomIntInRange(1, 100 / DIAMOND_PROBABILITY);
                
                if (terrain.get(i).get(j) == 1) {
                    
                    if ((j == 0) || (j == HEIGHT - 1)) {
                        map.get(i).add(BlockType.BEDROCK);
                    } else if (j >= dirtStoneBorder) {
                        map.get(i).add(BlockType.DIRT);
                    } else if ((j <= 10) && (isDiamond == 1)) {
                        map.get(i).add(BlockType.DIAMOND_ORE);
                    } else {
                        map.get(i).add(BlockType.STONE);
                    }
                } else if (j <= 24) {
                    map.get(i).add(BlockType.WATER);
                } else {
                    map.get(i).add(null);
                }
            }
        }
    }
}
