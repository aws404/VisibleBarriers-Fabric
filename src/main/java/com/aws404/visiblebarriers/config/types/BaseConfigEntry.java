package com.aws404.visiblebarriers.config.types;

import com.aws404.visiblebarriers.config.menu.SettingsListWidget;
import com.aws404.visiblebarriers.config.menu.SettingsListWidgetEntry;
import com.aws404.visiblebarriers.config.menu.SettingsMenu;
import com.aws404.visiblebarriers.config.menu.SettingsToast;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

import java.util.function.Consumer;

public abstract class BaseConfigEntry<T> {
    protected static final MinecraftClient client = MinecraftClient.getInstance();
    protected static final ToastManager toasts = client.getToastManager();

    public final String name;
    protected T value;
    public final T defaultValue;
    public final boolean requiresCreative;
    protected final Consumer<T> callback;

    public BaseConfigEntry(String name, T defaultValue, boolean requiresCreative, Consumer<T> callback) {
        this.name = name;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.requiresCreative = requiresCreative;
        this.callback = callback;
    }

    public abstract void setValue(String value);

    public void setValue(T value) {
        this.value = value;

        if (!(client.currentScreen instanceof SettingsMenu)) {
            SettingsToast oldToast = toasts.getToast(SettingsToast.class, this);
            if (oldToast != null) {
                oldToast.update(client);
            } else {
                toasts.add(new SettingsToast(client, this));
            }
        }

        if (callback != null)
            callback.accept(value);
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

    public abstract Text getValueText();

    public abstract SettingsListWidgetEntry getSettingsListEntry(SettingsListWidget parent);
}
