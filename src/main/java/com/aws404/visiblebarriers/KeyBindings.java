package com.aws404.visiblebarriers;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private static KeyBinding TOGGLE_BARRIER_MODE = new KeyBinding("key.visiblebarriers.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "key.categories.creative");

    /**
     * Registers the keybinding to the client and sets its action to toggle the barrier mode
     */
    public static void registerKeyBindings() {
        KeyBindingHelper.registerKeyBinding(TOGGLE_BARRIER_MODE);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null)
                return;
            
            while (TOGGLE_BARRIER_MODE.wasPressed()) {
                VisibleBarriers.toggleBarrierMode();
            }
        });
    }

}
