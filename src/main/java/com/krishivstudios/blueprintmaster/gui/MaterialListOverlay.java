package com.krishivstudios.blueprintmaster.gui;

import com.krishivstudios.blueprintmaster.feature.MaterialCalculator;
import com.krishivstudios.blueprintmaster.model.Blueprint;
import com.krishivstudios.blueprintmaster.render.HologramRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class MaterialListOverlay {
    private static boolean visible = false;

    public static boolean isVisible() { return visible; }
    public static void setVisible(boolean v) { visible = v; }
    public static void toggleVisible() { visible = !visible; }

    public static void render(DrawContext context) {
        if (!visible) return;

        Blueprint active = HologramRenderer.getActiveBlueprint();
        if (active == null) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.textRenderer == null) return;

        List<MaterialCalculator.MaterialRequirement> reqs = MaterialCalculator.calculate(active);
        if (reqs.isEmpty()) return;

        int startX = 10;
        int startY = 10;
        int bgWidth = 160;
        int bgHeight = Math.min(180, 20 + reqs.size() * 12);

        // Glass HUD Box
        context.fill(startX, startY, startX + bgWidth, startY + bgHeight, 0x88000000);
        context.drawTextWithShadow(client.textRenderer, "§b§l📋 Required Materials", startX + 6, startY + 6, 0xFFFFFF);

        int lineY = startY + 20;
        int count = 0;
        for (MaterialCalculator.MaterialRequirement req : reqs) {
            if (count >= 12) {
                context.drawTextWithShadow(client.textRenderer, "§7... and " + (reqs.size() - count) + " more", startX + 6, lineY, 0xAAAAAA);
                break;
            }

            String color = req.isSatisfied() ? "§a✔ " : "§c✖ ";
            String itemName = req.item.getName().getString();
            if (itemName.length() > 14) itemName = itemName.substring(0, 12) + "..";

            String text = color + "§f" + itemName + ": §e" + req.available + "§7/§f" + req.required;
            context.drawTextWithShadow(client.textRenderer, text, startX + 6, lineY, 0xFFFFFF);
            lineY += 12;
            count++;
        }
    }
}
