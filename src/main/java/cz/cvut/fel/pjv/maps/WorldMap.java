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
import cz.cvut.fel.pjv.items.ItemType;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main output of class WorldMap is a map for a newly created world. 
 * The map defines types of blocks and their position in the world.
 *
 * @author Zdenek
 * @version 1.2
 */
public class WorldMap implements Serializable {
    
    private static MapConfig mapConfig;
    
    private final int WIDTH;
    private final int HEIGHT;
    
    private final long SEED;

    private List<List<Integer>> terrain;
    private List<List<BlockType>> map;
    private Map<List<Integer>, ItemType> itemMap;
    
    private List<Integer> completeSkyline;
    private List<Cave> caves;
    private Map<String, Structure> structures;
    
    private final int playerX;
    private final int playerY;

    /**
     * If no custom map is given in the JSON configuration file (mapConfig.JSON), create a new random map,
     * else create a new map from the given 2D array (completeMap).
     * 
     * @throws java.io.IOException
     * @since 1.0
     */
    public WorldMap() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        
        mapConfig = objectMapper.readValue(
            new File("mapConfig.JSON"), MapConfig.class
        );
        
        RNG.setNewSeed();
        SEED = RNG.getSeed();
        
        if (mapConfig.completeMap.get(0).isEmpty()) {
            WIDTH = mapConfig.width;
            HEIGHT = mapConfig.height;
            
            generateTerrain();
            generateMap();
            generateItemMap();
            
            // Spawn in the middle of the map.
            playerX = 0;
            // Spawn on the skyline (in the middle of the map).
            playerY = HEIGHT - completeSkyline.get(WIDTH / 2) - 2;
        } else {
            WIDTH = mapConfig.completeMap.get(0).size();
            HEIGHT = mapConfig.completeMap.size();
            customMap();
            
            playerX = mapConfig.playerStartingX;
            playerY = mapConfig.playerStartingY;
        }
    }
    
    /**
     * @return seed used for the random generator
     * @since 1.1
     */
    public long getSeed() {
        return SEED;
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
     * @return player's starting x coordinate
     * @since 1.2
     */
    public int getPlayerX() {
        return playerX;
    }
    
    /**
     * @return player's starting y coordinate
     * @since 1.2
     */
    public int getPlayerY() {
        return playerY;
    }

    /**
     * @return list of caves
     * @since 1.1
     */
    public List<Cave> getCaves() {
        return caves;
    }

    /**
     * @return map of structures
     * @since 1.2
     */
    public Map<String, Structure> getStructures() {
        return structures;
    }
    
    /**
     * @return instance of MapConfig, contains all configuration data for the map
     * @since 1.1
     */
    public static MapConfig getMapConfig() {
        return mapConfig;
    }

    /**
     * @return map of items generated with the map
     * @since 1.2
     */
    public Map<List<Integer>, ItemType> getItemMap() {
        return itemMap;
    }
    
    private void customMap() {
        map = new ArrayList<>(WIDTH);
        
        for (int i = 0; i < WIDTH; i++) {
            map.add(new ArrayList<BlockType>(HEIGHT));
            
            for (int j = 0; j < HEIGHT; j++) {
                map.get(i).add(mapConfig.completeMap.get(j).get(i));
            }
        }
    }
    
    private double calculateSkyline(double x, double amplitudeCoefficient, double periodCoefficient, double previousY) {
        // Mathematical function in the form of: y = a * sin(b * x) + c
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
        
        double amplitudeCoefficient = RNG.randomDoubleInRange(mapConfig.ampMin, mapConfig.ampMax) * mapConfig.amplitudeCoefficientMultiplicator;
        double periodCoefficient = 1 / (RNG.randomDoubleInRange(mapConfig.perMin, mapConfig.perMax) * mapConfig.periodCoefficientMultiplicator);
        
        double period = (2 * Math.PI) / (periodCoefficient);
        double previousY = HEIGHT / 2;
        
        int counter = 1;

        for (int i = 0; i < WIDTH; i++) {
            terrain.add(new ArrayList<Integer>(HEIGHT));
            
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

            for (int j = 0; j < HEIGHT; j++) {
                
                if (j == 0) {
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
        
        //Used to define the border between STONE and DIRT.
        int skylineModifier = 0;
        
        int dirtStoneBorder;
        
        for (int i = 0; i < WIDTH; i++) {
            map.add(new ArrayList<BlockType>(HEIGHT));
            
            boolean isUnderWater = false;
            
            if (skylineModifier < - 2) {
                skylineModifier += RNG.randomIntInRange(0, 2);
            } else if (skylineModifier > 6) {
                skylineModifier += RNG.randomIntInRange(-2, 1);
            } else {
                skylineModifier += RNG.randomIntInRange(-1, 1);
            }
            
            dirtStoneBorder = completeSkyline.get(i) - skylineModifier - 1;
            
            for (int j = HEIGHT - 1; j >= 0; j--) {
                
                if (terrain.get(i).get(j) == 1) {
                    
                    if (j == 0) {
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
                    } else if ((j <= HEIGHT / 8) && RNG.calculateProbability(mapConfig.diamondProbability)) {
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
        generateStructures();
    }
    
    /**
     * Finds random coordinates to create caves and inserts them into the map.
     * 
     * @since 1.1
     */
    private void generateCaves() {
        caves = new ArrayList<>();
        
        for (int i = 0; i < WIDTH; i++) {
            
            for (int j = HEIGHT - 1; j >= 1; j--) {
                
                if ((map.get(i).get(j) == BlockType.STONE) && RNG.calculateProbability(mapConfig.caveProbability)) {
                    caves.add(new Cave(i, j, map));
                    
                    // Insert the cave into the map
                    for (Vector vector : caves.get(caves.size() - 1).getCaveVectors()) {
                        map.get(vector.getX()).set(vector.getY(), null);
                    }
                }
            }
        }
    }
    
    /**
     * Finds random coordinates to create structures from blueprints 
     * that have been defined in mapConfig.JSON and inserts them into the map.
     * 
     * @since 1.2
     */
    private void generateStructures() {
        structures = new HashMap<>(mapConfig.structureBlueprints.size());
        
        for (Map.Entry<String, List<List<BlockType>>> structureBlueprint : mapConfig.structureBlueprints.entrySet()) {
            String structureName = structureBlueprint.getKey();
            List<List<BlockType>> structureData = structureBlueprint.getValue();
            
            int structureX = RNG.randomIntInRange(0, WIDTH - structureData.get(0).size());
            int structureY = RNG.randomIntInRange(0, HEIGHT - structureData.size() - 1);

            structures.put(structureName, new Structure(structureData, structureX, structureY));
            
            // Insert the structure into the map
            for (Vector vector : structures.get(structureName).getStructureVectors()) {

                if ((vector.getBlockType() == BlockType.AIR) || (vector.getBlockType() == BlockType.SPAWNER)) {
                    map.get(vector.getX()).set(vector.getY(), null);
                } else {
                    map.get(vector.getX()).set(vector.getY(), vector.getBlockType());
                }
            }
        }
    }
    
    /**
     * Create a map for spawning collectible items, that has coordinates as key and ItemType as value.
     * 
     * @since 1.2
     */
    private void generateItemMap() {
        itemMap = new HashMap<>();
        
        for (int i = 0; i < 10; i++) {
            int itemX = RNG.randomIntInRange(0, WIDTH - 1);
            
            for (int j = HEIGHT - 1; j >= 0; j--) {
                
                if (map.get(i).get(j) == null) {
                    int itemY = j;
                    
                    List<Integer> coords = new ArrayList<>(2);
                    coords.add(itemX);
                    coords.add(itemY);
                    itemMap.put(coords, ItemType.STONE);
                    
                    break;
                }
            }
        }
    }
}