package com.aws404.visiblebarriers.devices;

import com.aws404.visiblebarriers.util.ItemUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.Arrays;
import java.util.List;

public class RotationSelectorGUI extends Screen {

    private final List<String> OPTIONS = Arrays.asList("0", "90", "180", "270");

    private String device = "";

    public RotationSelectorGUI(String device) {
        super(new TranslatableText("menu.rotation.title"));
        this.device = device;
    }

    protected void init() {
        for (int i = 0; i < OPTIONS.size(); i++) {
            String name = OPTIONS.get(i);
            this.addButton(new ButtonWidget(this.width / 2 - 40, 70 + (i * 25), 80, 20, new LiteralText(name), (buttonWidget) -> {
                this.getDevice(name);
            }));
        }
    }

    private void getDevice(String option) {
        ItemUtils.givePlayerItemStack(ItemUtils.getCustomStructureBlock("device:" + device + (option.equalsIgnoreCase("0") ? "" : ":" + option), "SAVE"));
        this.client.openScreen((Screen) null);
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        DiffuseLighting.disableGuiDepthLighting();
        this.renderBackground(matrixStack);
        drawCenteredString(matrixStack, this.textRenderer, this.title.asString(), this.width / 2, 30, 16777215);

        DiffuseLighting.enableGuiDepthLighting();
        super.render(matrixStack, mouseX, mouseY, delta);
    }
}
