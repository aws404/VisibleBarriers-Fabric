package com.aws404.visiblebarriers.technicalvisibility.mixin;

import com.aws404.visiblebarriers.VisibleBarriers;
import net.minecraft.block.BarrierBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BarrierBlock.class)
public abstract class BarrierBlockMixin extends Block {

	public BarrierBlockMixin(Settings settings) {
		super(settings);
	}

	/**
	 * Overwrites the default barrier method which returns INVISIBLE.
	 * This makes the game actually attempt to load a model for the block
	 */
	@Inject(at = @At("HEAD"), method = "getRenderType", cancellable = true)
	public void getRenderType(BlockState state, CallbackInfoReturnable<BlockRenderType> info) {
		info.setReturnValue(BlockRenderType.MODEL);
	}

	/**
	 * Sets the logic for rendering.
	 * States that no sides should be rendered if the setting is on Vanilla rendering and only renders outer sides if on Visible rendering
	 */
	@Override
	public boolean isSideInvisible(BlockState state, BlockState neighbor, Direction facing) {
		if (!VisibleBarriers.isShowingBarriers())
			return true;

		return neighbor.getBlock() == this ? true : super.isSideInvisible(state, neighbor, facing);
	}


}
