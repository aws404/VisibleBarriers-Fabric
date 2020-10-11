package com.aws404.visiblebarriers.config.categories;

import com.aws404.visiblebarriers.config.types.BooleanConfigEntry;
import net.minecraft.client.MinecraftClient;

public class TechnicalVisibilityConfigCategory extends BaseConfigCategory {
    public static BooleanConfigEntry MASTER_SWITCH;
    public static BooleanConfigEntry SHOW_BARRIERS;
    public static BooleanConfigEntry SHOW_ARMOR_STANDS;
    public static BooleanConfigEntry SHOW_STRUCTURE_VOIDS;
    public static BooleanConfigEntry GUILD_BANNER_BEAM;

    public TechnicalVisibilityConfigCategory(MinecraftClient client) {
        super(client, "technical_visibility");

        MASTER_SWITCH = registerBooleanSetting("technical_master", true, value -> client.worldRenderer.reload());
        SHOW_BARRIERS = registerBooleanSetting("show_barriers", true, value -> client.worldRenderer.reload());
        SHOW_STRUCTURE_VOIDS = registerBooleanSetting("show_structure_voids", true, value -> client.worldRenderer.reload());
        SHOW_ARMOR_STANDS = registerBooleanSetting("show_armor_stands", true, null);
        GUILD_BANNER_BEAM = registerBooleanSetting("guild_banner_beam", true, null);
    }
}
