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

import cz.cvut.fel.pjv.blocks.BlockType;
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
    
    private final double AMPLITUDE_COEFFICIENT_MULTIPLICATOR;
    private final double PERIOD_COEFFICIENT_MULTIPLICATOR;
    
    private final double AMP_MIN;
    private final double AMP_MAX;
    private final double PER_MIN;
    private final double PER_MAX;
    /**
     * Represents the probability in percent.
     * Allowed values: 0.0 - 100.0
     */
    private final double FLAT_LAND_PROBABILITY;
    private final double DIAMOND_PROBABILITY;
    private final double CAVE_PROBABILITY;

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
        HEIGHT = 128;
        
        AMPLITUDE_COEFFICIENT_MULTIPLICATOR = 8;
        PERIOD_COEFFICIENT_MULTIPLICATOR = 12;

        AMP_MIN = 0.5;
        AMP_MAX = 1.5;
        PER_MIN = 0.5;
        PER_MAX = 1.5;

        FLAT_LAND_PROBABILITY = 10;
        DIAMOND_PROBABILITY = 1;
        CAVE_PROBABILITY = 0.05;
        
        r = new Random();
        seed = r.nextLong();
        r.setSeed(seed); // These are nice seeds: 7451260251423394044L -7780041021634934149L
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
        
        AMPLITUDE_COEFFICIENT_MULTIPLICATOR = 8;
        PERIOD_COEFFICIENT_MULTIPLICATOR = 12;

        AMP_MIN = 0.5;
        AMP_MAX = 1.5;
        PER_MIN = 0.5;
        PER_MAX = 1.5;

        FLAT_LAND_PROBABILITY = 10;
        DIAMOND_PROBABILITY = 1;
        CAVE_PROBABILITY = 0.05;
        
        r = new Random();
        seed = r.nextLong();
        r.setSeed(seed);
        System.out.println(seed);
        
        generateTerrain();
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
     * @return list of integers, that specify how high the skyline is
     * @since 1.0
     */
    public List<Integer> getCompleteSkyline() {
        return completeSkyline;
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
    
    private boolean calculateProbability(double probability) {
        
        if (probability <= 0) {
            return false;
        } else if (probability >= 100) {
            return true;
        } else {
            return randomIntInRange(1, (int) (100 / probability)) == 1;
        }
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
        double previousY = HEIGHT / 2;
        
        int counter = 1;

        for (int i = 0; i < WIDTH; i++) {
            terrain.add(new ArrayList<Integer>(HEIGHT));
            
            double skyline = calculateSkyline((double) i, amplitudeCoefficient, periodCoefficient, previousY);
            completeSkyline.add((int) skyline);
            
            if ((int)period == counter) {
                
                if (calculateProbability(FLAT_LAND_PROBABILITY)) {
                    amplitudeCoefficient = randomDoubleInRange(AMP_MIN, AMP_MAX) * 1;
                } else {
                    amplitudeCoefficient = randomDoubleInRange(AMP_MIN, AMP_MAX) * AMPLITUDE_COEFFICIENT_MULTIPLICATOR;
                }
                
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
    private void generateMap() {
        map = new ArrayList<>(WIDTH);
        
        /**
         * Used to define the border between STONE and DIRT.
         */
        int skylineModifier = 0;
        
        int dirtStoneBorder;
        
        for (int i = 0; i < WIDTH; i++) {
            map.add(new ArrayList<BlockType>(HEIGHT));
            
            boolean isUnderWater = false;
            
            if (skylineModifier < - 2) {
                skylineModifier += randomIntInRange(0, 2);
            } else if (skylineModifier > 6) {
                skylineModifier += randomIntInRange(-2, 1);
            } else {
                skylineModifier += randomIntInRange(-1, 1);
            }
            
            dirtStoneBorder = completeSkyline.get(i) - skylineModifier - 1;
            
            for (int j = HEIGHT - 1; j >= 0; j--) {
                
                if (terrain.get(i).get(j) == 1) {
                    
                    if ((j == 0) || (j == HEIGHT - 1)) {
                        // Bedrock
                        map.get(i).add(BlockType.BEDROCK);
                    } else if (j >= dirtStoneBorder) {
                        if ((j == completeSkyline.get(i)) && (!isUnderWater)) {
                            // Grass
                            map.get(i).add(BlockType.DIRT_WITH_GRASS);
                        } else {
                            // Dirt
                            map.get(i).add(BlockType.DIRT);
                        }
                    } else if ((j <= HEIGHT / 8) && calculateProbability(DIAMOND_PROBABILITY)) {
                        // Diamond
                        map.get(i).add(BlockType.DIAMOND_ORE);
                    } else {
                        // Stone
                        map.get(i).add(BlockType.STONE);
                    }
                } else if (j <= HEIGHT / 2 - HEIGHT / 16) {
                    // Water
                    map.get(i).add(BlockType.WATER);
                    isUnderWater = true;
                } else {
                    // Nothing/air
                    map.get(i).add(null);
                }
            }
        }
        
        generateCaves();
    }
    
    private void generateCaves() {
        
        int maxCaveWidth = 30;
        int maxCaveHeight = 14;
        
        for (int i = 0; i < WIDTH; i++) {
            
            for (int j = HEIGHT - 1; j >= 0; j--) {
                
                if ((i < WIDTH - maxCaveWidth) && (j < HEIGHT - maxCaveHeight) && (map.get(i).get(j) == BlockType.STONE) && calculateProbability(CAVE_PROBABILITY)) {
                    List<List<Integer>> listOfVectors = generateVectorCluster(randomIntInRange(10, maxCaveWidth), randomIntInRange(4, maxCaveHeight));
                    
                    for (List<Integer> vector : listOfVectors) {
                        
                        if (map.get(vector.get(0) + i).get(vector.get(1) + j) == BlockType.STONE) {
                            map.get(vector.get(0) + i).set(vector.get(1) + j, null);
                        }
                    }
                }
            }
        }
    }
    
    /*private List<List<Integer>> generateVectorCluster(int clusterWidth, int clusterHeight) {
     * List<List<Integer>> listOfVectors = new ArrayList<>(clusterWidth * clusterHeight);
     * 
     * double horizontalBuildingProb = 0;
     * double verticalBuildingProb;
     * 
     * for (int i = 0; i < clusterWidth; i++) {
     * 
     * boolean building = false;
     * 
     * if (i < (clusterWidth / 2)) {
     * horizontalBuildingProb += 100 / (clusterWidth / 2);
     * } else {
     * if ((horizontalBuildingProb - 100 / (clusterWidth / 2)) != 0) {
     * horizontalBuildingProb -= 100 / (clusterWidth / 2);
     * }
     * }
     * 
     * for (int j = clusterHeight - 1; j >= 0; j--) {
     * verticalBuildingProb = horizontalBuildingProb;
     * 
     * if ((!building) && calculateProbability(verticalBuildingProb)) {
     * building = true;
     * }
     * 
     * if (building) {
     * List<Integer> vector = new ArrayList<>(2);
     * vector.add(i);
     * vector.add(j);
     * listOfVectors.add(vector);
     * 
     * verticalBuildingProb = 100 - verticalBuildingProb;
     * 
     * if (calculateProbability(verticalBuildingProb)) {
     * break;
     * } else {
     * verticalBuildingProb += 100 / (clusterHeight / 2);
     * 
     * if (verticalBuildingProb > 100) {
     * verticalBuildingProb = 100;
     * }
     * }
     * } else {
     * verticalBuildingProb += 100 / (clusterHeight / 2);
     * 
     * if (verticalBuildingProb > 100) {
     * verticalBuildingProb = 100;
     * }
     * }
     * }
     * }
     * 
     * return listOfVectors;
     * }*/
    
    private boolean ellipseInnerArea(double x, double y, double a, double b) {
        return Math.pow(x - a, 2) / Math.pow(a, 2) + Math.pow(y - b, 2) / Math.pow(b, 2) <= 1;
    }
    
    private List<List<Integer>> generateVectorCluster(int clusterWidth, int clusterHeight) {
        List<List<Integer>> listOfVectors = new ArrayList<>(clusterWidth * clusterHeight);
        
        for (int i = 0; i < clusterWidth; i++) {
            
            for (int j = clusterHeight - 1; j >= 0; j--) {
                
                if (ellipseInnerArea(i, j, clusterWidth / 2.0, clusterHeight / 2.0)) {
                    List<Integer> vector = new ArrayList<>(2);
                    vector.add(i);
                    vector.add(j);
                    listOfVectors.add(vector);
                }
            }
        }
        
        return listOfVectors;
    }
}