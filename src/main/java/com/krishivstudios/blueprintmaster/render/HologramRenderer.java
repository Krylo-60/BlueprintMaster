package com.krishivstudios.blueprintmaster.render;

import com.krishivstudios.blueprintmaster.model.Blueprint;
import com.krishivstudios.blueprintmaster.model.BlueprintLibrary;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Map;

public class HologramRenderer {
    private static Blueprint activeBlueprint = null;
    private static BlockPos originPos = null;
    private static boolean visible = true;
    private static float opacity = 0.65f;

    static {
        if (!BlueprintLibrary.getBlueprints().isEmpty()) {
            activeBlueprint = BlueprintLibrary.getBlueprints().get(0);
        }
    }

    public static Blueprint getActiveBlueprint() { return activeBlueprint; }
    public static void setActiveBlueprint(Blueprint blueprint) { activeBlueprint = blueprint; }

    public static BlockPos getOriginPos() { return originPos; }
    public static void setOriginPos(BlockPos pos) { originPos = pos; }

    public static boolean isVisible() { return visible && activeBlueprint != null && originPos != null; }
    public static void setVisible(boolean v) { visible = v; }
    public static void toggleVisible() { visible = !visible; }

    public static float getOpacity() { return opacity; }
    public static void setOpacity(float op) { opacity = Math.max(0.1f, Math.min(1.0f, op)); }

    public static void nudge(int dx, int dy, int dz) {
        if (originPos != null) {
            originPos = originPos.add(dx, dy, dz);
        }
    }

    public static void placeAtPlayerLook() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null) {
            originPos = client.player.getBlockPos().add(client.player.getHorizontalFacing().getVector().multiply(3));
            visible = true;
        }
    }

    public static void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Vec3d cameraPos) {
        if (!isVisible() || activeBlueprint == null || originPos == null) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null) return;

        matrices.push();
        matrices.translate(originPos.getX() - cameraPos.x, originPos.getY() - cameraPos.y, originPos.getZ() - cameraPos.z);

        VertexConsumer lineConsumer = vertexConsumers.getBuffer(RenderLayer.getLines());

        // 1. Draw 3D Blueprint Bounding Box Outline
        int sx = activeBlueprint.getSizeX();
        int sy = activeBlueprint.getSizeY();
        int sz = activeBlueprint.getSizeZ();
        WorldRenderer.drawBox(matrices, lineConsumer, 0, 0, 0, sx, sy, sz, 0.0f, 0.75f, 1.0f, 0.85f);

        matrices.pop();
    }
}
