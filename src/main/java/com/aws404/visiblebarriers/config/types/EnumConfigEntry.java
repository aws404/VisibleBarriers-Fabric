package com.aws404.visiblebarriers.config.types;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class EnumConfigEntry<T extends Enum<T>> extends BaseConfigEntry<T> {

    public EnumConfigEntry(String name, T defaultValue, boolean requiresCreative, ConfigEntryCallbackChange<T> callback) {
        super(name, defaultValue, requiresCreative, callback);
    }

    @Override
    public void setValue(String value) {
        setValue((T) T.valueOf(defaultValue.getClass(), value));
    }

    @Override
    public void cycle() {
        T[] test = (T[]) defaultValue.getClass().getEnumConstants();
        int newOrdinal = value.ordinal() + 1;
        if (newOrdinal > test.length - 1) {
            value = test[0];
        } else {
            value = test[newOrdinal];
        }
    }

    @Override
    public Text getMenuText() {
        return new TranslatableText("setting.visiblebarriers." + name).append(new LiteralText(": " + value.toString()));
    }

    @Override
    public Text getMenuHoverText() {
        return new TranslatableText("menu.settings.cycle");
    }

}
