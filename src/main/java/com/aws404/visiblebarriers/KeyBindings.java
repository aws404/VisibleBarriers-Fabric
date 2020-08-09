package com.aws404.visiblebarriers;

import com.aws404.visiblebarriers.devicemenu.WynnDeviceGUI;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private static KeyBinding TOGGLE_BARRIER_MODE = new KeyBinding("key.visiblebarriers.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "key.categories.creative");
    private static KeyBinding OPEN_DEVICE_SCREEN = new KeyBinding("key.visiblebarriers.device_screen", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.categories.creative");

    /**
     * Registers the keybindings to the client and sets their actions
     */
    public static void registerKeyBindings() {
        KeyBindingHelper.registerKeyBinding(TOGGLE_BARRIER_MODE);
        KeyBindingHelper.registerKeyBinding(OPEN_DEVICE_SCREEN);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null)
                return;
            
            while (TOGGLE_BARRIER_MODE.wasPressed()) {
                VisibleBarriers.toggleBarrierMode();
            }

            while (OPEN_DEVICE_SCREEN.wasPressed()) {
                if (client.player.abilities.creativeMode)
                    client.openScreen(new WynnDeviceGUI());
            }
        });
    }

}
