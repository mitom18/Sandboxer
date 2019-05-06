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
package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.blocks.LiquidBlock;
import cz.cvut.fel.pjv.blocks.SolidBlock;
import cz.cvut.fel.pjv.blocks.BlockType;
import cz.cvut.fel.pjv.blocks.Block;
import cz.cvut.fel.pjv.creatures.CreatureType;
import cz.cvut.fel.pjv.creatures.Enemy;
import cz.cvut.fel.pjv.creatures.Friend;
import cz.cvut.fel.pjv.creatures.NPC;
import cz.cvut.fel.pjv.creatures.Player;
import cz.cvut.fel.pjv.items.Item;
import cz.cvut.fel.pjv.items.StoredBlock;
import cz.cvut.fel.pjv.items.ItemType;
import cz.cvut.fel.pjv.maps.Cave;
import cz.cvut.fel.pjv.maps.WorldMap;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains all entities in the world.
 * 
 * @author Zdenek
 * @version 1.0
 */
public class World implements Serializable {
    
    private final int WIDTH;
    private final int HEIGHT;
    
    /**
     * Array of instances of blocks in the world.
     */
    private List<Block> blocks;
    
    /**
     * Array of instances of items in the world.
     */
    private List<Item> items;
    
    /**
     * Array of instances of NPCs in the world.
     */
    private List<NPC> npcs;
    
    /*
     * Instance of WorldMap.
     */
    private final WorldMap worldMap;
    
    /**
     * Create new world.
     *
     * @throws java.io.IOException
     * @since 1.0
     */
    public World() throws IOException {
        worldMap = new WorldMap();
        WIDTH = worldMap.getWIDTH();
        HEIGHT = worldMap.getHEIGHT();
        createWorld();
    }
    
    private void spawnNPCs() {
        npcs = new ArrayList<>();
        int i = 0;
        for (Cave cave : worldMap.getCaves()) {
            npcs.add(new Enemy((cave.getSpawner().getX() - worldMap.getMap().size() / 2) * Block.block_width, cave.getSpawner().getY() * Block.block_height, CreatureType.SKELETON));
            i++;
            System.out.println("NPC num: " + i);
            System.out.println("x: " + cave.getSpawner().getX() * Block.block_width);
            System.out.println("y: " + cave.getSpawner().getY() * Block.block_height);
        }
        npcs.add(new Friend(32, 0, CreatureType.MONK));
    }
    
    private void createWorld() {
        blocks = new ArrayList<>();
        
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (worldMap.getMap().get(i).get(j) != null) {
                    if (worldMap.getMap().get(i).get(j) == BlockType.WATER) {
                        blocks.add(new LiquidBlock((i - (int) WIDTH/2) * Block.block_width, j * Block.block_height, worldMap.getMap().get(i).get(j)));
                    } else {
                        blocks.add(new SolidBlock((i - (int) WIDTH/2) * Block.block_width, j * Block.block_height, worldMap.getMap().get(i).get(j)));
                    }
                }
            }
        }
        
        items = new ArrayList<>();
        items.add(new StoredBlock(0,650, ItemType.DIAMOND_ORE));
        
        spawnNPCs();
    }
    
    /**
     * Update all NPCs in the world.
     * 
     * @param player instance of the player
     * @since 1.0
     */
    public void updateNPCs(Player player) {
        for (NPC npc : npcs) {
            if (npc.isKilled()) { continue; }
            npc.update(this, player);
        }
    }
    
    /**
     * Update all liquids around the player. Liquids can expand.
     *
     * @param player instance of player
     * @since 1.0
     */
    public void updateLiquids(Player player) {
        List<Block> newBlocks = new ArrayList<>();
        for (Block block : blocks) {
            if (block.isDestroyed() || block instanceof SolidBlock) { continue; }
            if (!(block.getX() > player.getX()-block.getWidth()*4 && block.getX2() < player.getX2()+block.getWidth()*4)) {
                continue;
            }
            if (block instanceof LiquidBlock) {
                LiquidBlock lBlock = (LiquidBlock) block;
                for (Block newBlock : lBlock.expand(this)) {
                    newBlocks.add(newBlock);
                }
            }
        }
        for (Block newBlock : newBlocks) {
            blocks.add(newBlock);
        }
    }

    /**
     * @return list of all blocks in the world
     * @since 1.0
     */
    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * @return list of all items in the world
     * @since 1.0
     */
    public List<Item> getItems() {
        return items;
    }
    
    /**
     * @return list of all NPCs in the world
     * @since 1.0
     */
    public List<NPC> getNpcs() {
        return npcs;
    }
    
    /**
     * @return instance of WorldMap
     * @since 1.0
     */
    public WorldMap getWorldMap() {
        return worldMap;
    }
    
    /**
     * @return width of the world in blocks
     * @since 1.0
     */
    public int getWIDTH() {
        return WIDTH;
    }
    
}
