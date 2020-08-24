package com.aws404.visiblebarriers.config;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class SettingsMenu extends Screen {
    public SettingsMenu() {
        super(new TranslatableText("menu.settings.title"));
    }

    protected void init() {
        buttons.clear();
        int current = 0;
        for (ConfigEntry entry : ConfigManager.SETTINGS.values()) {
            makeSettingsButton(entry, 90 + (current * 25));
            current++;
        }
    }

    public void makeSettingsButton(ConfigEntry entry, int y) {
        Text settingName = new TranslatableText("setting.visiblebarriers." + entry.name).append(new LiteralText(": ")).append(new TranslatableText("setting.visiblebarriers." + entry.name + "." + entry.value));
        this.addButton(new ButtonWidget(this.width / 2 - 150, y, 300, 20, settingName, (buttonWidget) -> {
            entry.toggle();
            init();
        }));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        DiffuseLighting.disableGuiDepthLighting();
        this.renderBackground(matrices);
        drawCenteredString(matrices, this.textRenderer, this.title.asString(), this.width / 2, 30, 16777215);

        super.render(matrices, mouseX, mouseY, delta);
    }

    public void onClose() {
        super.onClose();
        ConfigManager.saveSettingsFile();
    }
}
