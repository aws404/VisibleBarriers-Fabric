package com.aws404.visiblebarriers.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.GameMode;

public class ConfigEntry {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public final String name;
    public boolean value;
    private final boolean requiresCreative;
    public final ConfigEntryCallbackChange callback;

    public ConfigEntry(String name, boolean defaultValue, boolean requiresCreative, ConfigEntryCallbackChange callback) {
        this.name = name;
        this.value = defaultValue;
        this.requiresCreative = requiresCreative;
        this.callback = callback;
    }

    public void toggle() {
        setValue(!value);
    }

    public void setValue(boolean value) {
        this.value = value;

        SystemToast.show(client.getToastManager(), SystemToast.Type.WORLD_BACKUP, new TranslatableText("setting.visiblebarriers." + name), new TranslatableText("setting.visiblebarriers.toggle", new TranslatableText("setting.visiblebarriers." + name + "." + value)));

        if (callback != null)
            callback.onChanged(value);
    }

    public boolean getValue() {
        if (requiresCreative && !(client.interactionManager.getCurrentGameMode() == GameMode.SPECTATOR || client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE)) {
            return false;
        }

        return value;
    }

    public interface ConfigEntryCallbackChange {
        void onChanged(boolean value);
    }

}
