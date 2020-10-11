package com.aws404.visiblebarriers.devices;

import com.aws404.visiblebarriers.util.ItemUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;

public class SizeSelectorGUI extends Screen {

    private final ArrayList<String> OPTIONS = new ArrayList<String>();

    public SizeSelectorGUI(String device) {
        super(new TranslatableText("menu.type.title"));

        OPTIONS.add("Small" + device);
        OPTIONS.add("Medium" + device);
        OPTIONS.add("Large" + device);

        if (device.contains("Oak") || device.contains("Jungle") || device.contains("Dark") || device.contains("Light")) {
            OPTIONS.add("Huge" + device);
        }
    }

    protected void init() {
        for (int i = 0; i < OPTIONS.size(); i++) {
            String name = OPTIONS.get(i);
            this.addButton(new ButtonWidget(this.width / 2 - 40, 70 + (i * 25), 80, 20, name, (buttonWidget) -> {
                this.getDevice(name);
            }));
        }
    }

    private void getDevice(String option) {
        ItemUtils.givePlayerItemStack(ItemUtils.getCustomStructureBlock("device:gather:" + option, "SAVE"));
        this.minecraft.openScreen(null);
    }

    public void render(int mouseX, int mouseY, float delta) {
        DiffuseLighting.disableGuiDepthLighting();
        this.renderBackground();
        drawCenteredString(font, this.title.asString(), this.width / 2, 30, 16777215);

        DiffuseLighting.enableGuiDepthLighting();
        super.render(mouseX, mouseY, delta);
    }
}
