package com.krishivstudios.blueprintmaster;

import com.krishivstudios.blueprintmaster.render.HologramRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class BlueprintMasterClient implements ClientModInitializer {
    public static KeyBinding blueprintMenuKey;
    public static KeyBinding materialListKey;
    public static KeyBinding toggleHologramKey;

    @Override
    public void onInitializeClient() {
        try {
            blueprintMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.blueprintmaster.menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                "category.blueprintmaster"
            ));

            materialListKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.blueprintmaster.materials",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "category.blueprintmaster"
            ));

            toggleHologramKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.blueprintmaster.toggle_hologram",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.blueprintmaster"
            ));

            // World Render Event
            WorldRenderEvents.LAST.register(context -> {
                try {
                    if (context.matrixStack() != null && context.consumers() != null && context.camera() != null) {
                        HologramRenderer.render(context.matrixStack(), context.consumers(), context.camera().getPos());
                    }
                } catch (Throwable ignored) {}
            });

            // Client Tick Event
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                try {
                    while (blueprintMenuKey != null && blueprintMenuKey.wasPressed()) {
                        try {
                            if (client.currentScreen == null) {
                                Class<?> screenClass = Class.forName("com.krishivstudios.blueprintmaster.gui.BlueprintScreen");
                                Object screen = screenClass.getDeclaredConstructor().newInstance();
                                client.setScreen((net.minecraft.client.gui.screen.Screen) screen);
                            }
                        } catch (Throwable t) {
                            HologramRenderer.placeAtPlayerLook();
                            HologramRenderer.cycleColor();
                        }
                    }

                    while (materialListKey != null && materialListKey.wasPressed()) {
                        try {
                            Class<?> matClass = Class.forName("com.krishivstudios.blueprintmaster.gui.MaterialListOverlay");
                            matClass.getMethod("toggleVisible").invoke(null);
                        } catch (Throwable ignored) {}
                    }

                    while (toggleHologramKey != null && toggleHologramKey.wasPressed()) {
                        HologramRenderer.toggleVisible();
                    }
                } catch (Throwable ignored) {}
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
