package com.aws404.visiblebarriers;

import com.aws404.visiblebarriers.config.ConfigManager;
import com.aws404.visiblebarriers.config.SettingsMenu;
import com.aws404.visiblebarriers.devices.WynnDeviceGUI;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private static final KeyBinding OPEN_DEVICE_SCREEN = new KeyBinding("key.visiblebarriers.device_screen", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.categories.creative");
    private static final KeyBinding OPEN_CONFIG_SCREEN = new KeyBinding("key.visiblebarriers.config_screen", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "key.categories.creative");

    private static final KeyBinding TOGGLE_BARRIER_MODE = new KeyBinding("key.visiblebarriers.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "key.categories.creative");
    private static final KeyBinding TOGGLE_STRUCTURE_BLOCK_NAME = new KeyBinding("key.visiblebarriers.structure_block_names", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.categories.creative");

    /**
     * Registers the keybindings to the client and sets their actions
     */
    public static void registerKeyBindings() {
        KeyBindingHelper.registerKeyBinding(OPEN_DEVICE_SCREEN);
        KeyBindingHelper.registerKeyBinding(OPEN_CONFIG_SCREEN);

        KeyBindingHelper.registerKeyBinding(TOGGLE_BARRIER_MODE);
        KeyBindingHelper.registerKeyBinding(TOGGLE_STRUCTURE_BLOCK_NAME);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null)
                return;
            
            while (TOGGLE_BARRIER_MODE.wasPressed()) {
                ConfigManager.TECHNICAL_VISIBILITY.toggle();
            }

            while (TOGGLE_STRUCTURE_BLOCK_NAME.wasPressed()) {
                ConfigManager.CONSTANT_STRUCTURE_BLOCK_NAME.toggle();
            }

            while (OPEN_DEVICE_SCREEN.wasPressed()) {
                if (client.player.abilities.creativeMode)
                    client.openScreen(new WynnDeviceGUI());
            }

            while (OPEN_CONFIG_SCREEN.wasPressed()) {
                client.openScreen(new SettingsMenu());
            }
        });
    }

}
