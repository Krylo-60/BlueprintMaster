package com.krishivstudios.blueprintmaster.feature;

import com.krishivstudios.blueprintmaster.model.Blueprint;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class SingleplayerBuilder {
    private static boolean easyBuildEnabled = false;

    public static boolean isEasyBuildEnabled() {
        return easyBuildEnabled && FairPlaySafetyManager.isAutoPlaceAllowed();
    }

    public static void setEasyBuildEnabled(boolean enabled) {
        if (enabled && !FairPlaySafetyManager.isAutoPlaceAllowed()) {
            FairPlaySafetyManager.notifyMultiplayerFairPlay();
            easyBuildEnabled = false;
            return;
        }
        easyBuildEnabled = enabled;
    }

    public static void toggleEasyBuild() {
        setEasyBuildEnabled(!easyBuildEnabled);
    }

    public static void tryPlaceGhostBlock(Blueprint blueprint, BlockPos origin, BlockPos targetPos) {
        if (!isEasyBuildEnabled() || blueprint == null || origin == null) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || client.interactionManager == null || client.world == null) return;

        BlockPos relPos = targetPos.subtract(origin);
        BlockState requiredState = blueprint.getBlocks().get(relPos);
        if (requiredState == null || requiredState.isAir()) return;

        // Check if player has the matching item in hand
        ItemStack mainHand = client.player.getMainHandStack();
        if (mainHand.getItem() == requiredState.getBlock().asItem()) {
            BlockHitResult hit = new BlockHitResult(
                Vec3d.ofCenter(targetPos),
                Direction.UP,
                targetPos,
                false
            );
            client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hit);
        }
    }
}
