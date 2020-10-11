package com.aws404.visiblebarriers.config.menu;

import com.aws404.visiblebarriers.config.categories.BaseConfigCategory;
import com.aws404.visiblebarriers.config.types.BaseConfigEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.util.math.MathHelper;

import java.util.Collection;
import java.util.Map;

public class SettingsListWidget extends EntryListWidget<SettingsListWidgetEntry> {
    public final SettingsMenu parent;
    public int maxKeyNameLength = 0;

    public SettingsListWidget(MinecraftClient client, SettingsMenu parent, Collection<BaseConfigCategory> values) {
        super(client, parent.width, parent.height, 25, parent.height - 25, 25);
        this.parent = parent;

        for (BaseConfigCategory category : values) {
            addEntry(new SettingsListWidgetEntry.SettingsListWidgetCategory(client, this, category.getName()));
            for (Map.Entry<String, BaseConfigEntry<?>> entry : category.getConfigEntries().entrySet()) {
                addEntry(entry.getValue().getSettingsListEntry(this));
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.updateScrollingState(mouseX, mouseY, button);
        if (this.isMouseOver(mouseX, mouseY)) {
            SettingsListWidgetEntry entry = this.getEntryAtPositions(mouseY);
            if (entry != null) {
                if (entry.mouseClicked(mouseX, mouseY, button)) {
                    this.setFocused(entry);
                    this.setDragging(true);
                    return true;
                }
            } else if (button == 0) {
                this.clickedHeader((int)(mouseX - (double)(this.left + this.width / 2 - this.getRowWidth() / 2)), (int)(mouseY - (double)this.top) + (int)this.getScrollAmount() - 4);
                return true;
            }
        }

        return false;
    }

    private SettingsListWidgetEntry getEntryAtPositions(double y) {
        int m = MathHelper.floor(y - (double)this.top) - this.headerHeight + (int)this.getScrollAmount() - 4;
        int n = m / this.itemHeight;
        if (n > children().size()) {
            return null;
        }
        return this.children().get(n);
    }
}