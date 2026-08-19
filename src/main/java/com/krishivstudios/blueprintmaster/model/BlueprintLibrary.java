package com.krishivstudios.blueprintmaster.model;

import net.minecraft.block.Blocks;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlueprintLibrary {
    private static final List<Blueprint> blueprints = new ArrayList<>();

    static {
        loadStarterBlueprints();
    }

    private static void loadStarterBlueprints() {
        // 1. Cozy Survival Cottage (7x5x7)
        Blueprint cottage = new Blueprint("starter_cottage", "Cozy Survival Cottage", "Krylo_plays", "Residential", 7, 5, 7);
        for (int x = 0; x < 7; x++) {
            for (int z = 0; z < 7; z++) {
                cottage.setBlock(x, 0, z, Blocks.COBBLESTONE);
            }
        }
        for (int y = 1; y < 4; y++) {
            cottage.setBlock(0, y, 0, Blocks.OAK_LOG);
            cottage.setBlock(6, y, 0, Blocks.OAK_LOG);
            cottage.setBlock(0, y, 6, Blocks.OAK_LOG);
            cottage.setBlock(6, y, 6, Blocks.OAK_LOG);

            for (int x = 1; x < 6; x++) {
                cottage.setBlock(x, y, 0, (x == 2 || x == 4) ? Blocks.GLASS_PANE : Blocks.OAK_PLANKS);
                cottage.setBlock(x, y, 6, Blocks.OAK_PLANKS);
            }
            for (int z = 1; z < 6; z++) {
                cottage.setBlock(0, y, z, (z == 3) ? Blocks.GLASS_PANE : Blocks.OAK_PLANKS);
                cottage.setBlock(6, y, z, (z == 3) ? Blocks.GLASS_PANE : Blocks.OAK_PLANKS);
            }
        }
        cottage.setBlock(3, 1, 0, Blocks.OAK_DOOR);
        for (int x = 0; x < 7; x++) {
            for (int z = 0; z < 7; z++) {
                cottage.setBlock(x, 4, z, Blocks.OAK_PLANKS);
            }
        }
        blueprints.add(cottage);

        // 2. Compact 9x9 Auto-Crop Farm (9x3x9)
        Blueprint farm = new Blueprint("crop_farm", "Compact 9x9 Crop Farm", "Krylo_plays", "Farming", 9, 3, 9);
        for (int x = 0; x < 9; x++) {
            for (int z = 0; z < 9; z++) {
                if (x == 4 && z == 4) {
                    farm.setBlock(x, 0, z, Blocks.WATER);
                    farm.setBlock(x, 1, z, Blocks.OAK_SLAB);
                    farm.setBlock(x, 2, z, Blocks.LANTERN);
                } else {
                    farm.setBlock(x, 0, z, Blocks.FARMLAND);
                    farm.setBlock(x, 1, z, Blocks.WHEAT);
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            farm.setBlock(i, 1, 0, Blocks.OAK_FENCE);
            farm.setBlock(i, 1, 8, Blocks.OAK_FENCE);
            farm.setBlock(0, 1, i, Blocks.OAK_FENCE);
            farm.setBlock(8, 1, i, Blocks.OAK_FENCE);
        }
        blueprints.add(farm);

        // 3. Nether Portal Ancient Shrine (5x6x3)
        Blueprint shrine = new Blueprint("portal_shrine", "Nether Portal Shrine", "Krylo_plays", "Mystical", 5, 6, 3);
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 3; z++) {
                shrine.setBlock(x, 0, z, Blocks.CRYING_OBSIDIAN);
            }
        }
        shrine.setBlock(1, 1, 1, Blocks.OBSIDIAN);
        shrine.setBlock(2, 1, 1, Blocks.OBSIDIAN);
        shrine.setBlock(3, 1, 1, Blocks.OBSIDIAN);
        shrine.setBlock(1, 2, 1, Blocks.OBSIDIAN);
        shrine.setBlock(3, 2, 1, Blocks.OBSIDIAN);
        shrine.setBlock(1, 3, 1, Blocks.OBSIDIAN);
        shrine.setBlock(3, 3, 1, Blocks.OBSIDIAN);
        shrine.setBlock(1, 4, 1, Blocks.OBSIDIAN);
        shrine.setBlock(2, 4, 1, Blocks.OBSIDIAN);
        shrine.setBlock(3, 4, 1, Blocks.OBSIDIAN);
        shrine.setBlock(0, 1, 1, Blocks.GOLD_BLOCK);
        shrine.setBlock(4, 1, 1, Blocks.GOLD_BLOCK);
        shrine.setBlock(0, 2, 1, Blocks.LANTERN);
        shrine.setBlock(4, 2, 1, Blocks.LANTERN);
        blueprints.add(shrine);

        // 4. Medieval Defense Watchtower (5x9x5)
        Blueprint tower = new Blueprint("watchtower", "Medieval Watchtower", "Krylo_plays", "Defense", 5, 9, 5);
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 5; x++) {
                for (int z = 0; z < 5; z++) {
                    if (x == 0 || x == 4 || z == 0 || z == 4) {
                        tower.setBlock(x, y, z, (y % 2 == 0) ? Blocks.STONE_BRICKS : Blocks.COBBLESTONE);
                    }
                }
            }
        }
        tower.setBlock(2, 1, 0, Blocks.AIR);
        tower.setBlock(2, 2, 0, Blocks.AIR);
        blueprints.add(tower);

        // 5. Medieval Castle Gate & Fortified Wall (11x8x5) [NEW in v1.1.0]
        Blueprint castleGate = new Blueprint("castle_gate", "Medieval Castle Gate & Wall", "Krylo_plays", "Defense", 11, 8, 5);
        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 8; y++) {
                for (int z = 0; z < 5; z++) {
                    if ((x < 3 || x > 7) && (z == 0 || z == 4)) {
                        castleGate.setBlock(x, y, z, Blocks.STONE_BRICKS);
                    } else if (z == 2 && y >= 4) {
                        castleGate.setBlock(x, y, z, Blocks.STONE_BRICKS);
                    }
                }
            }
        }
        for (int x = 3; x <= 7; x++) {
            for (int y = 1; y <= 4; y++) {
                castleGate.setBlock(x, y, 2, Blocks.IRON_BARS);
            }
        }
        castleGate.setBlock(1, 8, 1, Blocks.LANTERN);
        castleGate.setBlock(9, 8, 1, Blocks.LANTERN);
        blueprints.add(castleGate);

        // 6. Netherite Beacon Pyramid Temple (9x7x9) [NEW in v1.1.0]
        Blueprint beaconTemple = new Blueprint("beacon_temple", "Beacon Pyramid Temple", "Krylo_plays", "Mystical", 9, 7, 9);
        for (int layer = 0; layer < 4; layer++) {
            int span = 9 - (layer * 2);
            int start = layer;
            for (int x = start; x < start + span; x++) {
                for (int z = start; z < start + span; z++) {
                    beaconTemple.setBlock(x, layer, z, (layer == 3) ? Blocks.NETHERITE_BLOCK : Blocks.DIAMOND_BLOCK);
                }
            }
        }
        beaconTemple.setBlock(4, 4, 4, Blocks.BEACON);
        blueprints.add(beaconTemple);

        // 7. Modern Quartz & Glass Villa (9x6x9) [NEW in v1.1.0]
        Blueprint villa = new Blueprint("modern_villa", "Modern Quartz Villa", "Krylo_plays", "Modern", 9, 6, 9);
        for (int x = 0; x < 9; x++) {
            for (int z = 0; z < 9; z++) {
                villa.setBlock(x, 0, z, Blocks.SMOOTH_QUARTZ);
                villa.setBlock(x, 5, z, Blocks.SMOOTH_QUARTZ);
            }
        }
        for (int y = 1; y < 5; y++) {
            for (int x = 0; x < 9; x++) {
                villa.setBlock(x, y, 0, (x % 2 == 0) ? Blocks.TINTED_GLASS : Blocks.SMOOTH_QUARTZ);
                villa.setBlock(x, y, 8, Blocks.SMOOTH_QUARTZ);
            }
            for (int z = 0; z < 9; z++) {
                villa.setBlock(0, y, z, Blocks.SMOOTH_QUARTZ);
                villa.setBlock(8, y, z, (z % 2 == 0) ? Blocks.TINTED_GLASS : Blocks.SMOOTH_QUARTZ);
            }
        }
        blueprints.add(villa);
    }

    public static List<Blueprint> getBlueprints() {
        return Collections.unmodifiableList(blueprints);
    }

    public static void addBlueprint(Blueprint blueprint) {
        blueprints.add(blueprint);
    }
}
