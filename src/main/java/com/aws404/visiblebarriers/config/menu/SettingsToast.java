package com.aws404.visiblebarriers.config.menu;

import com.aws404.visiblebarriers.config.types.BaseConfigEntry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class SettingsToast implements Toast {
    private final BaseConfigEntry<?> entry;
    private final Text title;
    private Text description;
    private long startTime;
    public boolean justUpdated;
    private int width;

    public SettingsToast(MinecraftClient client, BaseConfigEntry<?> entry) {
        this.entry = entry;
        this.title = new TranslatableText("setting.visiblebarriers." + entry.name);
        this.description = new TranslatableText("setting.visiblebarriers.toggle", entry.getRawValue().toString());
        this.width = Math.min(230, Math.max(client.textRenderer.getWidth(title), client.textRenderer.getWidth(description))) + 20;
    }

    public void update(MinecraftClient client) {
        this.description = new TranslatableText("setting.visiblebarriers.toggle", entry.getRawValue().toString());
        this.width = Math.min(230, Math.max(client.textRenderer.getWidth(title), client.textRenderer.getWidth(description))) + 20;
        this.justUpdated = true;
    }

    public Object getType() {
        return entry;
    }

    public int getWidth() {
        return this.width;
    }

    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        if (this.justUpdated) {
            this.startTime = startTime;
            this.justUpdated = false;
        }

        manager.getGame().getTextureManager().bindTexture(TEXTURE);
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        int i = this.getWidth();
        manager.drawTexture(matrices, 0, 0, 0, 64, i, this.getHeight());

        manager.getGame().textRenderer.draw(matrices, this.title, 18.0F, 7.0F, -256);
        manager.getGame().textRenderer.draw(matrices, description, 18.0F, 18, -1);

        return startTime - this.startTime < 5000L ? Visibility.SHOW : Visibility.HIDE;
    }
}
