package com.aws404.visiblebarriers;

import com.aws404.visiblebarriers.armorstandtools.interactionscreen.InteractionScreen;
import com.aws404.visiblebarriers.config.categories.StructureBlockToolsCategory;
import com.aws404.visiblebarriers.config.categories.TechnicalVisibilityConfigCategory;
import com.aws404.visiblebarriers.config.menu.SettingsMenu;
import com.aws404.visiblebarriers.devices.WynnDeviceGUI;
import com.aws404.visiblebarriers.util.VersionUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.TranslatableText;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class KeyBindings {
    private static final KeyBinding OPEN_DEVICE_SCREEN = new KeyBinding("key.visiblebarriers.device_screen", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.categories.creative");
    private static final KeyBinding OPEN_CONFIG_SCREEN = new KeyBinding("key.visiblebarriers.config_screen", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "key.categories.creative");
    private static final KeyBinding OPEN_ARMOR_STAND_SELECTOR_SCREEN = new KeyBinding("key.visiblebarriers.open_armor_stand_selector_screen", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "key.categories.creative");
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
        KeyBindingHelper.registerKeyBinding(OPEN_ARMOR_STAND_SELECTOR_SCREEN);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null)
                return;
            
            while (TOGGLE_BARRIER_MODE.wasPressed()) {
                TechnicalVisibilityConfigCategory.MASTER_SWITCH.cycle();
            }

            while (TOGGLE_STRUCTURE_BLOCK_NAME.wasPressed()) {
                StructureBlockToolsCategory.PHYSICAL_NAME_DISPLAY.cycle();
            }

            while (OPEN_DEVICE_SCREEN.wasPressed()) {
                if (client.player.abilities.creativeMode)
                    client.openScreen(new WynnDeviceGUI());
            }

            while (OPEN_CONFIG_SCREEN.wasPressed()) {
                client.openScreen(new SettingsMenu());
            }

            while (OPEN_ARMOR_STAND_SELECTOR_SCREEN.wasPressed()) {
                List<ArmorStandEntity> list = client.world.getEntities(ArmorStandEntity.class, client.player.getVisibilityBoundingBox().expand(3, 3, 3), null);
                if (list.size() > 0) {
                    client.openScreen(new InteractionScreen(list));
                } else {
                    VersionUtils.sendMessage(new TranslatableText("message.visiblebarriers.no_stands"));
                }
            }
        });
    }

}
