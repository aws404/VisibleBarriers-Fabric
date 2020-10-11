package com.aws404.visiblebarriers.config.categories;

import com.aws404.visiblebarriers.config.types.BooleanConfigEntry;
import com.aws404.visiblebarriers.config.types.EnumConfigEntry;
import net.minecraft.client.MinecraftClient;

public class StructureBlockToolsCategory extends BaseConfigCategory {
    public static EnumConfigEntry<StructureBlockNameDisplay> PHYSICAL_NAME_DISPLAY;
    public static BooleanConfigEntry ACTION_BAR_NAME_DISPLAY;
    public static BooleanConfigEntry BROKEN_BEAM;

    public StructureBlockToolsCategory(MinecraftClient client) {
        super(client, "structure_blocks");
        PHYSICAL_NAME_DISPLAY = registerEnumSetting("physical_name_display", StructureBlockNameDisplay.TARGETED_BLOCK, true, null);
        ACTION_BAR_NAME_DISPLAY = registerBooleanSetting("action_bar_name_display", true, null);
        BROKEN_BEAM = registerBooleanSetting("broken_beam", true, null);
    }

    public enum StructureBlockNameDisplay {
        NONE,
        TARGETED_BLOCK,
        BLOCK_CONSTANT
    }
}
