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
 *
 * @author Zdenek
 */
public class Structure implements Serializable {
    
    private final String structureName;
    private List<Vector> structureVectors;
    
    private final int x;
    private final int y;
    
    private Vector spawner;

    /**
     * The constructor of Structure.
     * 
     * @param structureName the name of the structure as it was given in the mapConfig.JSON file
     * @param x
     * @param y
     */
    public Structure(String structureName, int x, int y) {
        this.structureName = structureName;
        this.x = x;
        this.y = y;
        
        createBlueprintVectors();
        createSpawner();
    }

    public List<Vector> getStructureVectors() {
        return structureVectors;
    }

    public Vector getSpawner() {
        return spawner;
    }
    
    private void createBlueprintVectors() {
        structureVectors = new ArrayList<>();
        
        for (int i = 0; i < WorldMap.getMapConfig().structureBlueprints.get(structureName).get(0).size(); i++) {
            
            for (int j = 0; j < WorldMap.getMapConfig().structureBlueprints.get(structureName).size(); j++) {
                
                if (WorldMap.getMapConfig().structureBlueprints.get(structureName).get(j).get(i) != null) {
                    structureVectors.add(new Vector(i + x, j + y, WorldMap.getMapConfig().structureBlueprints.get(structureName).get(j).get(i)));
                }
            }
        }
    }
    
    private void createSpawner() {
        
        for (Vector vector : structureVectors) {
            
            if (vector.getBlockType() == BlockType.SPAWNER) {
                spawner = vector;
                break;
            }
        }
        
        // create space for the head of the spawned NPC
        Vector spaceForHead = new Vector(spawner.getX(), spawner.getY() + 1, BlockType.AIR);
        
        if (!structureVectors.contains(spaceForHead)) {
            structureVectors.add(spaceForHead);
        }
    }
}
