package com.krishivstudios.blueprintmaster.model;

import net.minecraft.block.Blocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlueprintLibrary {
    private static final List<Blueprint> BLUEPRINTS = new ArrayList<>();

    static {
        registerStarterBlueprints();
    }

    public static List<Blueprint> getBlueprints() {
        return Collections.unmodifiableList(BLUEPRINTS);
    }

    private static void registerStarterBlueprints() {
        // 1. 🏡 Cozy Survival Cottage (7x5x7)
        Blueprint cottage = new Blueprint("starter_cottage", "Cozy Survival Cottage", "Krylo_plays", "Residential", 7, 5, 7);
        for (int x = 0; x < 7; x++) {
            for (int z = 0; z < 7; z++) {
                cottage.setBlock(x, 0, z, Blocks.COBBLESTONE);
                // Walls
                if (x == 0 || x == 6 || z == 0 || z == 6) {
                    for (int y = 1; y < 4; y++) {
                        if ((x == 0 && z == 0) || (x == 6 && z == 0) || (x == 0 && z == 6) || (x == 6 && z == 6)) {
                            cottage.setBlock(x, y, z, Blocks.OAK_LOG);
                        } else if (y == 2 && ((x == 3 && (z == 0 || z == 6)) || (z == 3 && (x == 0 || x == 6)))) {
                            cottage.setBlock(x, y, z, Blocks.GLASS_PANE);
                        } else if (x == 3 && z == 0 && y == 1) {
                            cottage.setBlock(x, y, z, Blocks.OAK_DOOR);
                        } else {
                            cottage.setBlock(x, y, z, Blocks.OAK_PLANKS);
                        }
                    }
                }
                // Roof
                cottage.setBlock(x, 4, z, Blocks.OAK_PLANKS);
            }
        }
        BLUEPRINTS.add(cottage);

        // 2. 🌾 Compact Auto-Crop Farm (9x3x9)
        Blueprint farm = new Blueprint("crop_farm", "Compact 9x9 Auto-Crop Farm", "Krishiv Studios", "Farming", 9, 3, 9);
        for (int x = 0; x < 9; x++) {
            for (int z = 0; z < 9; z++) {
                if (x == 0 || x == 8 || z == 0 || z == 8) {
                    farm.setBlock(x, 0, z, Blocks.OAK_LOG);
                    farm.setBlock(x, 1, z, Blocks.OAK_FENCE);
                } else if (x == 4 && z == 4) {
                    farm.setBlock(x, 0, z, Blocks.WATER);
                    farm.setBlock(x, 1, z, Blocks.OAK_SLAB);
                    farm.setBlock(x, 2, z, Blocks.LANTERN);
                } else {
                    farm.setBlock(x, 0, z, Blocks.FARMLAND);
                    farm.setBlock(x, 1, z, Blocks.WHEAT);
                }
            }
        }
        BLUEPRINTS.add(farm);

        // 3. 🗼 Nether Portal Ancient Shrine (5x6x3)
        Blueprint portal = new Blueprint("portal_shrine", "Nether Portal Ancient Shrine", "Krylo_plays", "Mystical", 5, 6, 3);
        // Obsidian Frame
        for (int x = 1; x <= 3; x++) {
            portal.setBlock(x, 1, 1, Blocks.OBSIDIAN);
            portal.setBlock(x, 5, 1, Blocks.OBSIDIAN);
        }
        for (int y = 2; y <= 4; y++) {
            portal.setBlock(1, y, 1, Blocks.OBSIDIAN);
            portal.setBlock(3, y, 1, Blocks.OBSIDIAN);
        }
        // Shrine Accents
        portal.setBlock(0, 1, 1, Blocks.CRYING_OBSIDIAN);
        portal.setBlock(4, 1, 1, Blocks.CRYING_OBSIDIAN);
        portal.setBlock(0, 2, 1, Blocks.GILDED_BLACKSTONE);
        portal.setBlock(4, 2, 1, Blocks.GILDED_BLACKSTONE);
        portal.setBlock(0, 0, 1, Blocks.POLISHED_BLACKSTONE_BRICKS);
        portal.setBlock(4, 0, 1, Blocks.POLISHED_BLACKSTONE_BRICKS);
        portal.setBlock(2, 0, 0, Blocks.GOLD_BLOCK);
        BLUEPRINTS.add(portal);

        // 4. 🛡️ Medieval Watchtower (5x9x5)
        Blueprint tower = new Blueprint("watchtower", "Medieval Defense Watchtower", "Krishiv Studios", "Defense", 5, 9, 5);
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 5; x++) {
                for (int z = 0; z < 5; z++) {
                    if (x == 0 || x == 4 || z == 0 || z == 4) {
                        if (y % 2 == 0) {
                            tower.setBlock(x, y, z, Blocks.STONE_BRICKS);
                        } else {
                            tower.setBlock(x, y, z, Blocks.COBBLESTONE);
                        }
                    }
                }
            }
        }
        // Top platform & Battlements
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                tower.setBlock(x, 7, z, Blocks.OAK_PLANKS);
                if ((x == 0 || x == 4 || z == 0 || z == 4) && (x + z) % 2 == 0) {
                    tower.setBlock(x, 8, z, Blocks.COBBLESTONE_WALL);
                }
            }
        }
        tower.setBlock(2, 8, 2, Blocks.LANTERN);
        BLUEPRINTS.add(tower);
    }
}
