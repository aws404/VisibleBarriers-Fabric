package com.aws404.visiblebarriers.mixin;

import com.aws404.visiblebarriers.VisibleBarriers;
import net.minecraft.block.*;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(StructureVoidBlock.class)
public abstract class StructureVoidBlockMixin extends Block {

	public StructureVoidBlockMixin(Settings settings) {
		super(settings);
	}

	/**
	 * Overwrites the default structure block method which returns INVISIBLE.
	 * This makes the game actually attempt to load a model for the block
	 */
	@Overwrite
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	/**
	 * Sets the logic for rendering.
	 * States that sides should only be rendered when Visible mode is on
	 */
	@Override
	public boolean isSideInvisible(BlockState state, BlockState neighbor, Direction facing) {
		return !VisibleBarriers.SHOWING_BARRIERS;
	}

}
