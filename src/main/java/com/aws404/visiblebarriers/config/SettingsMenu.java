package com.aws404.visiblebarriers.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
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
        String settingName = new TranslatableText("setting.visiblebarriers." + entry.name).asFormattedString() + ": " + new TranslatableText("setting.visiblebarriers." + entry.name + "." + entry.value).asFormattedString();
        this.addButton(new ButtonWidget(this.width / 2 - 150, y, 300, 20, settingName, (buttonWidget) -> {
            entry.toggle();
            init();
        }));
    }

    public void render(int mouseX, int mouseY, float delta) {
        DiffuseLighting.disableGuiDepthLighting();
        this.renderBackground();
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 30, 16777215);

        super.render(mouseX, mouseY, delta);
    }

    public void onClose() {
        super.onClose();
        ConfigManager.saveSettingsFile();
    }
}
