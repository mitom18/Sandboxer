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

import cz.cvut.fel.pjv.World;
import cz.cvut.fel.pjv.blocks.Block;
import cz.cvut.fel.pjv.blocks.BlockType;
import cz.cvut.fel.pjv.blocks.LiquidBlock;
import cz.cvut.fel.pjv.blocks.SolidBlock;
import cz.cvut.fel.pjv.items.Item;
import cz.cvut.fel.pjv.items.ItemType;
import cz.cvut.fel.pjv.items.StoredBlock;
import cz.cvut.fel.pjv.testJavaFXApp;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

/**
 * Enemy unit tests.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class EnemyTest {
    
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
            public void run() {
                try {
                    Application.launch(testJavaFXApp.class, new String[1]);
                } catch (IllegalStateException e) {}
            }
        };
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
     * Test of update method, of class Enemy.
     */
    @Test
    public void testUpdate() {
        World world = testWorld;
        Player player = new Player(0, 0);
        Enemy instance = mock(Enemy.class);
        ArgumentCaptor<World> arg1 = ArgumentCaptor.forClass(World.class);
        ArgumentCaptor<Player> arg2 = ArgumentCaptor.forClass(Player.class);
        instance.update(world, player);
        verify(instance, times(1)).update(arg1.capture(), arg2.capture());
        assertEquals(world, arg1.getValue());
        assertEquals(player, arg2.getValue());
    }

    /**
     * Test of attack method, of class Enemy.
     */
    @Test
    public void testAttack() {
        Player player = new Player(0, 0);
        Enemy instance = mock(Enemy.class);
        ArgumentCaptor<Player> arg = ArgumentCaptor.forClass(Player.class);
        instance.attack(player);
        verify(instance, times(1)).attack(arg.capture());
        assertEquals(player, arg.getValue());
    }
    
    /**
     * Test of attack method, of class Enemy.
     */
    @Test
    public void testAttack2() {
        Player player = new Player(0, 0);
        int expectedPlayerHpAfterAttack = player.getHp() - 1;
        Enemy instance = new Enemy(0, 0, CreatureType.BOSS, testWorld);
        instance.attack(player);
        assertEquals(expectedPlayerHpAfterAttack, player.getHp());
    }

    /**
     * Test of die method, of class Enemy.
     */
    @Test
    public void testDie() {
        Enemy instance = new Enemy(0, 0, CreatureType.BOSS, testWorld);
        assertTrue(testWorld.getItems().size() > 0);
        instance.die();
        assertTrue(instance.isKilled());
        Item dropItem = testWorld.getItems().get(0);
        assertFalse(dropItem.isPicked());
    }
    
}
