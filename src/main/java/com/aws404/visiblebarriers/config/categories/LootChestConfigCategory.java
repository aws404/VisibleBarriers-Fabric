package com.aws404.visiblebarriers.config.categories;

import com.aws404.visiblebarriers.config.types.EnumConfigEntry;
import com.aws404.visiblebarriers.config.types.StringConfigEntry;
import com.aws404.visiblebarriers.lootchests.LootChestManager;
import com.aws404.visiblebarriers.util.VersionUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;

import java.io.File;
import java.io.IOException;

public class LootChestConfigCategory extends BaseConfigCategory {
    public static EnumConfigEntry<LootChestBeam> RENDER_BEAM;
    public static EnumConfigEntry<LootChestHighlighter> HIGHLIGHT;
    public static StringConfigEntry FILE_PATH;

    public static final File DEFAULT_LOOT_CHEST_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "chests.yml");

    public LootChestConfigCategory(MinecraftClient client) {
        super(client, "loot_chest");
        RENDER_BEAM = registerEnumSetting("render_beam", LootChestBeam.BROKEN, true, null);
        HIGHLIGHT = registerEnumSetting("highlight", LootChestHighlighter.NONE, true, null);
        FILE_PATH = registerStringSetting("file_path", DEFAULT_LOOT_CHEST_FILE.getAbsolutePath(), value -> {
            LootChestManager.LOOT_CHEST_FILE = new File(value);
            try {
                LootChestManager.reloadLootChests();
            } catch (IOException e) {
                VersionUtils.sendMessage(new TranslatableText("message.visiblebarriers.chest_file_error", e.getMessage()));
            }
        });
    }

    public enum LootChestBeam {
        NONE,
        ALL,
        BROKEN
    }

    public enum LootChestHighlighter {
        NONE,
        OUTLINE_CULLED,
        OUTLINED_NOT_CULLED
    }

}
