package com.krishivstudios.blueprintmaster.model;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class Blueprint {
    private final String id;
    private final String displayName;
    private final String author;
    private final String category;
    private int sizeX, sizeY, sizeZ;
    private final Map<BlockPos, BlockState> blocks = new HashMap<>();

    public Blueprint(String id, String displayName, String author, String category, int sizeX, int sizeY, int sizeZ) {
        this.id = id;
        this.displayName = displayName;
        this.author = author;
        this.category = category;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    public void setBlock(int x, int y, int z, BlockState state) {
        blocks.put(new BlockPos(x, y, z), state);
    }

    public void setBlock(int x, int y, int z, Block block) {
        blocks.put(new BlockPos(x, y, z), block.getDefaultState());
    }

    public Map<BlockPos, BlockState> getBlocks() {
        return blocks;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getSizeX() { return sizeX; }
    public int getSizeY() { return sizeY; }
    public int getSizeZ() { return sizeZ; }

    public int getTotalBlocks() {
        int count = 0;
        for (BlockState state : blocks.values()) {
            if (!state.isAir()) count++;
        }
        return count;
    }

    public Map<Item, Integer> getRequiredMaterials() {
        Map<Item, Integer> materials = new HashMap<>();
        for (BlockState state : blocks.values()) {
            if (state.isAir()) continue;
            Item item = state.getBlock().asItem();
            if (item != null && item != Blocks.AIR.asItem()) {
                materials.put(item, materials.getOrDefault(item, 0) + 1);
            }
        }
        return materials;
    }

    public void rotate90() {
        Map<BlockPos, BlockState> rotated = new HashMap<>();
        int oldSizeX = this.sizeX;
        int oldSizeZ = this.sizeZ;

        for (Map.Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
            BlockPos pos = entry.getKey();
            int newX = oldSizeZ - 1 - pos.getZ();
            int newZ = pos.getX();
            rotated.put(new BlockPos(newX, pos.getY(), newZ), entry.getValue());
        }

        this.blocks.clear();
        this.blocks.putAll(rotated);
        this.sizeX = oldSizeZ;
        this.sizeZ = oldSizeX;
    }
}
