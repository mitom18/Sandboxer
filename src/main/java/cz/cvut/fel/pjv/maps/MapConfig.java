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
public class MapConfig {
    
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
    
    private final String SEED;
    
    public MapConfig() {
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
        CAVE_PROBABILITY = 0.01;
        
        SEED = "";
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public double getAMPLITUDE_COEFFICIENT_MULTIPLICATOR() {
        return AMPLITUDE_COEFFICIENT_MULTIPLICATOR;
    }

    public double getPERIOD_COEFFICIENT_MULTIPLICATOR() {
        return PERIOD_COEFFICIENT_MULTIPLICATOR;
    }

    public double getAMP_MIN() {
        return AMP_MIN;
    }

    public double getAMP_MAX() {
        return AMP_MAX;
    }

    public double getPER_MIN() {
        return PER_MIN;
    }

    public double getPER_MAX() {
        return PER_MAX;
    }

    public double getFLAT_LAND_PROBABILITY() {
        return FLAT_LAND_PROBABILITY;
    }

    public double getDIAMOND_PROBABILITY() {
        return DIAMOND_PROBABILITY;
    }

    public double getCAVE_PROBABILITY() {
        return CAVE_PROBABILITY;
    }

    public String getSEED() {
        return SEED;
    }
    
    
}
