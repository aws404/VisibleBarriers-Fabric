package com.aws404.visiblebarriers.mixin;

import com.aws404.visiblebarriers.VisibleBarriers;
import net.minecraft.block.*;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BarrierBlock.class)
public abstract class BarrierBlockMixin extends Block {

	public BarrierBlockMixin(Settings settings) {
		super(settings);
	}

	/**
	 * Overwrites the default barrier method which returns INVISIBLE.
	 * This makes the game actually attempt to load a model for the block
	 */
	@Overwrite
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	/**
	 * Sets the logic for rendering.
	 * States that no sides should be rendered if the setting is on Vanilla rendering and only renders outer sides if on Visible rendering
	 */
	@Override
	public boolean isSideInvisible(BlockState state, BlockState neighbor, Direction facing) {
		if (!VisibleBarriers.SHOWING_BARRIERS)
			return true;

		return neighbor.getBlock() == this ? true : super.isSideInvisible(state, neighbor, facing);
	}


}
