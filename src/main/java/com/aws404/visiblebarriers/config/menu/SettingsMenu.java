package com.aws404.visiblebarriers.config.menu;

import com.aws404.visiblebarriers.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class SettingsMenu extends Screen {
    private SettingsListWidget listWidget;

    public SettingsMenu() {
        super(new TranslatableText("menu.settings.title"));
    }

    protected void init() {
        listWidget = new SettingsListWidget(MinecraftClient.getInstance(), this, ConfigManager.CATEGORIES);
        this.addChild(listWidget);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        listWidget.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void tick() {
        super.tick();
        listWidget.children().forEach(SettingsListWidgetEntry::tick);
    }

    public void onClose() {
        super.onClose();
        listWidget.children().forEach(SettingsListWidgetEntry::onClose);
        ConfigManager.saveSettingsFile();
    }
}
