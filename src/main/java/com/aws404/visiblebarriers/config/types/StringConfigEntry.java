package com.aws404.visiblebarriers.config.types;

import com.aws404.visiblebarriers.config.menu.SettingsListWidget;
import com.aws404.visiblebarriers.config.menu.SettingsListWidgetEntry;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class StringConfigEntry extends BaseConfigEntry<String> {
    public StringConfigEntry(String name, String defaultValue, Consumer<String> callback) {
        super(name, defaultValue, false, callback);
    }

    public void cycle() {
        client.openScreen(new ChatScreen(String.format("/setting %s %s", name, value)));
    }

    @Override
    public Text getValueText() {
        return new LiteralText(value);
    }

    @Override
    public SettingsListWidgetEntry getSettingsListEntry(SettingsListWidget parent) {
        return new StringConfigListEntry(client, parent, this);
    }

    public static class StringConfigListEntry extends SettingsListWidgetEntry.SettingsListWidgetConfigEntry {
        private final TextFieldWidget textFieldWidget;
        private String newValue;

        private StringConfigListEntry(MinecraftClient client, SettingsListWidget parent, StringConfigEntry entry) {
            super(client, parent, entry);
            this.newValue = entry.getValue();
            this.textFieldWidget = new TextFieldWidget(client.textRenderer, 0, 0, 200, 20, entry.getValueText());
            this.textFieldWidget.setMaxLength(255);
            this.textFieldWidget.setText(entry.getRawValue());
            this.textFieldWidget.setChangedListener((string) -> this.newValue = string);
        }

        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
            this.textFieldWidget.x = x + 105;
            this.textFieldWidget.y = (y + entryHeight / 2) - 10;
            this.textFieldWidget.render(matrices, mouseX, mouseY, tickDelta);
        }

        public void tick() {
            this.textFieldWidget.tick();
        }

        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            return super.keyPressed(keyCode, scanCode, modifiers) || this.textFieldWidget.keyPressed(keyCode, scanCode, modifiers);
        }

        public boolean charTyped(char chr, int keyCode) {
            return this.textFieldWidget.charTyped(chr, keyCode);
        }

        public void onClose() {
            if (!((StringConfigEntry) entry).getValue().equals(newValue))
                entry.setValue(newValue);
        }

        public List<? extends Element> children() {
            return ImmutableList.of(this.textFieldWidget);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return this.textFieldWidget.mouseClicked(mouseX, mouseY, button);
        }

        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return this.textFieldWidget.mouseReleased(mouseX, mouseY, button);
        }
    }
}
