package com.aws404.visiblebarriers.config.menu;

import com.aws404.visiblebarriers.config.types.BaseConfigEntry;
import com.aws404.visiblebarriers.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

public abstract class SettingsListWidgetEntry extends ElementListWidget.Entry<SettingsListWidgetEntry> {
    protected final MinecraftClient client;
    protected final SettingsListWidget parent;

    public SettingsListWidgetEntry(MinecraftClient client, SettingsListWidget parent) {
        this.client = client;
        this.parent = parent;
    }

    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
    }

    public void onClose() {
    }

    public void tick() {
    }

    public static class SettingsListWidgetConfigEntry extends SettingsListWidgetEntry {
        public final BaseConfigEntry<?> entry;
        private final Text name;

        public SettingsListWidgetConfigEntry(MinecraftClient client, SettingsListWidget parent, BaseConfigEntry<?> entry) {
            super(client, parent);
            this.entry = entry;
            this.name = new TranslatableText("setting.visiblebarriers." + entry.name);
            parent.maxKeyNameLength = Math.max(parent.maxKeyNameLength, client.textRenderer.getWidth(name));
        }

        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            client.textRenderer.draw(matrices, name, (float) (x + 90 - parent.maxKeyNameLength), (float) ((y + entryHeight / 2) - 9 / 2), 16777215);
        }

        public List<? extends Element> children() {
            return null;
        }
    }

    public static class SettingsListWidgetCategory extends SettingsListWidgetEntry {
        private final Text text;
        private final List<? extends Element> children = new ArrayList<>();

        public SettingsListWidgetCategory(MinecraftClient client1, SettingsListWidget parent1, Text text) {
            super(client1, parent1);
            this.text = text;
        }

        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            RenderUtils.drawCenteredText(matrices, client.textRenderer, this.text, parent.parent.width / 2, ((y + entryHeight / 2) - 9 / 2), 16777215);
        }

        public List<? extends Element> children() {
            return children;
        }
    }
}