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

import cz.cvut.fel.pjv.items.Item;
import cz.cvut.fel.pjv.items.ItemType;
import cz.cvut.fel.pjv.items.StoredBlock;
import cz.cvut.fel.pjv.items.Tool;
import cz.cvut.fel.pjv.testJavaFXApp;
import static java.lang.Thread.sleep;
import javafx.application.Application;
import javafx.application.Platform;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

/**
 * Inventory unit tests.
 *
 * @author Michal-jr
 * @version 1.0
 */
public class InventoryTest {
    
    private static Thread javaFXBGThread;
    
    /**
     * Start test JavaFX application needed for internal graphics initialization.
     * 
     * @throws java.lang.InterruptedException
     */
    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        javaFXBGThread = new Thread() {
            @Override
            public void run() {
                try {
                    Application.launch(testJavaFXApp.class, new String[1]);
                } catch (IllegalStateException e) {}
            }
        };
        javaFXBGThread.start();
        sleep(1000); //wait 1 second for JavaFX Toolkit to properly initialize
    }
    
    /**
     * Shut down the JavaFX application.
     */
    @AfterClass
    public static void tearDownClass() {
        Platform.exit();
        javaFXBGThread = null;
    }

    /**
     * Test of add method, of class Inventory.
     */
    @Test
    public void testAdd() {
        Item itemToAdd = new StoredBlock(0, 0, ItemType.DIRT);
        Inventory instance = mock(Inventory.class);
        ArgumentCaptor<Item> arg = ArgumentCaptor.forClass(Item.class);
        instance.add(itemToAdd);
        verify(instance, times(1)).add(arg.capture());
        assertEquals(itemToAdd, arg.getValue());
    }
    
    /**
     * Test of add method, of class Inventory.
     */
    @Test
    public void testAdd2() {
        Item itemToAdd = new StoredBlock(0, 0, ItemType.DIRT);
        Item itemToAdd2 = new StoredBlock(0, 0, ItemType.DIRT);
        Inventory instance = new Inventory();
        instance.add(itemToAdd);
        instance.add(itemToAdd2);
        instance.setActiveItemIndex(1);
        StoredBlock activeItem = (StoredBlock) instance.getActiveItem();
        assertTrue(activeItem.getQuantity() == 2);
    }

    /**
     * Test of remove method, of class Inventory.
     */
    @Test
    public void testRemove() {
        Inventory instance = mock(Inventory.class);
        ArgumentCaptor<Item> arg = ArgumentCaptor.forClass(Item.class);
        Item itemToRemove = instance.getActiveItem();
        instance.remove(itemToRemove);
        verify(instance, times(1)).remove(arg.capture());
        assertEquals(itemToRemove, arg.getValue());
        assertNull(instance.getActiveItem());
    }
    
    /**
     * Test of remove method, of class Inventory.
     */
    @Test
    public void testRemove2() {
        Inventory instance = new Inventory();
        Item itemToAdd = new StoredBlock(0, 0, ItemType.DIRT);
        instance.add(itemToAdd);
        instance.add(itemToAdd);
        instance.setActiveItemIndex(1);
        Item itemToRemove = instance.getActiveItem();
        instance.remove(itemToRemove);
        StoredBlock activeItem = (StoredBlock) instance.getActiveItem();
        assertTrue(activeItem.getQuantity() == 1);
    }

    /**
     * Test of getInv method, of class Inventory.
     */
    @Test
    public void testGetInv() {
        Inventory instance = new Inventory();
        Item[] expResult = new Item[64];
        expResult[0] = new Tool(0, 0, ItemType.PICKAXE);
        Item[] result = instance.getInv();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getHotbarItems method, of class Inventory.
     */
    @Test
    public void testGetHotbarItems() {
        Inventory instance = new Inventory();
        Item[] expResult = new Item[10];
        expResult[0] = new Tool(0, 0, ItemType.PICKAXE);
        Item[] result = instance.getHotbarItems();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getActiveItemIndex method, of class Inventory.
     */
    @Test
    public void testGetActiveItemIndex() {
        Inventory instance = new Inventory();
        int expResult = 0;
        int result = instance.getActiveItemIndex();
        assertEquals(expResult, result);
    }

    /**
     * Test of getActiveItem method, of class Inventory.
     */
    @Test
    public void testGetActiveItem() {
        Inventory instance = new Inventory();
        Item expResult = new Tool(0, 0, ItemType.PICKAXE);
        Item result = instance.getActiveItem();
        assertEquals(expResult, result);
    }

    /**
     * Test of setActiveItemIndex method, of class Inventory.
     */
    @Test
    public void testSetActiveItemIndex() {
        int activeItemIndex = 5;
        Inventory instance = new Inventory();
        instance.setActiveItemIndex(activeItemIndex);
        assertEquals(instance.getActiveItemIndex(), activeItemIndex);
    }
    
}
