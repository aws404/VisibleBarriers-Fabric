package com.aws404.visiblebarriers.lootchests.mixin;

import com.aws404.visiblebarriers.config.ConfigManager;
import com.aws404.visiblebarriers.config.enums.LootChestBeam;
import com.aws404.visiblebarriers.config.enums.LootChestHighlighter;
import com.aws404.visiblebarriers.lootchests.LootChestManager;
import com.aws404.visiblebarriers.util.RenderUtils;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
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
public abstract class WorldRendererMixin {
    private static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/beacon_beam.png");

    @Shadow @Final private MinecraftClient client;

    @Shadow private ClientWorld world;

    /**
     * Responsible for rending the in-world loot chest overlay and beacon beam
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;checkEmpty(Lnet/minecraft/client/util/math/MatrixStack;)V", shift = At.Shift.AFTER, by = 2), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci, Profiler profiler, Vec3d vec3d, double camX, double camY, double camZ, Matrix4f matrix4f2, boolean bl, Frustum frustum2, boolean bl3, VertexConsumerProvider.Immediate immediate) {
        if (ConfigManager.LOOT_CHEST_HIGHLIGHTER.getValue() != LootChestHighlighter.NONE || ConfigManager.LOOT_CHEST_BEAM.getValue() != LootChestBeam.NONE) {
            RenderLayer layer = ConfigManager.LOOT_CHEST_HIGHLIGHTER.getValue() == LootChestHighlighter.OUTLINED_NOT_CULLED ? RenderUtils.LINES_NO_CULL : RenderLayer.getLines();

            List<LootChestManager.LootChest> lootChests = LootChestManager.getRelevantLocations();

            // Iterates through any relevant (close by) loot chests
            lootChests.forEach(lootChest -> {
                boolean isReplaceable = client.world.getBlockState(lootChest.pos).getMaterial().isReplaceable();
                boolean hasAirAbove = client.world.getBlockState(lootChest.pos.add(0, 1, 0)).isAir();
                boolean hasBlockBelow = !(client.world.getBlockState(lootChest.pos.add(0, -1, 0)).getBlock() == Blocks.AIR);

                boolean isBroken = !isReplaceable || !hasAirAbove || !hasBlockBelow;

                // Draws the box at the loot chests location
                if (ConfigManager.LOOT_CHEST_HIGHLIGHTER.getValue() != LootChestHighlighter.NONE) {
                    VertexConsumer boxConsumer = immediate.getBuffer(layer);
                    RenderUtils.outlineBlock(matrices, boxConsumer, camX, camY, camZ, lootChest.pos, 1, isBroken ? 0 : 1, isBroken ? 0 : 1);
                }

                if (ConfigManager.LOOT_CHEST_BEAM.getValue() == LootChestBeam.ALL || (isBroken && ConfigManager.LOOT_CHEST_BEAM.getValue() == LootChestBeam.BROKEN)) {
                    matrices.push();
                    matrices.translate(lootChest.pos.getX() - camX, lootChest.pos.getY() - camY, lootChest.pos.getZ() - camZ);
                    VertexConsumer beaconConsumer = immediate.getBuffer(RenderLayer.getLines());
                    BeaconBlockEntityRenderer.renderLightBeam(matrices, layer1 -> beaconConsumer, BEAM_TEXTURE, 1, 1.0f, world.getTime(), -lootChest.pos.getY(), 255, DyeColor.RED.getColorComponents(), 0.15F, 0.175F);
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
