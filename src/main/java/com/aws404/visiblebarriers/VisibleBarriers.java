package com.aws404.visiblebarriers;

import com.aws404.visiblebarriers.config.ConfigManager;
import com.aws404.visiblebarriers.itemchanger.ItemManager;
import com.aws404.visiblebarriers.lootchests.LootChestManager;
import com.aws404.visiblebarriers.structureblocktools.StructureBlockNameRenderer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class VisibleBarriers implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger();

	public void onInitialize() {
		// Register the key bindings
		KeyBindings.registerKeyBindings();
		// Starts the structure block name renderer
		StructureBlockNameRenderer.start();
		// Starts the config manager
		ConfigManager.start();
		// Registers the item overrides
		ItemManager.registerOverrides();
		// Set barriers to accept transparency in its texture
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BARRIER, RenderLayer.getTranslucent());
		// Loads the loot chest file
		try {
			LootChestManager.reloadLootChests();
		} catch (IOException e) {
			LOGGER.info("No loot file found");
		}

    }

}
