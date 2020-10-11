package com.aws404.visiblebarriers.util;

import com.aws404.visiblebarriers.mixin.RenderPhaseAccessor;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.OptionalDouble;

public class RenderUtils {
    public static RenderLayer LINES_NO_CULL = RenderLayer.of("lines_no_cull", VertexFormats.POSITION_COLOR, 1, 256, RenderLayer.MultiPhaseParameters.builder().lineWidth(new RenderPhase.LineWidth(OptionalDouble.empty()))
            .layering(RenderPhaseAccessor.getProjectionLayering())
            .transparency(RenderPhaseAccessor.getTranslucentTransparency())
            .writeMaskState(RenderPhaseAccessor.getColorMask())
            .cull(RenderPhaseAccessor.disableCulling())
            .depthTest(RenderPhaseAccessor.alwaysDepthTest())
            .alpha(RenderPhaseAccessor.oneTenthAlpha())
            .target(RenderPhaseAccessor.outlineTarget())
            .build(false));

    public static RenderLayer getBlockEntityWithColor(Identifier texture) {
        return RenderLayer.of("entity_cutout_no_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, true, true, RenderLayer.MultiPhaseParameters.builder()
                .texture(new RenderPhase.Texture(texture, false, false))
                .transparency(RenderPhaseAccessor.getTranslucentTransparency())
                .diffuseLighting(RenderPhaseAccessor.enableDiffuseLighting())
                .alpha(RenderPhaseAccessor.oneTenthAlpha())
                .overlay(RenderPhaseAccessor.enableOverlayColoring())
                .build(true));
    }

    public static void drawBox(MatrixStack matrices, VertexConsumer vertexConsumer, double camX, double camY, double camZ, float startX, float startY, float startZ, float endX, float endY, float endZ, float r, float g, float b) {
        WorldRenderer.drawBox(matrices, vertexConsumer, startX - camX, startY - camY, startZ - camZ, endX - camX, endY - camY, endZ - camZ, r, g, b, 1, r, g, b);
    }

    public static void outlineBlockAtCurrent(MatrixStack matrices, VertexConsumer vertexConsumer, double camX, double camY, double camZ, BlockPos pos, float r, float g, float b) {
        WorldRenderer.drawBox(matrices, vertexConsumer, pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ, pos.getX() + 1 - camX, pos.getY() + 1 - camY, pos.getZ() + 1 - camZ, r, g, b, 1, r, g, b);
    }

    public static void outlineBlockAtCurrent(MatrixStack matrices, VertexConsumer vertexConsumer, float r, float g, float b) {
        WorldRenderer.drawBox(matrices, vertexConsumer, 0, 0, 0, 1, 1, 1, r, g, b, 1, r, g, b);
    }

    public static void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, Text text, int centerX, int y, int color) {
        textRenderer.drawWithShadow(text.getString(), (float)(centerX - textRenderer.getStringWidth(text.getString()) / 2), (float)y, color);
    }
}
