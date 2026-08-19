package com.krishivstudios.blueprintmaster;

import com.krishivstudios.blueprintmaster.gui.BlueprintScreen;
import com.krishivstudios.blueprintmaster.gui.MaterialListOverlay;
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

        // In-World 3D Hologram Renderer Hook
        WorldRenderEvents.LAST.register(context -> {
            if (context.matrixStack() != null && context.consumers() != null && context.camera() != null) {
                HologramRenderer.render(context.matrixStack(), context.consumers(), context.camera().getPos());
            }
        });

        // Keybind & Client Tick Dispatcher
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (blueprintMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new BlueprintScreen());
                }
            }

            while (materialListKey.wasPressed()) {
                MaterialListOverlay.toggleVisible();
            }

            while (toggleHologramKey.wasPressed()) {
                HologramRenderer.toggleVisible();
            }
        });
    }
}
