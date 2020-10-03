package com.aws404.visiblebarriers.config;

import com.aws404.visiblebarriers.config.enums.LootChestBeam;
import com.aws404.visiblebarriers.config.enums.StructureBlockNameDisplay;
import com.aws404.visiblebarriers.config.types.BaseConfigEntry;
import com.aws404.visiblebarriers.config.types.BooleanConfigEntry;
import com.aws404.visiblebarriers.config.types.EnumConfigEntry;
import com.aws404.visiblebarriers.config.types.StringConfigEntry;
import com.aws404.visiblebarriers.lootchests.LootChestManager;
import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

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

    public static final HashMap<String, BaseConfigEntry<?>> SETTINGS = new HashMap<>();
    private static Map<String, String> settingsFile = new HashMap<>();

    private static final File CONFIG_FILE = new File(client.runDirectory.getPath(), "config/visiblebarriers.json");
    public static final File DEFAULT_LOOT_CHEST_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "chests.yml");

    public static BooleanConfigEntry BROKEN_STRUCTURE_BLOCK_BEAM;
    public static BooleanConfigEntry TECHNICAL_VISIBILITY;
    public static BooleanConfigEntry GUILD_BANNER_BEAM;

    public static EnumConfigEntry<LootChestBeam> LOOT_CHEST_BEAM;
    public static EnumConfigEntry<StructureBlockNameDisplay> STRUCTURE_BLOCK_NAME_DISPLAY;

    public static StringConfigEntry LOOT_CHEST_FILE_PATH;

    public static void start() {
        if (isInitialised)
            return;

        setUpSettingsFile(false);

        BROKEN_STRUCTURE_BLOCK_BEAM = registerBooleanSetting("broken_structure_block_beam", true, null);
        TECHNICAL_VISIBILITY = registerBooleanSetting("technical_visibility", true, (value) -> {
            // Reload chunks
            client.worldRenderer.reload();
        });
        GUILD_BANNER_BEAM = registerBooleanSetting("guild_banner_beam", true, null);
        LOOT_CHEST_BEAM = registerEnumSetting("loot_chest_highlight", LootChestBeam.NONE, true, null);
        STRUCTURE_BLOCK_NAME_DISPLAY = registerEnumSetting("structure_block_name_display", StructureBlockNameDisplay.NONE, true, null);

        LOOT_CHEST_FILE_PATH = registerStringSetting("loot_chest_file_path", DEFAULT_LOOT_CHEST_FILE.getAbsolutePath(), value -> {
            LootChestManager.LOOT_CHEST_FILE = new File(value);
            try {
                LootChestManager.reloadLootChests();
            } catch (IOException e) {
                client.player.sendMessage(new LiteralText("Error parsing the loot chest file!"));
            }
        });

        saveSettingsFile();

        isInitialised = true;
    }

    /**
     * Registers a setting
     * @param name the name of the setting
     * @param requiresCreative if this is true, the value of the setting will be allways false if the player is not creative/spectator
     * @param callback a callback for when the setting is changes
     */
    private static BooleanConfigEntry registerBooleanSetting(String name, boolean requiresCreative, BaseConfigEntry.ConfigEntryCallbackChange<Boolean> callback) {
        if (!settingsFile.containsKey(name)) {
            settingsFile.put(name, "false");
        }

        BooleanConfigEntry entry;
        try {
            entry = new BooleanConfigEntry(name, Boolean.parseBoolean(settingsFile.get(name)), requiresCreative, callback);
        } catch (ClassCastException e) {
            entry = new BooleanConfigEntry(name, false, requiresCreative, callback);
        }
        SETTINGS.put(name, entry);

        return entry;
    }

    /**
     * Registers a setting
     */
    private static <T extends Enum<T>> EnumConfigEntry<T> registerEnumSetting(String name, T defaultValue, boolean requiresCreative, BaseConfigEntry.ConfigEntryCallbackChange<T> callback) {
        if (!settingsFile.containsKey(name)) {
            settingsFile.put(name, defaultValue.name());
        }

        EnumConfigEntry<T> entry;
        try {
            entry = new EnumConfigEntry<>(name, T.valueOf(defaultValue.getDeclaringClass(), settingsFile.get(name)), requiresCreative, callback);
        } catch (ClassCastException e) {
            entry = new EnumConfigEntry<>(name, defaultValue, requiresCreative, callback);
        }
        SETTINGS.put(name, entry);

        return entry;
    }

    /**
     * Registers a setting
     */
    private static StringConfigEntry registerStringSetting(String name, String defaultValue, BaseConfigEntry.ConfigEntryCallbackChange<String> callback) {
        if (!settingsFile.containsKey(name)) {
            settingsFile.put(name, defaultValue);
        }

        StringConfigEntry entry;
        try {
            entry = new StringConfigEntry(name, settingsFile.get(name), callback);
        } catch (ClassCastException e) {
            entry = new StringConfigEntry(name, defaultValue, callback);
        }
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
        for (BaseConfigEntry<?> entry : SETTINGS.values()) {
            settingsFile.put(entry.name, entry.getRawValue().toString());
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
