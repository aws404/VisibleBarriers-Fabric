package com.aws404.visiblebarriers;

import com.aws404.visiblebarriers.config.ConfigManager;
import com.aws404.visiblebarriers.structureblocktools.StructureBlockNameRenderer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;

public class VisibleBarriers implements ModInitializer {

	public void onInitialize() {
		// Register the key bindings
		KeyBindings.registerKeyBindings();
		// Starts the structure block name renderer
		StructureBlockNameRenderer.start();
		// Starts the config manager
		ConfigManager.start();

		// Set barriers to accept transparency int the texture
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BARRIER, RenderLayer.getTranslucent());
    }
}
