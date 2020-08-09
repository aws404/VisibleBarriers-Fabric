package com.aws404.visiblebarriers.devicemenu;

import com.aws404.visiblebarriers.util.ItemUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;

public class OreTypeGUI extends Screen {

    private ArrayList<String> options = new ArrayList<String>();

    public OreTypeGUI(String device) {
        super(new TranslatableText("menu.type.title", new Object[0]));

        options.add(device + "Wall");
        options.add(device);
        options.add(device + "Corner");
    }

    protected void init() {
        for (int i = 0; i < options.size(); i++) {
            String name = options.get(i);
            this.addButton(new ButtonWidget(this.width / 2 - 40, 70 + (i * 25), 80, 20, name, (buttonWidget) -> {
                this.setRotation(name);
            }));
        }
    }

    private void setRotation(String option) {
        if (option.contains("Wall") || option.contains("Corner")) {
            this.minecraft.openScreen((Screen) new RotationSelectorGUI("gather:" + option));
        } else {
            ItemUtils.givePlayerItemStack(ItemUtils.getCustomStructureBlock("device:gather:" + option, "SAVE"));
            this.minecraft.openScreen((Screen) null);
        }
    }

    public void render(int mouseX, int mouseY, float delta) {
        DiffuseLighting.disableGuiDepthLighting();
        this.renderBackground();
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 30, 16777215);

        DiffuseLighting.enableGuiDepthLighting();
        super.render(mouseX, mouseY, delta);
    }

}
