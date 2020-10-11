package com.aws404.visiblebarriers.config.menu;

import com.aws404.visiblebarriers.config.ConfigManager;
import com.aws404.visiblebarriers.util.RenderUtils;
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
        this.children.add(listWidget);
    }

    public void render(int mouseX, int mouseY, float delta) {
        renderBackground();
        listWidget.render(mouseX, mouseY, delta);
        RenderUtils.drawCenteredText(null, this.font, this.title, this.width / 2, 8, 16777215);
        super.render(mouseX, mouseY, delta);
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
