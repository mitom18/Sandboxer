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

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.pjv.blocks.BlockType;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Map generates a map. The map defines how a game world is going to look.
 * The constructor takes 2 integer arguments (width and height of the map) or
 * if no arguments are given width and height are set to default values.
 *
 * @author Zdenek
 * @version 1.1
 */
public class Map implements Serializable {
    
    private MapConfig mapConfig;

    private List<List<Integer>> terrain;
    private List<List<BlockType>> map;
    
    private List<Integer> completeSkyline;
    private List<Cave> caves;

    /**
     * Create a new map with default parameters.
     * 
     * @throws java.io.IOException
     * @since 1.0
     */
    public Map() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        
        mapConfig = objectMapper.readValue(
            new File("mapConfig.JSON"), MapConfig.class
        );
        
        RNG.setMapConfig(mapConfig);
        RNG.setNewSeed();
        
        generateTerrain();
        generateMap();
    }
    
    /**
     * @return randomly generated seed for this instance of Map
     * @since 1.1
     */
    public long getSeed() {
        return RNG.getSeed();
    }
    
    /**
     * @return width of the map in blocks
     * @since 1.0
     */
    public int getWidth() {
        return mapConfig.width;
    }

    /**
     * @return height of the map in blocks
     * @since 1.0
     */
    public int getHeight() {
        return mapConfig.height;
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
        terrain = new ArrayList<>(mapConfig.width);
        completeSkyline = new ArrayList<>(mapConfig.width);
        
        double amplitudeCoefficient = RNG.randomDoubleInRange(mapConfig.ampMin, mapConfig.ampMax) * mapConfig.amplitudeCoefficientMultiplicator;
        double periodCoefficient = 1 / (RNG.randomDoubleInRange(mapConfig.perMin, mapConfig.perMax) * mapConfig.periodCoefficientMultiplicator);
        
        double period = (2 * Math.PI) / (periodCoefficient);
        double previousY = mapConfig.height / 2;
        
        int counter = 1;

        for (int i = 0; i < mapConfig.width; i++) {
            terrain.add(new ArrayList<Integer>(mapConfig.height));
            
            double skyline = calculateSkyline((double) i, amplitudeCoefficient, periodCoefficient, previousY);
            completeSkyline.add((int) skyline);
            
            if ((int)period == counter) {
                
                if (RNG.calculateProbability(mapConfig.flatLandProbability)) {
                    amplitudeCoefficient = RNG.randomDoubleInRange(mapConfig.ampMin, mapConfig.ampMax) * 1;
                } else {
                    amplitudeCoefficient = RNG.randomDoubleInRange(mapConfig.ampMin, mapConfig.ampMax) * mapConfig.amplitudeCoefficientMultiplicator;
                }
                
                periodCoefficient = 1 / (RNG.randomDoubleInRange(mapConfig.perMin, mapConfig.perMax) * mapConfig.periodCoefficientMultiplicator);
                
                period = (2 * Math.PI) / (periodCoefficient);
                double nextSkyline = calculateSkyline((double) i + 1, amplitudeCoefficient, periodCoefficient, previousY);
                previousY += skyline - nextSkyline;
                
                counter = 1;
            }

            for (int j = 0; j < mapConfig.height; j++) {
                
                if ((j == 0) || (j == mapConfig.height - 1)) {
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
        map = new ArrayList<>(mapConfig.width);
        
        /**
         * Used to define the border between STONE and DIRT.
         */
        int skylineModifier = 0;
        
        int dirtStoneBorder;
        
        for (int i = 0; i < mapConfig.width; i++) {
            map.add(new ArrayList<BlockType>(mapConfig.height));
            
            boolean isUnderWater = false;
            
            if (skylineModifier < - 2) {
                skylineModifier += RNG.randomIntInRange(0, 2);
            } else if (skylineModifier > 6) {
                skylineModifier += RNG.randomIntInRange(-2, 1);
            } else {
                skylineModifier += RNG.randomIntInRange(-1, 1);
            }
            
            dirtStoneBorder = completeSkyline.get(i) - skylineModifier - 1;
            
            for (int j = mapConfig.height - 1; j >= 0; j--) {
                
                if (terrain.get(i).get(j) == 1) {
                    
                    if ((j == 0) || (j == mapConfig.height - 1)) {
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
                    } else if ((j <= mapConfig.height / 8) && RNG.calculateProbability(mapConfig.diamondProbability)) {
                        // Diamond
                        map.get(i).add(BlockType.DIAMOND_ORE);
                    } else {
                        // Stone
                        map.get(i).add(BlockType.STONE);
                    }
                } else if (j <= mapConfig.height / 2 - mapConfig.height / 16) {
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
        
        for (int i = 0; i < mapConfig.width; i++) {
            
            for (int j = mapConfig.height - 1; j >= 0; j--) {
                
                if ((i < mapConfig.width - maxCaveWidth) && (j < mapConfig.height - maxCaveHeight) && (map.get(i).get(j) == BlockType.STONE) && RNG.calculateProbability(mapConfig.caveProbability)) {
                    List<List<Integer>> listOfVectors = generateVectorCluster(RNG.randomIntInRange(10, maxCaveWidth), RNG.randomIntInRange(4, maxCaveHeight));
                    
                    for (List<Integer> vector : listOfVectors) {
                        
                        if (map.get(vector.get(0) + i).get(vector.get(1) + j) == BlockType.STONE) {
                            map.get(vector.get(0) + i).set(vector.get(1) + j, null);
                        }
                    }
                }
            }
        }
    }
    
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