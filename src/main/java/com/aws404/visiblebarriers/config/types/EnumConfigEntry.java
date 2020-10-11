package com.aws404.visiblebarriers.config.types;

import com.aws404.visiblebarriers.config.menu.SettingsListWidget;
import com.aws404.visiblebarriers.config.menu.SettingsListWidgetEntry;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class EnumConfigEntry<T extends Enum<T>> extends BaseConfigEntry<T> {

    public EnumConfigEntry(String name, T defaultValue, boolean requiresCreative, Consumer<T> callback) {
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
            setValue(test[0]);
        } else {
            setValue(test[newOrdinal]);
        }
    }

    @Override
    public Text getValueText() {
        return new LiteralText(value.toString());
    }

    @Override
    public SettingsListWidgetEntry getSettingsListEntry(SettingsListWidget parent) {
        return new EnumConfigListEntry(client, parent, this);
    }

    public static class EnumConfigListEntry extends SettingsListWidgetEntry.SettingsListWidgetConfigEntry {
        private final ButtonWidget editButton;

        private EnumConfigListEntry(MinecraftClient client, SettingsListWidget parent, EnumConfigEntry<?> entry) {
            super(client, parent, entry);
            this.editButton = new ButtonWidget(0, 0, 200, 20, "Select", (buttonWidget) -> {
                entry.cycle();
            });
        }

        public void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            super.render(index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
            this.editButton.x = x + 105;
            this.editButton.y = (y + entryHeight / 2) - 10;
            this.editButton.setMessage(entry.getValueText().asFormattedString());
            this.editButton.render(mouseX, mouseY, tickDelta);
        }

        public List<? extends Element> children() {
            return ImmutableList.of(this.editButton);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return this.editButton.mouseClicked(mouseX, mouseY, button);
        }

        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return this.editButton.mouseReleased(mouseX, mouseY, button);
        }
    }
}
