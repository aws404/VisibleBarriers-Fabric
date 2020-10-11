package com.aws404.visiblebarriers.config.types;

import com.aws404.visiblebarriers.config.menu.SettingsListWidget;
import com.aws404.visiblebarriers.config.menu.SettingsListWidgetEntry;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.List;
import java.util.function.Consumer;

public class BooleanConfigEntry extends BaseConfigEntry<Boolean> {
    public BooleanConfigEntry(String name, boolean defaultValue, boolean requiresCreative, Consumer<Boolean> callback) {
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
    public Text getValueText() {
        return new TranslatableText("setting.visiblebarriers." + name + "." + value);
    }

    @Override
    public SettingsListWidgetEntry getSettingsListEntry(SettingsListWidget parent) {
        return new BooleanConfigListEntry(client, parent, this);
    }

    public static class BooleanConfigListEntry extends SettingsListWidgetEntry.SettingsListWidgetConfigEntry {
        private final CheckboxWidget checkBox;

        private BooleanConfigListEntry(MinecraftClient client, SettingsListWidget parent, BooleanConfigEntry entry) {
            super(client, parent, entry);
            this.checkBox = new CheckboxWidget(0, 0, 200, 20, "", entry.getValue());
        }

        public void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            super.render(index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
            this.checkBox.x = x + 205;
            this.checkBox.y = (y + entryHeight / 2) - 10;
            this.checkBox.render(mouseX, mouseY, tickDelta);
        }

        public List<? extends Element> children() {
            return ImmutableList.of(this.checkBox);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.checkBox.mouseClicked(mouseX, mouseY, button)) {
                ((BooleanConfigEntry) entry).setValue(checkBox.isChecked());
                return true;
            }
            return false;
        }

        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return this.checkBox.mouseReleased(mouseX, mouseY, button);
        }
    }
}
