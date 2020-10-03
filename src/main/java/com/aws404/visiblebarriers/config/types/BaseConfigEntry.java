package com.aws404.visiblebarriers.config.types;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.GameMode;

public abstract class BaseConfigEntry<T> {
    protected static final MinecraftClient client = MinecraftClient.getInstance();

    public final String name;
    protected T value;
    public final T defaultValue;
    public final boolean requiresCreative;
    protected final ConfigEntryCallbackChange<T> callback;

    public BaseConfigEntry(String name, T defaultValue, boolean requiresCreative, ConfigEntryCallbackChange<T> callback) {
        this.name = name;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.requiresCreative = requiresCreative;
        this.callback = callback;
    }

    public abstract void setValue(String value);

    public void setValue(T value) {
        this.value = value;

        SystemToast.show(client.getToastManager(), SystemToast.Type.WORLD_BACKUP, new TranslatableText("setting.visiblebarriers." + name), new TranslatableText("setting.visiblebarriers.toggle", new TranslatableText("setting.visiblebarriers." + name + "." + value)));

        if (callback != null)
            callback.onChanged(value);
    }

    public T getValue() {
        if (requiresCreative && !(client.interactionManager.getCurrentGameMode() == GameMode.SPECTATOR || client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE)) {
            return defaultValue;
        }

        return value;
    }

    public T getRawValue() {
        return value;
    }

    public abstract void cycle();

    public abstract Text getMenuText();

    public abstract Text getMenuHoverText();

    public interface ConfigEntryCallbackChange<T> {
        void onChanged(T value);
    }


}
