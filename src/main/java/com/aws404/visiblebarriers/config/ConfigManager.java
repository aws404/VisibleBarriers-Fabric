package com.aws404.visiblebarriers.config;

import com.aws404.visiblebarriers.VisibleBarriers;
import com.aws404.visiblebarriers.config.categories.BaseConfigCategory;
import com.aws404.visiblebarriers.config.categories.LootChestConfigCategory;
import com.aws404.visiblebarriers.config.categories.StructureBlockToolsCategory;
import com.aws404.visiblebarriers.config.categories.TechnicalVisibilityConfigCategory;
import com.aws404.visiblebarriers.config.types.BaseConfigEntry;
import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Gson gson = new Gson();

    private static boolean isInitialised = false;

    public static final ArrayList<BaseConfigCategory> CATEGORIES = new ArrayList<>();
    public static Map<String, String> settingsFile = new HashMap<>();

    private static final File CONFIG_FILE = new File(client.runDirectory.getPath(), "config/visiblebarriers.json");

    private static final String CONFIG_VERSION = "3";

    public static void start() {
        if (isInitialised)
            return;

        setUpSettingsFile(false);

        CATEGORIES.add(new TechnicalVisibilityConfigCategory(client));
        CATEGORIES.add(new LootChestConfigCategory(client));
        CATEGORIES.add(new StructureBlockToolsCategory(client));

        saveSettingsFile();

        isInitialised = true;
    }

    private static void setUpSettingsFile(boolean forceNew) {
        try {
            if (!CONFIG_FILE.exists() || forceNew) {
                VisibleBarriers.LOGGER.warn("Creating a new config file. Forced?: {}", forceNew);
                CONFIG_FILE.createNewFile();
                saveSettingsFile();
            }

            Reader reader = Files.newBufferedReader(CONFIG_FILE.toPath());
            settingsFile = gson.fromJson(reader, Map.class);
            reader.close();

            if (!settingsFile.getOrDefault("version", "0").equals(CONFIG_VERSION)) {
                VisibleBarriers.LOGGER.warn("Settings file out of date, create a new one.");
                setUpSettingsFile(true);
                return;
            }

            VisibleBarriers.LOGGER.info("Successfully loaded '{}' settings from the settings file, with config version '{}'", settingsFile.size(), CONFIG_VERSION);
        } catch (Exception e) {
            if (forceNew) {
                e.printStackTrace();
                VisibleBarriers.LOGGER.fatal("Error parsing the config file! Tried to make a new one and failed. ({})", e.getMessage());
            } else {
                VisibleBarriers.LOGGER.fatal("Error parsing the config file! Creating new one. ({})", e.getMessage());
                setUpSettingsFile(true);
            }
        }
    }

    public static void saveSettingsFile() {
        settingsFile.clear();
        for (BaseConfigCategory category : CATEGORIES) {
            for (Map.Entry<String, BaseConfigEntry<?>> entry : category.getConfigEntries().entrySet()) {
                settingsFile.put(entry.getKey(), entry.getValue().getRawValue().toString());
            }
        }

        settingsFile.put("version", CONFIG_VERSION);

        try {
            if (!CONFIG_FILE.exists()) {
                VisibleBarriers.LOGGER.info("No settings file was found, creating one.");
                CONFIG_FILE.createNewFile();
            }

            Writer writer = Files.newBufferedWriter(CONFIG_FILE.toPath());

            writer.write(gson.toJson(settingsFile));

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            VisibleBarriers.LOGGER.fatal("Error saving the config file. ({}) ", e.getMessage());
        }
    }
}
