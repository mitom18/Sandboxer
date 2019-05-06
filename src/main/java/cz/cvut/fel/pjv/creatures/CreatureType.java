/*
 * The MIT License
 *
 * Copyright 2019 Michal-jr.
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
package cz.cvut.fel.pjv.creatures;

import javafx.scene.image.Image;

/**
 * Enumeration class representing all types of creatures that can appear in the world.
 *
 * @author Michal-jr
 * @version 1.0
 */
public enum CreatureType {
    
    /**
     * Type of player.
     */
    PLAYER(0, new Image("spritesheet_hero.png"), 10),
    
    /**
     * Type of monk.
     */
    MONK(0, new Image("spritesheet_monk.png"), 15),
    
    /**
     * Type of skeleton.
     */
    SKELETON(1, new Image("spritesheet_skeleton.png"), 5);

    private final int id;
    private final Image spritesheet;
    private int hp;


    private CreatureType(int id, Image spritesheet, int hp) {
        this.id = id;
        this.spritesheet = spritesheet;
        this.hp = hp;
    }

    /**
     * @return spritesheet of the specified creature
     * @since 1.0
     */
    public Image getSpritesheet() {
        return spritesheet;
    }

    /**
     * @return health points of the specified creature
     * @since 1.0
     */
    public int getHp() {
        return hp;
    }
    
}
