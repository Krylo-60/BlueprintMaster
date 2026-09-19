package com.krishivstudios.blueprintmaster.feature;

import com.krishivstudios.blueprintmaster.model.Blueprint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Method;
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
            try {
                Object inv = client.player.getInventory();
                if (inv != null) {
                    Method sizeMethod = inv.getClass().getMethod("size");
                    Method getStackMethod = inv.getClass().getMethod("getStack", int.class);
                    int size = (int) sizeMethod.invoke(inv);
                    for (int i = 0; i < size; i++) {
                        ItemStack stack = (ItemStack) getStackMethod.invoke(inv, i);
                        if (stack != null && !stack.isEmpty()) {
                            Item item = stack.getItem();
                            inventoryCounts.put(item, inventoryCounts.getOrDefault(item, 0) + stack.getCount());
                        }
                    }
                }
            } catch (Throwable t) {
                // Fallback to hand/armor items
                for (ItemStack stack : client.player.getHandItems()) {
                    if (stack != null && !stack.isEmpty()) {
                        inventoryCounts.put(stack.getItem(), inventoryCounts.getOrDefault(stack.getItem(), 0) + stack.getCount());
                    }
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

        result.sort((a, b) -> {
            if (a.isSatisfied() != b.isSatisfied()) {
                return a.isSatisfied() ? 1 : -1;
            }
            return a.item.getName().getString().compareToIgnoreCase(b.item.getName().getString());
        });

        return result;
    }
}
