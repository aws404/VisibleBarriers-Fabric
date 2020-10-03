package com.aws404.visiblebarriers.config.types;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class BooleanConfigEntry extends BaseConfigEntry<Boolean> {
    public BooleanConfigEntry(String name, boolean defaultValue, boolean requiresCreative, ConfigEntryCallbackChange<Boolean> callback) {
        super(name, defaultValue, requiresCreative, callback);
    }

    @Override
    public void setValue(String value) {
        setValue(Boolean.getBoolean(value));
    }

    public void cycle() {
        setValue(!value);
    }

    @Override
    public Text getMenuText() {
        return new TranslatableText("setting.visiblebarriers." + name).append(new LiteralText(": ")).append(new TranslatableText("setting.visiblebarriers." + name + "." + value));
    }

    @Override
    public Text getMenuHoverText() {
        return new TranslatableText("menu.settings.cycle");
    }
}
