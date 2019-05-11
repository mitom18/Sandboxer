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
import cz.cvut.fel.pjv.blocks.SolidBlock;
import cz.cvut.fel.pjv.creatures.NPC;
import cz.cvut.fel.pjv.creatures.Player;
import cz.cvut.fel.pjv.items.Item;
import cz.cvut.fel.pjv.items.ItemType;
import cz.cvut.fel.pjv.items.StoredBlock;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

/**
 * World unit tests.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class WorldTest {
    
    private static Thread javaFXBGThread;
    private static World testWorld;
    
    /**
     * Start test JavaFX application needed for internal graphics initialization.
     * Create the world instance that will be used in tests.
     *
     * @throws IOException
     */
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
    }
    
    /**
     * Delete the world instance and shut down the JavaFX application.
     */
    @AfterClass
    public static void tearDownClass() {
        testWorld = null;
        Platform.exit();
        javaFXBGThread = null;
    }

    /**
     * Test of addItem method, of class World.
     */
    @Test
    public void testAddItem() {
        Item itemToAdd = new StoredBlock(0, 0, ItemType.DIRT);
        World instance = mock(World.class);
        ArgumentCaptor<Item> arg = ArgumentCaptor.forClass(Item.class);
        instance.addItem(itemToAdd);
        verify(instance, times(1)).addItem(arg.capture());
        assertEquals(itemToAdd, arg.getValue());
    }
    
    /**
     * Test of addItem method, of class World.
     */
    @Test
    public void testAddItem2() {
        Item itemToAdd = new StoredBlock(0, 0, ItemType.DIRT);
        World instance = testWorld;
        instance.addItem(itemToAdd);
        List<Item> expResult = new ArrayList<>();
        expResult.add(itemToAdd);
        assertEquals(instance.getItems(), expResult);
    }

    /**
     * Test of updateLayingItems method, of class World.
     */
    @Test
    public void testUpdateLayingItems() {
        World instance = mock(World.class);
        instance.updateLayingItems();
        verify(instance, times(1)).updateLayingItems();
    }

    /**
     * Test of updateNPCs method, of class World.
     */
    @Test
    public void testUpdateNPCs() {
        Player player = new Player(0, 0);
        World instance = mock(World.class);
        ArgumentCaptor<Player> arg = ArgumentCaptor.forClass(Player.class);
        instance.updateNPCs(player);
        verify(instance, times(1)).updateNPCs(arg.capture());
        assertEquals(player, arg.getValue());
    }

    /**
     * Test of updateLiquids method, of class World.
     */
    @Test
    public void testUpdateLiquids() {
        Player player = new Player(0, 0);
        World instance = mock(World.class);
        ArgumentCaptor<Player> arg = ArgumentCaptor.forClass(Player.class);
        instance.updateNPCs(player);
        verify(instance, times(1)).updateNPCs(arg.capture());
        assertEquals(player, arg.getValue());
    }
    
    /**
     * Test of getHighestBlockY method, of class World.
     */
    @Test
    public void testGetHighestBlockY() {
        double x = 0.0;
        World instance = testWorld;
        double expResult = 0.0;
        double result = instance.getHighestBlockY(x);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getHighestBlockY method, of class World.
     */
    @Test
    public void testGetHighestBlockY2() {
        double x = 9*Block.block_width;
        World instance = testWorld;
        double expResult = -Block.block_height;
        double result = instance.getHighestBlockY(x);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getBlocks method, of class World.
     */
    @Test
    public void testGetBlocks() {
        World instance = testWorld;
        int expResult = 11;
        int result = instance.getBlocks().size();
        assertEquals(expResult, result);
    }

    /**
     * Test of getItems method, of class World.
     */
    @Test
    public void testGetItems() {
        World instance = testWorld;
        List<Item> expResult = new ArrayList<>();
        List<Item> result = instance.getItems();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNpcs method, of class World.
     */
    @Test
    public void testGetNpcs() {
        World instance = testWorld;
        List<NPC> expResult = new ArrayList<>();
        List<NPC> result = instance.getNpcs();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPlayerSpawnX method, of class World.
     */
    @Test
    public void testGetPlayerSpawnX() {
        World instance = testWorld;
        double expResult = 0.0;
        instance.setPlayerSpawnX(expResult);
        double result = instance.getPlayerSpawnX();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setPlayerSpawnX method, of class World.
     */
    @Test
    public void testSetPlayerSpawnX() {
        double playerSpawnX = 25.0;
        World instance = mock(World.class);
        ArgumentCaptor<Double> arg = ArgumentCaptor.forClass(Double.class);
        instance.setPlayerSpawnX(playerSpawnX);
        verify(instance, times(1)).setPlayerSpawnX(arg.capture());
        assertEquals(playerSpawnX, arg.getValue(), 0.0);
    }
    
    /**
     * Test of setPlayerSpawnX method, of class World.
     */
    @Test
    public void testSetPlayerSpawnX2() {
        double playerSpawnX = 5.0;
        World instance = testWorld;
        instance.setPlayerSpawnX(playerSpawnX);
        assertEquals(testWorld.getPlayerSpawnX(), playerSpawnX, 0.0);
    }
    
}
