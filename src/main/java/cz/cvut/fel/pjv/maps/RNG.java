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

import java.util.Random;

/**
 *
 * @author Zdenek
 */
public abstract class RNG {
    
    private static MapConfig mapConfig;
    
    private static String inputSeed;
    private static long seed;
    private static Random r;

    public static void setMapConfig(MapConfig mapConfig) {
        RNG.mapConfig = mapConfig;
    }

    public static void setNewSeed() {
        inputSeed = mapConfig.seed;
        // These are nice seeds: 7451260251423394044L -7780041021634934149L
        r = new Random();
        
        if (inputSeed.equals("")) {
            seed = r.nextLong();
        } else {
            seed = Long.parseLong(inputSeed);
        }
        
        r.setSeed(seed);
    }
    
    public static long getSeed() {
        return seed;
    }

    public static Random getR() {
        return r;
    }
    
    /**
     * Generates a random double in a given range.
     * 
     * @param min smallest possible generated number
     * @param max largest possible generated number
     * @return random double in the given range
     * @since 1.1
     */
    public static double randomDoubleInRange(double min, double max) {
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
    public static int randomIntInRange(int min, int max) {
        return min + r.nextInt((max - min) + 1);
    }
    
    public static boolean calculateProbability(double probability) {
        
        if (probability <= 0) {
            return false;
        } else if (probability >= 100) {
            return true;
        } else {
            return randomIntInRange(1, (int) (100 / probability)) == 1;
        }
    }
}
