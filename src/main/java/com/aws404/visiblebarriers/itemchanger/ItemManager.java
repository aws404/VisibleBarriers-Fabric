package com.aws404.visiblebarriers.itemchanger;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;

public class ItemManager {

    public static Item VANTA_BLOCK = Registry.register(Registry.ITEM,
            new Identifier("vanta_block"),
            new CustomNamedItem(new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS), "block.visiblebarriers.vanta", new Identifier("ink_sac"), new LiteralText("VANTA BLACK"))
    );

    public static void registerOverrides() {
        registerItemOverride(Items.OAK_WOOD, new Identifier("oak_log"), new LiteralText("ALL SIDED OAK"), null);
        registerItemOverride(Items.SPRUCE_WOOD, new Identifier("spruce_log"), new LiteralText("ALL SIDED SPRUCE"), null);
        registerItemOverride(Items.BIRCH_WOOD, new Identifier("birch_log"), new LiteralText("ALL SIDED BIRCH"), null);
        registerItemOverride(Items.JUNGLE_WOOD, new Identifier("jungle_log"), new LiteralText("ALL SIDED JUNGLE"), null);
        registerItemOverride(Items.ACACIA_WOOD, new Identifier("acacia_log"), new LiteralText("ALL SIDED ACACIA"), null);
        registerItemOverride(Items.DARK_OAK_WOOD, new Identifier("dark_oak_log"), new LiteralText("ALL SIDED DARKOAK"), null);
        registerItemOverride(Items.MUSHROOM_STEM, new Identifier("red_mushroom"), new LiteralText("ALL SIDED MUSHROOM STEM RED"), null);
    }

    public static final HashMap<Block, ItemGroup> GROUP_REPLACEMENTS = new HashMap<Block, ItemGroup>() {{
        put(Blocks.BARRIER, ItemGroup.TOOLS);
        put(Blocks.COMMAND_BLOCK, ItemGroup.REDSTONE);
        put(Blocks.STRUCTURE_BLOCK, ItemGroup.REDSTONE);
        put(Blocks.STRUCTURE_VOID, ItemGroup.REDSTONE);
    }};

    private static Item registerItemOverride(Item original, Identifier replacementItem, LiteralText itemName, String lore) {
        return Registry.register(Registry.ITEM,
                Registry.ITEM.getRawId(original),
                Registry.ITEM.getId(original).toString(),
                new CustomNamedItem(new FabricItemSettings().group(original.getGroup()), original.getTranslationKey(), replacementItem, itemName, lore)
        );
    }
}
