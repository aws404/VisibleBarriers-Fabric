package com.aws404.visiblebarriers.config.categories;

import com.aws404.visiblebarriers.config.ConfigManager;
import com.aws404.visiblebarriers.config.types.BaseConfigEntry;
import com.aws404.visiblebarriers.config.types.BooleanConfigEntry;
import com.aws404.visiblebarriers.config.types.EnumConfigEntry;
import com.aws404.visiblebarriers.config.types.StringConfigEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.HashMap;
import java.util.function.Consumer;

public class BaseConfigCategory {
    private final HashMap<String, BaseConfigEntry<?>> entries = new HashMap<>();
    private final String categoryName;
    private final MinecraftClient client;

    public BaseConfigCategory(MinecraftClient client, String name) {
        this.categoryName = name;
        this.client = client;
    }

    public Text getName() {
        return new TranslatableText("setting.category." + categoryName);
    }

    public HashMap<String, BaseConfigEntry<?>>  getConfigEntries() {
        return entries;
    }

    protected StringConfigEntry registerStringSetting(String name, String defaultValue, Consumer<String> callback) {
        String key = doDefaultSetting(name, defaultValue);

        StringConfigEntry entry;
        try {
            entry = new StringConfigEntry(name, ConfigManager.settingsFile.get(key), callback);
        } catch (ClassCastException e) {
            entry = new StringConfigEntry(name, defaultValue, callback);
        }
        entries.put(key, entry);

        return entry;
    }

    protected BooleanConfigEntry registerBooleanSetting(String name, boolean requiresCreative, Consumer<Boolean> callback) {
        String key = doDefaultSetting(name, "false");

        BooleanConfigEntry entry;
        try {
            entry = new BooleanConfigEntry(name, Boolean.parseBoolean(ConfigManager.settingsFile.get(key)), requiresCreative, callback);
        } catch (ClassCastException e) {
            entry = new BooleanConfigEntry(name, false, requiresCreative, callback);
        }
        entries.put(key, entry);

        return entry;
    }

    protected <T extends Enum<T>> EnumConfigEntry<T> registerEnumSetting(String name, T defaultValue, boolean requiresCreative, Consumer<T> callback) {
        String key = doDefaultSetting(name, defaultValue.name());

        EnumConfigEntry<T> entry;
        try {
            entry = new EnumConfigEntry<>(name, T.valueOf(defaultValue.getDeclaringClass(), ConfigManager.settingsFile.get(key)), requiresCreative, callback);
        } catch (ClassCastException | EnumConstantNotPresentException e) {
            entry = new EnumConfigEntry<>(name, defaultValue, requiresCreative, callback);
        }
        entries.put(key, entry);

        return entry;
    }

    private String doDefaultSetting(String name, String defaultValue) {
        String key = String.format("%s.%s", categoryName, name);
        if (!ConfigManager.settingsFile.containsKey(key)) {
            ConfigManager.settingsFile.put(key, defaultValue);
        }

        return key;
    }

}
