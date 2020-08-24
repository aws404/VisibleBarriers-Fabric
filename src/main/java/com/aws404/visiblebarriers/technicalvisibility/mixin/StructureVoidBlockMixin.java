package com.aws404.visiblebarriers.technicalvisibility.mixin;

import com.aws404.visiblebarriers.config.ConfigManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.StructureVoidBlock;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureVoidBlock.class)
public abstract class StructureVoidBlockMixin extends Block {

	public StructureVoidBlockMixin(Settings settings) {
		super(settings);
	}

	/**
	 * Overwrites the default structure block method which returns INVISIBLE.
	 * This makes the game actually attempt to load a model for the block
	 */
	@Inject(at = @At("HEAD"), method = "getRenderType", cancellable = true)
	public void getRenderType(BlockState state, CallbackInfoReturnable<BlockRenderType> info) {
		info.setReturnValue(BlockRenderType.MODEL);
	}

	/**
	 * Sets the logic for rendering.
	 * States that sides should only be rendered when Visible mode is on
	 */
	@Override
	public boolean isSideInvisible(BlockState state, BlockState neighbor, Direction facing) {
		return !ConfigManager.TECHNICAL_VISIBILITY.getValue();
	}

}
