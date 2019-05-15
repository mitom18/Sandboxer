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

import java.util.Random;

/**
 * RNG is a random number generator for the entire game.
 * 
 * @author Zdenek
 * @version 1.1
 */
public abstract class RNG {
    
    private static String inputSeed;
    private static long seed;
    private static final Random r = new Random();

    /**
     * Sets a new seed to the instance of Random.
     * @since 1.0
     */
    public static void setNewSeed() {
        inputSeed = WorldMap.getMapConfig().seed;
        // These are nice seeds: 7451260251423394044L -7780041021634934149L
        
        if (inputSeed.equals("")) {
            seed = r.nextLong();
        } else {
            seed = Long.parseLong(inputSeed);
        }
        
        r.setSeed(seed);
    }
    
    /**
     * @return the seed that the instance of Random is currently using
     * @since 1.0
     */
    public static long getSeed() {
        return seed;
    }
    
    /**
     * Generates a random double in a given range.
     * 
     * @param min smallest possible generated number
     * @param max largest possible generated number
     * @return random double in the given range
     * @since 1.0
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
     * @since 1.0
     */
    public static int randomIntInRange(int min, int max) {
        return min + r.nextInt((max - min) + 1);
    }
    
    /**
     * @param probability in percent (can take fractions)
     * @return true or false, chosen randomly but with the given probability
     * @since 1.1
     */
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
