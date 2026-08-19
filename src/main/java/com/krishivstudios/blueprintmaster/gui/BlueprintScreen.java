package com.krishivstudios.blueprintmaster.gui;

import com.krishivstudios.blueprintmaster.feature.FairPlaySafetyManager;
import com.krishivstudios.blueprintmaster.feature.MaterialCalculator;
import com.krishivstudios.blueprintmaster.feature.SingleplayerBuilder;
import com.krishivstudios.blueprintmaster.model.Blueprint;
import com.krishivstudios.blueprintmaster.model.BlueprintLibrary;
import com.krishivstudios.blueprintmaster.render.HologramRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;

public class BlueprintScreen extends Screen {
    private int selectedIndex = 0;
    private boolean showMaterials = false;

    public BlueprintScreen() {
        super(Text.literal("BlueprintMaster — Builder's Suite"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // 1. Previous Blueprint
        this.addDrawableChild(ButtonWidget.builder(Text.literal("◀ Prev"), b -> {
            List<Blueprint> list = BlueprintLibrary.getBlueprints();
            if (!list.isEmpty()) {
                selectedIndex = (selectedIndex - 1 + list.size()) % list.size();
                HologramRenderer.setActiveBlueprint(list.get(selectedIndex));
            }
        }).dimensions(centerX - 130, centerY - 55, 60, 20).build());

        // 2. Next Blueprint
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Next ▶"), b -> {
            List<Blueprint> list = BlueprintLibrary.getBlueprints();
            if (!list.isEmpty()) {
                selectedIndex = (selectedIndex + 1) % list.size();
                HologramRenderer.setActiveBlueprint(list.get(selectedIndex));
            }
        }).dimensions(centerX + 70, centerY - 55, 60, 20).build());

        // 3. Place Hologram at Player
        this.addDrawableChild(ButtonWidget.builder(Text.literal("📍 Place Here"), b -> {
            HologramRenderer.placeAtPlayerLook();
            this.close();
        }).dimensions(centerX - 130, centerY - 25, 125, 20).build());

        // 4. Rotate 90°
        this.addDrawableChild(ButtonWidget.builder(Text.literal("🔄 Rotate 90°"), b -> {
            if (HologramRenderer.getActiveBlueprint() != null) {
                HologramRenderer.getActiveBlueprint().rotate90();
            }
        }).dimensions(centerX + 5, centerY - 25, 125, 20).build());

        // 5. Toggle Materials View
        this.addDrawableChild(ButtonWidget.builder(Text.literal("📋 Material List"), b -> {
            showMaterials = !showMaterials;
        }).dimensions(centerX - 130, centerY + 5, 125, 20).build());

        // 6. Easy Build (Solo Singleplayer Only)
        String easyText = FairPlaySafetyManager.isAutoPlaceAllowed()
            ? (SingleplayerBuilder.isEasyBuildEnabled() ? "⚡ Easy-Build: ON" : "⚡ Easy-Build: OFF")
            : "🔒 Easy-Build (Solo Only)";

        this.addDrawableChild(ButtonWidget.builder(Text.literal(easyText), b -> {
            if (FairPlaySafetyManager.isAutoPlaceAllowed()) {
                SingleplayerBuilder.toggleEasyBuild();
                this.clearAndInit();
            } else {
                FairPlaySafetyManager.notifyMultiplayerFairPlay();
            }
        }).dimensions(centerX + 5, centerY + 5, 125, 20).build());

        // 7. Close Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("✔ Done"), b -> {
            this.close();
        }).dimensions(centerX - 60, centerY + 45, 120, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Title Box
        context.drawCenteredTextWithShadow(this.textRenderer, "§b§l🏗️ BlueprintMaster — Structure Planner", centerX, centerY - 95, 0xFFFFFF);

        Blueprint active = HologramRenderer.getActiveBlueprint();
        if (active != null) {
            String name = "§e" + active.getDisplayName() + " §7(" + active.getSizeX() + "x" + active.getSizeY() + "x" + active.getSizeZ() + ")";
            context.drawCenteredTextWithShadow(this.textRenderer, name, centerX, centerY - 75, 0xFFFFFF);
            String info = "§7Category: §f" + active.getCategory() + " §8| §7Total Blocks: §a" + active.getTotalBlocks();
            context.drawCenteredTextWithShadow(this.textRenderer, info, centerX, centerY - 50, 0xAAAAAA);
        }

        // Fair Play Status Badge
        String fairPlayStatus = FairPlaySafetyManager.isAutoPlaceAllowed()
            ? "§a🟢 Singleplayer Solo Mode §7(Easy-Build Active)"
            : "§6🛡️ Multiplayer Fair-Play Active §7(Hologram Guide Only)";
        context.drawCenteredTextWithShadow(this.textRenderer, fairPlayStatus, centerX, centerY + 75, 0xFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }
}
