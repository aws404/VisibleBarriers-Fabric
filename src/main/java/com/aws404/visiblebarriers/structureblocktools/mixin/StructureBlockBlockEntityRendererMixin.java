package com.aws404.visiblebarriers.structureblocktools.mixin;

import com.aws404.visiblebarriers.VisibleBarriers;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.StructureBlockBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureBlockBlockEntityRenderer.class)
public abstract class StructureBlockBlockEntityRendererMixin extends BlockEntityRenderer<StructureBlockBlockEntity> {

    private static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/beacon_beam.png");

    public StructureBlockBlockEntityRendererMixin(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    /**
     * Renders a beacon beam from structure blocks with broken attributes (due to world edit)
     */
    @Inject(at = @At("HEAD"), method = "render")
    private void render(StructureBlockBlockEntity structureBlockBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo info) {
        if (VisibleBarriers.isShowingBarriers() && !structureBlockBlockEntity.hasStructureName() && structureBlockBlockEntity.getMode() == StructureBlockMode.DATA) {
            long l = structureBlockBlockEntity.getWorld().getTime();
            BeaconBlockEntityRenderer.renderLightBeam(matrixStack, vertexConsumerProvider, BEAM_TEXTURE, f, 1.0f, l, i, j, DyeColor.RED.getColorComponents(), 0.15F, 0.175F);

        }
    }
}
