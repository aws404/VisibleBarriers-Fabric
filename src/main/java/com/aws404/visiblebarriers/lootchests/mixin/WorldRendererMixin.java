package com.aws404.visiblebarriers.lootchests.mixin;

import com.aws404.visiblebarriers.config.categories.LootChestConfigCategory;
import com.aws404.visiblebarriers.lootchests.LootChestManager;
import com.aws404.visiblebarriers.util.RenderUtils;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    private static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/beacon_beam.png");

    @Shadow @Final private MinecraftClient client;

    @Shadow private ClientWorld world;

    @Shadow @Final private BufferBuilderStorage bufferBuilders;
    @Shadow private int renderDistance;

    /**
     * Responsible for rending the in-world loot chest overlay and beacon beam
     */
    @Inject(method = "render", at = @At("TAIL"))
    private void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
        if (LootChestConfigCategory.HIGHLIGHT.getValue() != LootChestConfigCategory.LootChestHighlighter.NONE
                || LootChestConfigCategory.RENDER_BEAM.getValue() != LootChestConfigCategory.LootChestBeam.NONE) {

            double camX = camera.getPos().getX();
            double camY = camera.getPos().getY();
            double camZ = camera.getPos().getZ();

            VertexConsumerProvider.Immediate immediate = bufferBuilders.getEntityVertexConsumers();
            List<LootChestManager.LootChest> lootChests = LootChestManager.getRelevantLocations((renderDistance - 1) * 16);

            // Iterates through any relevant (close by) loot chests
            lootChests.forEach(lootChest -> {
                matrices.push();
                matrices.translate(lootChest.pos.getX() - camX, lootChest.pos.getY() - camY, lootChest.pos.getZ() - camZ);

                boolean isBroken = !client.world.getBlockState(lootChest.pos).getMaterial().isReplaceable()
                        || client.world.getBlockState(lootChest.pos.add(0, 1, 0)).isSimpleFullBlock(world, lootChest.pos.add(0, 1, 0))
                        || client.world.getBlockState(lootChest.pos.add(0, -1, 0)).getBlock() == Blocks.AIR;

                // Draws the box at the loot chests location
                if (LootChestConfigCategory.HIGHLIGHT.getValue() != LootChestConfigCategory.LootChestHighlighter.NONE) {
                    VertexConsumer boxConsumer = immediate.getBuffer(LootChestConfigCategory.HIGHLIGHT.getValue() == LootChestConfigCategory.LootChestHighlighter.OUTLINED_NOT_CULLED ? RenderUtils.LINES_NO_CULL : RenderLayer.getLines());
                    RenderUtils.outlineBlockAtCurrent(matrices, boxConsumer, 1, isBroken ? 0 : 1, isBroken ? 0 : 1);
                }

                // Draws beam at broken loot chests
                if (LootChestConfigCategory.RENDER_BEAM.getValue() == LootChestConfigCategory.LootChestBeam.ALL || (isBroken && LootChestConfigCategory.RENDER_BEAM.getValue() == LootChestConfigCategory.LootChestBeam.BROKEN)) {
                    BeaconBlockEntityRenderer.renderLightBeam(matrices, immediate, BEAM_TEXTURE, 1, 1.0f, world.getTime(), -lootChest.pos.getY(), 255 - lootChest.pos.getY(), DyeColor.RED.getColorComponents(), 0.15F, 0.175F);
                }

                matrices.pop();
            });

            immediate.draw();

            // Displays the actionbar message for the closest chest
            if (lootChests.size() >= 1) {
                LootChestManager.LootChest chest = lootChests.get(0);
                if (chest.pos.isWithinDistance(camera.getPos(), 4)) {
                    client.inGameHud.setOverlayMessage(new TranslatableText("message.visiblebarriers.chest_info", chest.level, chest.line, chest.pos.toShortString()).formatted(Formatting.GOLD), false);
                }
            }
        }
    }


}
