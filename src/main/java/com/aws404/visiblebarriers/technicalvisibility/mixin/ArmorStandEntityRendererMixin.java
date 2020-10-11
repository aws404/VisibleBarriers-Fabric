package com.aws404.visiblebarriers.technicalvisibility.mixin;

import com.aws404.visiblebarriers.config.categories.TechnicalVisibilityConfigCategory;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandEntityRenderer.class)
public abstract class ArmorStandEntityRendererMixin extends LivingEntityRenderer<ArmorStandEntity, ArmorStandArmorEntityModel> {

    private static final Identifier MARKER_SKIN = new Identifier("textures/entity/armorstand/marker.png");
    private static final Identifier INVISIBLE_SKIN = new Identifier("textures/entity/armorstand/invisible.png");
    private static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/beacon_beam.png");

    private static final CompoundTag GUILD_POSE = new CompoundTag();
    static {
        ListTag blankPose = new ListTag();
        blankPose.add(FloatTag.of(0F));
        blankPose.add(FloatTag.of(0F));
        blankPose.add(FloatTag.of(0F));

        GUILD_POSE.put("RightArm", blankPose);
        GUILD_POSE.put("LeftArm", blankPose);
        GUILD_POSE.put("RightLeg", blankPose);
        GUILD_POSE.put("LeftLeg", blankPose);
    }

    private static MinecraftClient client = MinecraftClient.getInstance();

    public ArmorStandEntityRendererMixin(EntityRenderDispatcher dispatcher, ArmorStandArmorEntityModel model, float shadowSize) {
        super(dispatcher, model, shadowSize);
    }

    /**
     * Invisible and marker visibility renderer
     */
    @Inject(method = "getRenderLayer", at = @At("HEAD"), cancellable = true)
    private void getRenderLayer(ArmorStandEntity armorStandEntity, boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<RenderLayer> cir) {
       if (TechnicalVisibilityConfigCategory.MASTER_SWITCH.getValue() && TechnicalVisibilityConfigCategory.SHOW_ARMOR_STANDS.getValue()) {
           if (armorStandEntity.isMarker())
               cir.setReturnValue(RenderLayer.getEntityCutoutNoCull(MARKER_SKIN));
           else if (armorStandEntity.isInvisible())
               cir.setReturnValue(RenderLayer.getEntityCutoutNoCull(INVISIBLE_SKIN));
       }
    }

    /**
     * Guild banner beam renderer
     */
    @Override
    public void render(ArmorStandEntity livingEntity, float entityRotation, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(livingEntity, entityRotation, g, matrixStack, vertexConsumerProvider, i);

        if (TechnicalVisibilityConfigCategory.GUILD_BANNER_BEAM.getValue() && livingEntity.world.getBlockState(livingEntity.getBlockPos().add(0, -1 ,0)).getBlock() == Blocks.SEA_LANTERN) {
            if (livingEntity.toTag(new CompoundTag()).get("Pose").equals(GUILD_POSE)) {
                matrixStack.push();
                matrixStack.translate(-0.5D, 0.0D, -0.5D);
                BeaconBlockEntityRenderer.renderLightBeam(matrixStack, vertexConsumerProvider, BEAM_TEXTURE, g, 1F, livingEntity.world.getTime(), 0, i, DyeColor.LIGHT_BLUE.getColorComponents(), 0.15F, 0.175F);
                matrixStack.pop();
            }
        }
    }
}
