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
import cz.cvut.fel.pjv.maps.Structure;
import cz.cvut.fel.pjv.maps.WorldMap;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Contains all entities in the world.
 * Takes the world worldMap (2D ArrayList) as argument.
 * 
 * @author Zdenek
 * @version 1.1
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
    
    private double playerSpawnX;
    
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
        playerSpawnX = worldMap.getPlayerX();
    }
    
    private void spawnNPCs() {
        npcs = new ArrayList<>();
        
        for (Cave cave : worldMap.getCaves()) {
            double enemyX = (cave.getSpawner().getX() - (worldMap.getWIDTH() / 2)) * Block.block_width;
            double enemyY = (cave.getSpawner().getY() - 1) * Block.block_height;
            npcs.add(new Enemy(enemyX, enemyY, CreatureType.SKELETON, this));
        }
        
        for (Map.Entry<String, Structure> structure : worldMap.getStructures().entrySet()) {
            
            if (structure.getValue().getSpawner() != null) {
                double friendX = (structure.getValue().getSpawner().getX() - (worldMap.getWIDTH() / 2)) * Block.block_width;
                double friendY = (structure.getValue().getSpawner().getY() - 1) * Block.block_height;
                npcs.add(new Friend(friendX, friendY, CreatureType.MONK));
            }
        }
        
        npcs.add(new Enemy(32, 0, CreatureType.BOSS, this));
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
     * Add item to the world.
     *
     * @param itemToAdd instance of item to add into the world
     * @since 1.1
     */
    public void addItem(Item itemToAdd) {
        items.add(itemToAdd);
    }
    
    /**
     * Update all items that are laying in the world (they are not picked by any creature).
     *
     * @since 1.1
     */
    public void updateLayingItems() {
        for (Item item : items) {
            if (item.isPicked()) { continue; }
            item.setY(item.getY() + item.getVelocityY());
            Collision.preventItemCollision(item, this);
        }
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
     * @param x pixel X coordinate of the blocks column
     * @return pixel Y coordinate of the highest block in the given column of blocks
     * @since 1.1
     */
    public double getHighestBlockY(double x) {
        double yRet = 0;
        for (Block block : getBlockColumn(x)) {
            if (block.getY() < yRet) { yRet = block.getY(); }
        }
        return yRet;
    }
    
    private List<Block> getBlockColumn(double x) {
        List<Block> blockColumn = new ArrayList<>();
        for (Block block : blocks) {
            if ((int) block.getX() != (int) x) { continue; }
            blockColumn.add(block);
        }
        return blockColumn;
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
     * @return instance of the world map
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

    /**
     * @return X coordinate of the player's spawn point in pixels
     * @since 1.1
     */
    public double getPlayerSpawnX() {
        return playerSpawnX;
    }

    /**
     * Set X coordinate of the player's spawn point.
     *
     * @param playerSpawnX pixel X coordinate of the player's spawn point
     * @since 1.1
     */
    public void setPlayerSpawnX(double playerSpawnX) {
        this.playerSpawnX = playerSpawnX;
    }
    
}
