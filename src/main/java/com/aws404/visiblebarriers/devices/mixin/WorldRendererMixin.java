package com.aws404.visiblebarriers.devices.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.resource.SynchronousResourceReloadListener;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements AutoCloseable, SynchronousResourceReloadListener {

    @Shadow
    private MinecraftClient client;

    @Shadow
    public static void drawBox(MatrixStack matrix, VertexConsumer vertexConsumer, double x1, double y1, double z1, double x2, double y2, double z2, float red, float f, float g, float alpha, float h, float green, float blue) { };

    private ArrayList<Direction> directions = new ArrayList<>();

    /**
     * Handles the visuals for the ore placer
     */
    @Inject(method = "drawBlockOutline", at = @At("TAIL"))
    private void drawBlockOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, Entity entity, double camX, double camY, double camZ, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        ItemStack item = client.player.inventory.getMainHandStack();
        if (item.getItem().equals(Blocks.STRUCTURE_BLOCK.asItem()) && item.getName().asString().contains(" Ore Placer")) {
            BlockHitResult hitResult = (BlockHitResult) client.crosshairTarget;

            BlockPos offPos = new BlockPos(blockPos);
            if (!blockState.canReplace(new ItemPlacementContext(new ItemUsageContext(client.player, Hand.MAIN_HAND, hitResult)))) {
                offPos = blockPos.offset(hitResult.getSide());
            }

            Vec3d hit = client.crosshairTarget.getPos();
            directions.add(hitResult.getSide());

            if (hitResult.getSide().getAxis() != Direction.Axis.Y) {
                boolean swapAxis = hitResult.getSide().getAxis() == Direction.Axis.X;
                double hitPoint = swapAxis ? hit.z % 1 : hit.x % 1;

                if ((hitPoint <= 0.3 && hitPoint >= 0) || (hitPoint <= -0.7 && hitPoint >= -1)) {
                    directions.add(swapAxis ? Direction.SOUTH : Direction.EAST);
                }
                if ((hitPoint >= 0.7 && hitPoint <= 1) || (hitPoint >= -0.3 && hitPoint <= 0)) {
                    directions.add(swapAxis ? Direction.NORTH : Direction.WEST);
                }
            }

            drawInnerBoxIndicators(matrixStack, vertexConsumer, camX, camY, camZ, offPos, directions);
            directions.clear();
        }
    }

    /**
     * Handles the visuals of the fish placer
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci, Profiler profiler, Vec3d vec3d, double camX, double camY, double camZ, Matrix4f matrix4f2, boolean bl, Frustum frustum2, boolean bl3, VertexConsumerProvider.Immediate immediate) {
        ItemStack item = client.player.inventory.getMainHandStack();
        if (item.getItem().equals(Blocks.STRUCTURE_BLOCK.asItem()) && item.getName().asString().contains(" Fish Placer")) {
            HitResult rawRayTrace = client.getCameraEntity().rayTrace(10.0D, 0.0F, true);
            if (rawRayTrace.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult) rawRayTrace).getBlockPos();

                Fluid fluid = client.world.getFluidState(pos).getFluid();
                BlockState above = client.world.getBlockState(pos.add(0, 1, 0));
                if (!fluid.matchesType(Fluids.EMPTY) && above.isAir() && pos.getSquaredDistance(client.player.getX(), client.player.getY(), client.player.getZ(), true) <= 10) {
                    VertexConsumer vertexConsumer = immediate.getBuffer(RenderLayer.getLines());
                    drawBox(matrices, vertexConsumer, pos.getX() - camX, pos.getY() + 1 - camY, pos.getZ() - camZ, pos.getX() + 1 - camX, pos.getY() - 2 - camY, pos.getZ() + 1 - camZ, 1, 1, 1, 1, 1, 1, 1);
                }
            }
        }
    }

    /**
     * Draws the directional indicator boxes
     */
    private static void drawInnerBoxIndicators(MatrixStack matrixStack, VertexConsumer vertexConsumer, double camX, double camY, double camZ, BlockPos pos, List<Direction> directions) {
        for (Direction dir : directions) {
            switch (dir) {
                case UP:
                case DOWN:
                    drawBox(matrixStack, vertexConsumer, pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ, pos.getX() + 1 - camX, pos.getY() + 0.2 - camY, pos.getZ() + 1 - camZ, 1, 1, 1, 1, 1, 1, 1);
                    break;
                case EAST:
                    drawBox(matrixStack, vertexConsumer, pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ, pos.getX() + 0.2 - camX, pos.getY() + 1 - camY, pos.getZ() + 1 - camZ, 1, 1, 1, 1, 1, 1, 1);
                    break;
                case WEST:
                    drawBox(matrixStack, vertexConsumer, pos.getX() + 1 - camX, pos.getY() - camY, pos.getZ() - camZ, pos.getX() + 0.8 - camX, pos.getY() + 1 - camY, pos.getZ() + 1 - camZ, 1, 1, 1, 1, 1, 1, 1);
                    break;
                case SOUTH:
                    drawBox(matrixStack, vertexConsumer, pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ, pos.getX() + 1 - camX, pos.getY() + 1 - camY, pos.getZ() + 0.2 - camZ, 1, 1, 1, 1, 1, 1, 1);
                    break;
                case NORTH:
                    drawBox(matrixStack, vertexConsumer, pos.getX() - camX, pos.getY() - camY, pos.getZ() + 1 - camZ, pos.getX() + 1 - camX, pos.getY() + 1 - camY, pos.getZ() + 0.8 - camZ, 1, 1, 1, 1, 1, 1, 1);
                    break;
            }
        }
    }
}