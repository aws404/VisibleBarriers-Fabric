package com.aws404.visiblebarriers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.TranslatableText;

public class VisibleBarriers implements ModInitializer {

	private static boolean showingBarriers = false;

	@Override
	public void onInitialize() {
		System.out.println("initilising");
		// Register the key binding
		KeyBindings.registerKeyBindings();

		// Set barriers to accept transparency int the texture
		BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BARRIER, RenderLayer.getTranslucent());
	}

	public static void toggleBarrierMode() {
		// Set new value
		showingBarriers = !showingBarriers;

		// Sends the update toast message
		SystemToast.show(MinecraftClient.getInstance().getToastManager(), SystemToast.Type.WORLD_BACKUP, new TranslatableText("toast.visiblebarriers.toggle.title"), new TranslatableText("toast.visiblebarriers.toggle.desc", showingBarriers ? new TranslatableText("options.visiblebarriers.shown") : new TranslatableText("options.visiblebarriers.hidden")));

		// Reload chunks
		MinecraftClient.getInstance().worldRenderer.reload();
	}

	public static boolean isShowingBarriers() {
		return showingBarriers;
	}
}
