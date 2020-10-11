package com.aws404.visiblebarriers.structureblocktools.mixin;

import com.aws404.visiblebarriers.config.categories.StructureBlockToolsCategory;
import com.aws404.visiblebarriers.mixin.GameRendererMixin;
import com.aws404.visiblebarriers.util.RenderUtils;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.StructureBlockBlockEntityRenderer;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureBlockBlockEntityRenderer.class)
public abstract class StructureBlockBlockEntityRendererMixin extends BlockEntityRenderer<StructureBlockBlockEntity> {

    private static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/beacon_beam.png");
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public StructureBlockBlockEntityRendererMixin(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    /**
     * Renders a beacon beam from structure blocks with broken attributes (due to world edit)
     * Renders the name plate above the structure block displaying its name
     */
    @Inject(at = @At("HEAD"), method = "render")
    private void render(StructureBlockBlockEntity structureBlockBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo info) {
        String structure = structureBlockBlockEntity.getStructureName();

        // Structure block nameplate renderer
        if (!structure.isEmpty()) {
            if (StructureBlockToolsCategory.PHYSICAL_NAME_DISPLAY.getValue() == StructureBlockToolsCategory.StructureBlockNameDisplay.BLOCK_CONSTANT) {
                renderLabel(structureBlockBlockEntity.getStructureName(), matrixStack, vertexConsumerProvider);
            } else if (StructureBlockToolsCategory.PHYSICAL_NAME_DISPLAY.getValue() == StructureBlockToolsCategory.StructureBlockNameDisplay.TARGETED_BLOCK && client.crosshairTarget.getType().equals(HitResult.Type.BLOCK)) {
                BlockHitResult blockHitResult = (BlockHitResult)client.crosshairTarget;
                if (blockHitResult.getBlockPos().equals(structureBlockBlockEntity.getPos())) {
                    renderLabel(structureBlockBlockEntity.getStructureName(), matrixStack, vertexConsumerProvider);
                }
            }
        }

        // Broken structure block beam renderer
        if (StructureBlockToolsCategory.BROKEN_BEAM.getValue() && (!structureBlockBlockEntity.hasStructureName() || structure.isEmpty()) && structureBlockBlockEntity.getMode() == StructureBlockMode.DATA) {
            RenderUtils.outlineBlockAtCurrent(matrixStack, vertexConsumerProvider.getBuffer(RenderUtils.LINES_NO_CULL), 1, 0, 0);
            BeaconBlockEntityRenderer.renderLightBeam(matrixStack, vertexConsumerProvider, BEAM_TEXTURE, f, 1.0f, structureBlockBlockEntity.getWorld().getTime(), -structureBlockBlockEntity.getPos().getY(), 255 - structureBlockBlockEntity.getPos().getY(), DyeColor.RED.getColorComponents(), 0.15F, 0.175F);
        }
    }

    protected void renderLabel(String string, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider) {
        matrices.push();

        matrices.translate(0.5D, 1.5F, 0.5D);
        matrices.multiply(((GameRendererMixin) client.gameRenderer).getCamera().getRotation());
        matrices.scale(-0.025F, -0.025F, 0.025F);

        Matrix4f matrix4f = matrices.peek().getModel();

        int backgroundColor = (int)(client.options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24;

        TextRenderer textRenderer = client.textRenderer;
        float stringWidth = (float)(-textRenderer.getStringWidth(string) / 2);
        textRenderer.draw(string, stringWidth, 0, 553648127, false, matrix4f, vertexConsumerProvider, false, backgroundColor, 15728880);
        textRenderer.draw(string, stringWidth, 0, -1, false, matrix4f, vertexConsumerProvider, false, 0, 15728880);

        matrices.pop();
    }
}
