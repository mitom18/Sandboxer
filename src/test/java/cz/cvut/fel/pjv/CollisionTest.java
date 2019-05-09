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
package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.blocks.Block;
import cz.cvut.fel.pjv.blocks.BlockType;
import cz.cvut.fel.pjv.blocks.LiquidBlock;
import cz.cvut.fel.pjv.blocks.SolidBlock;
import cz.cvut.fel.pjv.creatures.Creature;
import cz.cvut.fel.pjv.creatures.CreatureType;
import cz.cvut.fel.pjv.creatures.Enemy;
import cz.cvut.fel.pjv.creatures.Player;
import cz.cvut.fel.pjv.items.Item;
import cz.cvut.fel.pjv.items.ItemType;
import cz.cvut.fel.pjv.items.StoredBlock;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * Collision unit tests.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class CollisionTest {
    
    private static Thread javaFXBGThread;
    private static World testWorld;
    
    @BeforeClass
    public static void setUpClass() throws IOException {
        javaFXBGThread = new Thread() {
            @Override
            public void run() { Application.launch(testJavaFXApp.class, new String[1]); }
        };
        javaFXBGThread.setDaemon(true);
        javaFXBGThread.start();
        
        testWorld = new World();
        testWorld.getBlocks().removeAll(testWorld.getBlocks());
        testWorld.getItems().removeAll(testWorld.getItems());
        testWorld.getNpcs().removeAll(testWorld.getNpcs());
        for (int i = 0; i < 10; i++) {
            testWorld.getBlocks().add(new SolidBlock(i*Block.block_width, 0, BlockType.DIRT));
        }
        testWorld.getBlocks().add(new SolidBlock(9*Block.block_width, -Block.block_height, BlockType.DIRT));
        testWorld.getBlocks().add(new LiquidBlock(0*Block.block_width, -Block.block_height, BlockType.WATER));
        testWorld.getItems().add(new StoredBlock(0, 0, ItemType.DIRT));
    }
    
    @AfterClass
    public static void tearDownClass() {
        testWorld = null;
        Platform.exit();
        javaFXBGThread = null;
    }

    /**
     * Test of preventCollision method, of class Collision.
     */
    @Test
    public void testPreventCollision() {
        Creature creature = new Player(0, Block.block_height-1);
        World world = testWorld;
        Collision.preventCollision(creature, world);
        assertEquals(creature.getY(), Block.block_height, 0.0);
    }

    /**
     * Test of preventItemCollision method, of class Collision.
     */
    @Test
    public void testPreventItemCollision() {
        Item item = new StoredBlock(0, Block.block_height-3, ItemType.DIRT);
        World world = testWorld;
        Collision.preventItemCollision(item, world);
        assertEquals(item.getY(), Block.block_height, 0.0);
    }

    /**
     * Test of creatureHasBlockInFront method, of class Collision.
     */
    @Test
    public void testCreatureHasBlockInFront() {
        World world = testWorld;
        Creature creature = new Enemy(10*Block.block_width, -1.5*Block.block_height, CreatureType.SKELETON, world);
        creature.setLeft(true);
        boolean expResult = true;
        boolean result = Collision.creatureHasBlockInFront(creature, world);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of creatureHasBlockInFront method, of class Collision.
     */
    @Test
    public void testCreatureHasBlockInFront2() {
        World world = testWorld;
        Creature creature = new Enemy(8*Block.block_width, -1.5*Block.block_height, CreatureType.SKELETON, world);
        creature.setRight(true);
        boolean expResult = true;
        boolean result = Collision.creatureHasBlockInFront(creature, world);
        assertEquals(expResult, result);
    }

    /**
     * Test of creatureIsInLiquid method, of class Collision.
     */
    @Test
    public void testCreatureIsInLiquid() {
        World world = testWorld;
        Creature creature = new Enemy(0*Block.block_width, -1.5*Block.block_height, CreatureType.SKELETON, world);
        Collision.creatureIsInLiquid(creature, world);
        assertTrue(creature.swimming());
    }

    /**
     * Test of collides method, of class Collision.
     */
    @Test
    public void testCollides() {
        double clickX = 0.5;
        double clickY = 0.5;
        Block block = new SolidBlock(0, 0, BlockType.DIRT);
        boolean expResult = true;
        boolean result = Collision.collides(clickX, clickY, block);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of collides method, of class Collision.
     */
    @Test
    public void testCollides2() {
        double clickX = -1;
        double clickY = -1;
        Block block = new SolidBlock(0, 0, BlockType.DIRT);
        boolean expResult = false;
        boolean result = Collision.collides(clickX, clickY, block);
        assertEquals(expResult, result);
    }

    /**
     * Test of creatureIsAttacked method, of class Collision.
     */
    @Test
    public void testCreatureIsAttacked() {
        World world = testWorld;
        double x = 50;
        double y = Block.block_height;
        double width = 20;
        double height = 2*Block.block_height;
        Creature creature = new Enemy(60, 60, CreatureType.SKELETON, world);
        boolean expResult = true;
        boolean result = Collision.creatureIsAttacked(x, y, width, height, creature);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of creatureIsAttacked method, of class Collision.
     */
    @Test
    public void testCreatureIsAttacked2() {
        World world = testWorld;
        double x = 70;
        double y = Block.block_height;
        double width = -20;
        double height = 2*Block.block_height;
        Creature creature = new Enemy(60, 60, CreatureType.SKELETON, world);
        boolean expResult = true;
        boolean result = Collision.creatureIsAttacked(x, y, width, height, creature);
        assertEquals(expResult, result);
    }

    /**
     * Test of controlItems method, of class Collision.
     */
    @Test
    public void testControlItems() {
        World world = testWorld;
        Player player = new Player(0, 0);
        Collision.controlItems(player, world);
        assertTrue(player.getInventory().getInv().length > 1);
    }
    
}
