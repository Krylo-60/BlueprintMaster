package com.krishivstudios.blueprintmaster.feature;

import com.krishivstudios.blueprintmaster.model.Blueprint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

public class MaterialCalculator {

    public static class MaterialRequirement {
        public final Item item;
        public final int required;
        public final int available;

        public MaterialRequirement(Item item, int required, int available) {
            this.item = item;
            this.required = required;
            this.available = available;
        }

        public boolean isSatisfied() {
            return available >= required;
        }

        public int getMissing() {
            return Math.max(0, required - available);
        }
    }

    public static List<MaterialRequirement> calculate(Blueprint blueprint) {
        if (blueprint == null) return Collections.emptyList();

        Map<Item, Integer> needed = blueprint.getRequiredMaterials();
        Map<Item, Integer> inventoryCounts = new HashMap<>();

        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null) {
            PlayerInventory inv = client.player.getInventory();
            for (int i = 0; i < inv.size(); i++) {
                ItemStack stack = inv.getStack(i);
                if (!stack.isEmpty()) {
                    Item item = stack.getItem();
                    inventoryCounts.put(item, inventoryCounts.getOrDefault(item, 0) + stack.getCount());
                }
            }
        }

        List<MaterialRequirement> result = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : needed.entrySet()) {
            Item item = entry.getKey();
            int required = entry.getValue();
            int available = inventoryCounts.getOrDefault(item, 0);
            result.add(new MaterialRequirement(item, required, available));
        }

        // Sort missing first, then alphabetical
        result.sort((a, b) -> {
            if (a.isSatisfied() != b.isSatisfied()) {
                return a.isSatisfied() ? 1 : -1;
            }
            return a.item.getName().getString().compareToIgnoreCase(b.item.getName().getString());
        });

        return result;
    }
}
