package com.aws404.visiblebarriers.itemchanger;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CustomNamedItem extends Item {
    private final Identifier replacement;
    private final LiteralText name;
    private final String lore;
    private final String creativeTranslation;

    public CustomNamedItem(FabricItemSettings settings, String creativeTranslation, Identifier replacement, LiteralText name, String lore) {
        super(settings);
        this.replacement = replacement;
        this.name = name;
        this.lore = lore;
        this.creativeTranslation = creativeTranslation;
    }

    public CustomNamedItem(FabricItemSettings settings, String creativeTranslation, Identifier replacement, LiteralText name) {
        this(settings, creativeTranslation, replacement, name, null);
    }

    public Item asItem() {
        return Registry.ITEM.get(replacement);
    }

    public LiteralText getNameReplacement() {
        return name;
    }

    public String getLoreLine() {
        return lore;
    }

    public String getCreativeMenuName() {
        return creativeTranslation;
    }
}
