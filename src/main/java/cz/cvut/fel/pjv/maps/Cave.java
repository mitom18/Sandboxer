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
 *
 * @author Zdenek
 */
public class Cave {
    
    /**
     * Variables m and n are the coordinates of the center of the core ellipse of a cave.
     */
    private final int m;
    private final int n;
    
    /**
     * The complete cave is stored as a list of vectors, that represent the cave's area.
     */
    private List<List<Integer>> caveVectors;

    public Cave(int m, int n) {
        this.m = m;
        this.n = n;
    }
    
    

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public List<List<Integer>> getCaveVectors() {
        return caveVectors;
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
