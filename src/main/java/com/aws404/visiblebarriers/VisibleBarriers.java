package com.aws404.visiblebarriers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.TranslatableText;

public class VisibleBarriers implements ModInitializer {

	public static boolean SHOWING_BARRIERS = false;

	@Override
	public void onInitialize() {
		// Register the key binding
		KeyBindings.registerKeyBindings();

		// Set barriers to accept transparency int the texture
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BARRIER, RenderLayer.getTranslucent());
	}

	public static void toggleBarrierMode() {
		// Set new value
		SHOWING_BARRIERS = !SHOWING_BARRIERS;

		// Send update message
		MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new TranslatableText("message.visiblebarriers.toggle", SHOWING_BARRIERS ? "Visible Barriers" : "Vanilla"));

		// Reload chunks
		MinecraftClient.getInstance().worldRenderer.reload();
	}
}
