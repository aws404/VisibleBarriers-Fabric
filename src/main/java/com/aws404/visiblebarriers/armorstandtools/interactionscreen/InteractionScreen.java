package com.aws404.visiblebarriers.armorstandtools.interactionscreen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class InteractionScreen extends Screen {
    private final List<ArmorStandEntity> stands;
    private InteractionListWidget listWidget;
    public EntityRenderWidget entityRenderWidget;

    public InteractionScreen(List<ArmorStandEntity> stands) {
        super(new TranslatableText("menu.interaction.title"));
        this.stands = stands;
    }

    @Override
    protected void init() {
        this.addButton(new ButtonWidget((width / 4) * 3 - 50, (height / 8) * 7, 100, 20, new TranslatableText("menu.interaction.interact"), button -> {
            client.openScreen(null);
            Vec3d vec3d = listWidget.getSelected().entity.getPos();
            client.getNetworkHandler().sendPacket(new PlayerInteractEntityC2SPacket(listWidget.getSelected().entity, Hand.MAIN_HAND, vec3d, false));
        }));

        this.listWidget = new InteractionListWidget(this, stands);
        this.children.add(this.listWidget);

        this.entityRenderWidget = new EntityRenderWidget((width / 4) * 3, height / 2 + height / 4, height / 4, listWidget.getSelected().entity);
        this.children.add(this.entityRenderWidget);

        super.init();
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        listWidget.render(matrices, mouseX, mouseY, delta);
        entityRenderWidget.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return this.isDragging() && button == 0 && entityRenderWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
}
