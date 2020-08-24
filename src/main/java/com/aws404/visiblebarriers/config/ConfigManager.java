package com.aws404.visiblebarriers.config;

import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Gson gson = new Gson();

    private static boolean isInitialised = false;

    public static final HashMap<String, ConfigEntry> SETTINGS = new HashMap<String, ConfigEntry>();
    private static Map<String, Boolean> settingsFile = new HashMap<>();

    private static final File CONFIG_FILE = new File(client.runDirectory.getPath(), "config/visiblebarriers.json");

    public static ConfigEntry BROKEN_STRUCTURE_BLOCK_BEAM;
    public static ConfigEntry TECHNICAL_VISIBILITY;
    public static ConfigEntry ACTIONBAR_STRUCTURE_BLOCK_NAME;
    public static ConfigEntry CONSTANT_STRUCTURE_BLOCK_NAME;
    public static ConfigEntry GUILD_BANNER_BEAM;

    public static void start() {
        if (isInitialised)
            return;

        setUpSettingsFile(false);

        BROKEN_STRUCTURE_BLOCK_BEAM = registerSetting("broken_structure_block_beam", true, null);
        TECHNICAL_VISIBILITY = registerSetting("technical_visibility", true, (value) -> {
            // Reload chunks
            client.worldRenderer.reload();
        });
        ACTIONBAR_STRUCTURE_BLOCK_NAME = registerSetting("actionbar_structure_block_name", true,  null);
        CONSTANT_STRUCTURE_BLOCK_NAME = registerSetting("constant_structure_block_name", true, null);
        GUILD_BANNER_BEAM = registerSetting("guild_banner_beam", true, null);

        saveSettingsFile();

        isInitialised = true;
    }

    /**
     * Registers a setting
     * @param name the name of the setting
     * @param requiresCreative if this is true, the value of the setting will be allways false if the player is not creative/spectator
     * @param callback a callback for when the setting is changes
     */
    private static ConfigEntry registerSetting(String name, boolean requiresCreative, ConfigEntry.ConfigEntryCallbackChange callback) {
        if (!settingsFile.containsKey(name)) {
            settingsFile.put(name, false);
        }

        ConfigEntry entry = new ConfigEntry(name, settingsFile.get(name), requiresCreative, callback);
        SETTINGS.put(name, entry);

        return entry;
    }

    private static void setUpSettingsFile(boolean forceNew) {
        try {
            if (!CONFIG_FILE.exists() || forceNew) {
                System.out.println("Making new config file!");
                CONFIG_FILE.createNewFile();
                saveSettingsFile();
            }

            Reader reader = Files.newBufferedReader(CONFIG_FILE.toPath());

            settingsFile = gson.fromJson(reader, Map.class);

            reader.close();
        } catch (IOException e) {
            if (forceNew) {
                e.printStackTrace();
                System.out.println("Error parsing the config file! Tried to make a new one and failed.");
            } else {
                System.out.println("Error parsing the config file! Creating a new one.");
                setUpSettingsFile(true);
            }
        }
    }

    public static void saveSettingsFile() {
        settingsFile.clear();
        for (ConfigEntry entry : SETTINGS.values()) {
            settingsFile.put(entry.name, entry.value);
        }

        try {
            if (!CONFIG_FILE.exists()) {
                System.out.println("Making new config file!");
                CONFIG_FILE.createNewFile();
            }

            Writer writer = Files.newBufferedWriter(CONFIG_FILE.toPath());

            writer.write(gson.toJson(settingsFile));

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving the config file!");
        }
    }
}
