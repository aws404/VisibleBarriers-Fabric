package com.aws404.visiblebarriers.config.types;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class StringConfigEntry extends BaseConfigEntry<String> {
    public StringConfigEntry(String name, String defaultValue, ConfigEntryCallbackChange<String> callback) {
        super(name, defaultValue, false, callback);
    }

    public void cycle() {
        client.openScreen(new ChatScreen(String.format("/setting %s %s", name, value)));
    }

    @Override
    public Text getMenuText() {
        String renderText = value;
        if (renderText.length() > 35) {
            renderText = renderText.substring(0, 32).concat("...");
        }
        return new TranslatableText("setting.visiblebarriers." + name).append(new LiteralText(": " + renderText));
    }

    @Override
    public Text getMenuHoverText() {
        return new TranslatableText("menu.settings.command", name);
    }
}
