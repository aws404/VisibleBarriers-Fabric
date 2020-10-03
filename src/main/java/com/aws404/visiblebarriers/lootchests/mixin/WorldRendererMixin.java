package com.aws404.visiblebarriers.lootchests.mixin;

import com.aws404.visiblebarriers.config.ConfigManager;
import com.aws404.visiblebarriers.config.enums.LootChestBeam;
import com.aws404.visiblebarriers.lootchests.LootChestManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.command.argument.BrigadierArgumentTypes;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.text.LiteralText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    private static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/beacon_beam.png");

    @Shadow
    public static void drawBox(MatrixStack matrix, VertexConsumer vertexConsumer, double x1, double y1, double z1, double x2, double y2, double z2, float red, float f, float g, float alpha, float h, float green, float blue) { };

    @Shadow @Final private MinecraftClient client;

    /**
     * Responsible for rending the in-world loot chest overlay and beacon beam
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;checkEmpty(Lnet/minecraft/client/util/math/MatrixStack;)V", shift = At.Shift.AFTER, by = 2), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci, Profiler profiler, Vec3d vec3d, double camX, double camY, double camZ, Matrix4f matrix4f2, boolean bl, Frustum frustum2, boolean bl3, VertexConsumerProvider.Immediate immediate) {
        if (ConfigManager.LOOT_CHEST_BEAM.getValue() != LootChestBeam.NONE) {
            VertexConsumer vertexConsumer = immediate.getBuffer(RenderLayer.getLines());

            List<LootChestManager.LootChest> lootChests = LootChestManager.getRelevantLocations();

            // Iterates through any relevant (close by) loot chests
            lootChests.forEach(lootChest -> {
                // Draws the box at the loot chests location
                drawBox(matrices, vertexConsumer, lootChest.pos.getX() - camX, lootChest.pos.getY() - camY, lootChest.pos.getZ() - camZ, lootChest.pos.getX() + 1 - camX, lootChest.pos.getY() + 1 - camY, lootChest.pos.getZ() + 1 - camZ, 1, 1, 1, 1, 1, 1, 1);

                // Displays a beacon beam if a) the BEAM_ALL setting is enabled or b) the BEAM_BROKEN setting is enabled and the blockstate is not of a replaceable material (air, tall grass, ect)
                boolean renderBeam = false;
                if (ConfigManager.LOOT_CHEST_BEAM.getValue() == LootChestBeam.BEAM_ALL) {
                    renderBeam = true;
                } else if (ConfigManager.LOOT_CHEST_BEAM.getValue() == LootChestBeam.BEAM_BROKEN) {
                    boolean isReplaceable = client.world.getBlockState(lootChest.pos).getMaterial().isReplaceable();
                    boolean hasAirAbove = client.world.getBlockState(lootChest.pos.add(0, 1, 0)).isAir();
                    boolean hasBlockBelow = !(client.world.getBlockState(lootChest.pos.add(0, -1, 0)).getBlock() == Blocks.AIR);

                    renderBeam = !isReplaceable || !hasAirAbove || !hasBlockBelow;
                }
                if (renderBeam) {
                    matrices.push();
                    matrices.translate(lootChest.pos.getX() - camX, lootChest.pos.getY() - camY, lootChest.pos.getZ() - camZ);
                    BeaconBlockEntityRenderer.renderLightBeam(matrices, layer -> vertexConsumer, BEAM_TEXTURE, 1, 1F, client.world.getTime(), -lootChest.pos.getY(), 255 - lootChest.pos.getY(), DyeColor.RED.getColorComponents(), 0.15F, 0.175F);
                    matrices.pop();
                }
            });

            // Displays the actionbar message for the closest chest
            if (lootChests.size() >= 1) {
                LootChestManager.LootChest chest = lootChests.get(0);
                if (chest.pos.isWithinDistance(vec3d, 4)) {
                    client.inGameHud.setOverlayMessage(new LiteralText("Line: " + chest.line + ", Level: " + chest.level), false);
                }
            }
        }
    }
}
