package com.krishivstudios.blueprintmaster;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlueprintMaster implements ModInitializer {
    public static final String MOD_ID = "blueprintmaster";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("=========================================");
        LOGGER.info(" 🏗️ BlueprintMaster v1.0.0 Initialized!");
        LOGGER.info(" Author: Krylo_plays (Krishiv Studios)");
        LOGGER.info(" 3D Holograms, Fair-Play & Builder Suite");
        LOGGER.info("=========================================");
    }
}
