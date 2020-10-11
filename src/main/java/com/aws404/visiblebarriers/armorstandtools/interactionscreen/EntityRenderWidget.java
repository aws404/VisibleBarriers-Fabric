package com.aws404.visiblebarriers.armorstandtools.interactionscreen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Quaternion;

import java.util.ArrayList;
import java.util.List;

public class EntityRenderWidget extends AbstractParentElement implements Drawable {
    private final List<? extends Element> children = new ArrayList<>();

    private final int x;
    private final int y;
    private final int size;

    private LivingEntity entity;

    int yaw = 0;
    int pitch = 0;

    public EntityRenderWidget(int x, int y, int size, LivingEntity entity) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.entity = entity;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (entity == null) {
            return;
        }
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)x, (float)y, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(0.0D, 0.0D, 1000.0D);
        matrixStack.scale((float)size, (float)size, (float)size);
        Quaternion quaternion2 = Vector3f.POSITIVE_X.getDegreesQuaternion((float) (Math.atan(yaw / 40.0F) * 20.0F));
        Quaternion quaternion3 = Vector3f.POSITIVE_Y.getDegreesQuaternion(pitch);
        Quaternion quaternion = Vector3f.POSITIVE_Z.getDegreesQuaternion(180F);
        quaternion.hamiltonProduct(quaternion2);
        quaternion.hamiltonProduct(quaternion3);
        matrixStack.multiply(quaternion);
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        quaternion2.conjugate();
        entityRenderDispatcher.setRotation(quaternion2);
        entityRenderDispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixStack, immediate, 15728880));
        immediate.draw();
        entityRenderDispatcher.setRenderShadows(true);

        RenderSystem.popMatrix();
    }

    @Override
    public List<? extends Element> children() {
        return children;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        yaw -= deltaY;
        pitch += deltaX;
        return deltaX != 0 && deltaY != 0;
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }
}
