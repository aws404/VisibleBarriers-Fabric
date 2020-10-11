package com.aws404.visiblebarriers.armorstandtools.interactionscreen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Objects;

public class InteractionListWidget extends EntryListWidget<InteractionListWidget.StandEntry> {
    private final InteractionScreen parent;
    private int maxKeyNameLength = 0;
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public InteractionListWidget(InteractionScreen parent, List<ArmorStandEntity> stands) {
        super(client, parent.width + 45, parent.height, 25, parent.height, 35);
        this.parent = parent;

        for (ArmorStandEntity stand : stands) {
            addEntry(new StandEntry(stand));
        }
        setSelected(getEntry(0));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int scrollbarPositionX = this.width / 2;
        int j = scrollbarPositionX + 6;

        int l = this.top + 4 - (int) getScrollAmount();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
        renderList(matrices, l, mouseX, mouseY, delta);

        client.getTextureManager().bindTexture(DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(519);

        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(left, top, -100.0D).texture(0.0F, (float)this.top / 32.0F).color(64, 64, 64, 255).next();
        bufferBuilder.vertex(left + width, top, -100.0D).texture((float)this.width / 32.0F, (float)this.top / 32.0F).color(64, 64, 64, 255).next();
        bufferBuilder.vertex(left + width, 0.0D, -100.0D).texture((float)this.width / 32.0F, 0.0F).color(64, 64, 64, 255).next();
        bufferBuilder.vertex(left, 0.0D, -100.0D).texture(0.0F, 0.0F).color(64, 64, 64, 255).next();
        bufferBuilder.vertex(left, height, -100.0D).texture(0.0F, (float)this.height / 32.0F).color(64, 64, 64, 255).next();
        bufferBuilder.vertex(left + width, height, -100.0D).texture((float)this.width / 32.0F, (float)this.height / 32.0F).color(64, 64, 64, 255).next();
        bufferBuilder.vertex(left + width, bottom, -100.0D).texture((float)this.width / 32.0F, (float)this.bottom / 32.0F).color(64, 64, 64, 255).next();
        bufferBuilder.vertex(left, bottom, -100.0D).texture(0.0F, (float)this.bottom / 32.0F).color(64, 64, 64, 255).next();
        tessellator.draw();

        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
        RenderSystem.disableAlphaTest();
        RenderSystem.shadeModel(7425);
        RenderSystem.disableTexture();

        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(left, top + 4, 0.0D).texture(0.0F, 1.0F).color(0, 0, 0, 0).next();
        bufferBuilder.vertex(right, top + 4, 0.0D).texture(1.0F, 1.0F).color(0, 0, 0, 0).next();
        bufferBuilder.vertex(right, top, 0.0D).texture(1.0F, 0.0F).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(left, top, 0.0D).texture(0.0F, 0.0F).color(0, 0, 0, 255).next();
        tessellator.draw();

        int scrollPosition = Math.max(0, getMaxPosition() - (bottom - top - 4));
        if (scrollPosition > 0) {
            int scrollBarStart = (int)((float)((bottom - top) * (bottom - top)) / (float)getMaxPosition());
            scrollBarStart = MathHelper.clamp(scrollBarStart, 32, bottom - top - 8);
            int scrollBarEnd = (int) getScrollAmount() * (bottom - top - scrollBarStart) / scrollPosition + top;
            scrollBarEnd = Math.max(scrollBarEnd, top);

            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(scrollbarPositionX, bottom, 0.0D).texture(0.0F, 1.0F).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(j, bottom, 0.0D).texture(1.0F, 1.0F).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(j, top, 0.0D).texture(1.0F, 0.0F).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(scrollbarPositionX, top, 0.0D).texture(0.0F, 0.0F).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(scrollbarPositionX, (scrollBarEnd + scrollBarStart), 0.0D).texture(0.0F, 1.0F).color(128, 128, 128, 255).next();
            bufferBuilder.vertex(j, (scrollBarEnd + scrollBarStart), 0.0D).texture(1.0F, 1.0F).color(128, 128, 128, 255).next();
            bufferBuilder.vertex(j, scrollBarEnd, 0.0D).texture(1.0F, 0.0F).color(128, 128, 128, 255).next();
            bufferBuilder.vertex(scrollbarPositionX, scrollBarEnd, 0.0D).texture(0.0F, 0.0F).color(128, 128, 128, 255).next();
            bufferBuilder.vertex(scrollbarPositionX, (scrollBarEnd + scrollBarStart - 1), 0.0D).texture(0.0F, 1.0F).color(192, 192, 192, 255).next();
            bufferBuilder.vertex((j - 1), (scrollBarEnd + scrollBarStart - 1), 0.0D).texture(1.0F, 1.0F).color(192, 192, 192, 255).next();
            bufferBuilder.vertex((j - 1), scrollBarEnd, 0.0D).texture(1.0F, 0.0F).color(192, 192, 192, 255).next();
            bufferBuilder.vertex(scrollbarPositionX, scrollBarEnd, 0.0D).texture(0.0F, 0.0F).color(192, 192, 192, 255).next();
            tessellator.draw();
        }

        renderDecorations(matrices, mouseX, mouseY);

        RenderSystem.enableTexture();
        RenderSystem.shadeModel(7424);
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
    }

    protected void renderList(MatrixStack matrices, int y, int mouseX, int mouseY, float delta) {
        int i = this.getItemCount();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        for(int index = 0; index < i; ++index) {
            int renderY = this.getRowTop(index);
            int l = getRowTop(index) + this.itemHeight;
            if (l >= this.top && renderY <= this.bottom) {
                int m = y + index * this.itemHeight + this.headerHeight;
                int height = this.itemHeight;
                StandEntry entry = this.getEntry(index);
                int width = this.getRowWidth();
                if (this.isSelectedItem(index)) {
                    int boxLeft = this.width / 2 - width;
                    int boxRight = this.width / 2 - 20;
                    RenderSystem.disableTexture();
                    float f = this.isFocused() ? 1.0F : 0.5F;
                    RenderSystem.color4f(f, f, f, 1.0F);
                    bufferBuilder.begin(7, VertexFormats.POSITION);
                    bufferBuilder.vertex(boxLeft, (m + height + 2), 0.0D).next();
                    bufferBuilder.vertex(boxRight, (m + height + 2), 0.0D).next();
                    bufferBuilder.vertex(boxRight, (m - 2), 0.0D).next();
                    bufferBuilder.vertex(boxLeft, (m - 2), 0.0D).next();
                    tessellator.draw();
                    RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
                    bufferBuilder.begin(7, VertexFormats.POSITION);
                    bufferBuilder.vertex((boxLeft + 1), (m + height + 1), 0.0D).next();
                    bufferBuilder.vertex((boxRight - 1), (m + height + 1), 0.0D).next();
                    bufferBuilder.vertex((boxRight - 1), (m - 1), 0.0D).next();
                    bufferBuilder.vertex((boxLeft + 1), (m - 1), 0.0D).next();
                    tessellator.draw();
                    RenderSystem.enableTexture();
                }

                entry.render(matrices, index, renderY, this.width / 2 - width, width, height, mouseX, mouseY, isMouseOver(mouseX, mouseY) && Objects.equals(getEntryAtPosition(mouseX, mouseY), entry), delta);
            }
        }

    }

    public class StandEntry extends ElementListWidget.Entry<StandEntry> {
        public final ArmorStandEntity entity;
        private MutableText name;
        private final ButtonWidget editButton;

        private StandEntry(ArmorStandEntity entity) {
            this.entity = entity;
            this.editButton = new ButtonWidget(0, 0, 75, 20, new TranslatableText("menu.interaction.select"), (buttonWidget) -> {
                setSelected(this);
                parent.entityRenderWidget.setEntity(entity);
            });

            this.name = entity.getName().copy();
            if (!entity.hasCustomName()) {
                ItemStack hand = entity.getEquippedStack(EquipmentSlot.MAINHAND);
                if (hand.getItem() == Items.SNOW) {
                    this.name = hand.getName().copy().formatted(Formatting.ITALIC);
                }

                ItemStack head = entity.getEquippedStack(EquipmentSlot.HEAD);
                if (head.getItem() == Items.SNOW) {
                    this.name = head.getName().copy().formatted(Formatting.ITALIC);
                }
            }
            if (entity.isMarker()) {
                this.name = name.formatted(Formatting.RED);
            } else if (entity.isInvisible()) {
                this.name = name.formatted(Formatting.BLUE);
            }
            maxKeyNameLength = Math.max(maxKeyNameLength, client.textRenderer.getWidth(name));
        }

        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            client.textRenderer.draw(matrices, name, (float) (x + 90 - maxKeyNameLength), (float) ((y + entryHeight / 2) - 9 / 2), 16777215);
            this.editButton.x = x + 105;
            this.editButton.y = (y + entryHeight / 2) - 10;

            if (getSelected() == this) {
                editButton.active = false;
                editButton.setMessage(new TranslatableText("menu.interaction.selected"));
            } else if (entity.isMarker()) {
                editButton.active = false;
                editButton.setMessage(new TranslatableText("menu.interaction.locked"));
                if (isMouseOver(mouseX, mouseY)) {
                    parent.renderTooltip(matrices, new TranslatableText("menu.interaction.locked.hover"), mouseX, mouseY);
                }
            } else {
                editButton.active = true;
                editButton.setMessage(new TranslatableText("menu.interaction.select"));
            }

            this.editButton.render(matrices, mouseX, mouseY, tickDelta);

        }

        public List<? extends Element> children() {
            return ImmutableList.of(this.editButton);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return this.editButton.mouseClicked(mouseX, mouseY, button);
        }

        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return this.editButton.mouseReleased(mouseX, mouseY, button);
        }
    }
}