package com.aws404.visiblebarriers.config;

import com.aws404.visiblebarriers.config.types.BaseConfigEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.GameMode;

import java.util.HashMap;
import java.util.Map;

public class SettingsMenu extends Screen {

    protected final HashMap<BaseConfigEntry<?>, AbstractButtonWidget> buttons = new HashMap<>();

    public SettingsMenu() {
        super(new TranslatableText("menu.settings.title"));
    }

    protected void init() {
        buttons.clear();
        int current = 0;
        for (BaseConfigEntry<?> entry : ConfigManager.SETTINGS.values()) {
            makeSettingsButton(entry, 90 + (current * 25));
            current++;
        }
    }

    public void makeSettingsButton(BaseConfigEntry<?> entry, int y) {
        AbstractButtonWidget button = new ButtonWidget(this.width / 2 - 150, y, 300, 20, entry.getMenuText(), (buttonWidget) -> {
            entry.cycle();
            init();
        });
        button.active = !entry.requiresCreative || client.interactionManager.getCurrentGameMode() == GameMode.SPECTATOR || client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE;
        buttons.put(entry, button);
        addChild(button);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        DiffuseLighting.disableGuiDepthLighting();
        this.renderBackground(matrices);
        drawCenteredString(matrices, this.textRenderer, this.title.asString(), this.width / 2, 50, 16777215);

        for (Map.Entry<BaseConfigEntry<?>, AbstractButtonWidget> entry : buttons.entrySet()) {
            entry.getValue().render(matrices, mouseX, mouseY, delta);
            if (entry.getValue().isHovered()) {
                Text hoverText = entry.getValue().active ? entry.getKey().getMenuHoverText() : new LiteralText("This feature requires creative or spectator mode");
                renderTooltip(matrices, hoverText, mouseX, mouseY);
            }
        }
    }

    public void onClose() {
        super.onClose();
        ConfigManager.saveSettingsFile();
    }
}
