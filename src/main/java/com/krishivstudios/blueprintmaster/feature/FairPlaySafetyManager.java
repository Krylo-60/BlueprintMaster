package com.krishivstudios.blueprintmaster.feature;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class FairPlaySafetyManager {

    public static boolean isSoloSingleplayer() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return false;

        // If playing on a remote server or realms -> Multiplayer
        if (client.getCurrentServerEntry() != null) {
            return false;
        }

        // If local singleplayer, check if LAN server has other players
        if (client.isInSingleplayer() && client.getServer() != null) {
            return client.getServer().getCurrentPlayerCount() <= 1;
        }

        return false;
    }

    public static boolean isAutoPlaceAllowed() {
        return isSoloSingleplayer();
    }

    public static void notifyMultiplayerFairPlay() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null) {
            client.player.sendMessage(
                Text.literal("§6[BlueprintMaster] §eFair-Play Protection Active: §f3D Holograms & Material Guides are enabled. Auto-placement is disabled on multiplayer servers."),
                false
            );
        }
    }
}
