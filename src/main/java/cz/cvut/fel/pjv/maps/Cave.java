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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Cave represents an empty area under the surface of the world. It is defined a List of Vectors. 
 * An enemy NPC spawns in each cave at the start of the game.
 * A special vector called "spawner" defines the exact starting position of this enemy.
 * 
 * @author Zdenek
 * @version 1.1
 */
public class Cave implements Serializable {
    
    /**
     * Variables x and y are the coordinates of the center of the core ellipse of a cave.
     */
    private final int x;
    private final int y;
    
    private final List<List<BlockType>> map;
    
    /**
     * The complete cave is stored as a list of vectors, that represent the cave's area.
     */
    private List<Vector> caveVectors;
    
    private Vector spawner;

    /**
     * Initializes a new Cave.
     * 
     * @param x x coordinate of the cave
     * @param y y coordinate of the cave
     * @param map the current state of the map is used to decide about specific vectors of the cave
     * @since 1.0
     */
    public Cave(int x, int y, List<List<BlockType>> map) {
        this.x = x;
        this.y = y;
        this.map = map;
        
        generateCaveVectors();
        createSpawner();
    }

    /**
     * @return A List of all Vectors that make up the cave.
     * @since 1.0
     */
    public List<Vector> getCaveVectors() {
        return caveVectors;
    }

    /**
     * @return A Vector that contains coordinates for the spawn of a new NPC.
     * @since 1.1
     */
    public Vector getSpawner() {
        return spawner;
    }
    
    private boolean ellipseInnerArea(double x, double y, double a, double b) {
        return Math.pow(x - a, 2) / Math.pow(a, 2) + Math.pow(y - b, 2) / Math.pow(b, 2) <= 1;
    }
    
    private List<Vector> newEllipse(int m, int n) {
        List<Vector> ellipse = new ArrayList<>();
        
        int ellipseWidth = RNG.randomIntInRange(10, 30);
        int ellipseHeight = RNG.randomIntInRange(4, 14);
        
        double a = ellipseWidth / 2.0;
        double b = ellipseHeight / 2.0;
        
        for (int i = 0; i < ellipseWidth; i++) {
            
            for (int j = ellipseHeight - 1; j >= 0; j--) {
                
                if (ellipseInnerArea(i, j, a, b)) {
                    // The BlockType.AIR parameter in a Vector of a Cave serves merely an informational purpose.
                    // Null is always used instead of BlockType.AIR in the actual worldMap.
                    ellipse.add(new Vector(i + m - (int) (Math.round(a)), j + n - (int) (Math.round(b)), BlockType.AIR));
                }
            }
        }
        
        return ellipse;
    }
    
    private void addEllipseToCave(List<Vector> ellipse) {
        for (Vector vector : ellipse) {

            if ((vector.getY() >= 0) && (vector.getY() < map.get(0).size())) {

                if (vector.getX() < 0) {
                    vector.setX(map.size() + vector.getX());
                } else if (vector.getX() >= map.size()) {
                    vector.setX(vector.getX() - map.size());
                }

                if (!caveVectors.contains(vector)) {

                    if (map.get(vector.getX()).get(vector.getY()) == BlockType.STONE) {
                        caveVectors.add(vector);
                    }
                }
            }
        }
    }
    
    private void generateCaveVectors() {
        caveVectors = new ArrayList<>();
        
        int newCenterX = x;
        int newCenterY = y;
        
        for (int i = 0; i < RNG.randomIntInRange(3, 7); i++) {
            addEllipseToCave(newEllipse(newCenterX, newCenterY));
            
            // get x from a random Vector in caveVectors
            newCenterX = caveVectors.get(RNG.randomIntInRange(0, caveVectors.size() - 1)).getX();
            // add a random value to shift the center of the new cave
            //newCenterX += RNG.randomIntInRange(-9, 9);
            // same for y
            newCenterY = caveVectors.get(RNG.randomIntInRange(0, caveVectors.size() - 1)).getY();
            //newCenterY += RNG.randomIntInRange(-3, 3);
        }
    }
    
    private void createSpawner() {
        Vector min = caveVectors.get(0);
        
        for (Vector vector : caveVectors) {
            
            if (vector.getY() < min.getY()) {
                min = vector;
            }
        }
        
        spawner = min;
        
        // create space for the head of the spawned NPC
        Vector spaceForHead = new Vector(spawner.getX(), spawner.getY() + 1, BlockType.AIR);
        
        if (!caveVectors.contains(spaceForHead)) {
            caveVectors.add(spaceForHead);
        }
    }
}
