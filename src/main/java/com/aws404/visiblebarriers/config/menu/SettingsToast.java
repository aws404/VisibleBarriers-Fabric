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

    public SettingsToast(BaseConfigEntry<?> entry) {
        this.entry = entry;
        this.title = new TranslatableText("setting.visiblebarriers." + entry.name);
        this.description = new TranslatableText("setting.visiblebarriers.toggle", entry.getRawValue().toString());
    }

    public void update() {
        this.description = new TranslatableText("setting.visiblebarriers.toggle", entry.getRawValue().toString());
        this.justUpdated = true;
    }

    public Object getType() {
        return entry;
    }

    public Visibility draw(ToastManager manager, long startTime) {
        if (this.justUpdated) {
            this.startTime = startTime;
            this.justUpdated = false;
        }

        manager.getGame().getTextureManager().bindTexture(TOASTS_TEX);
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        manager.blit(0, 0, 0, 64, 160, 32);

        manager.getGame().textRenderer.draw(this.title.asFormattedString(), 18.0F, 7.0F, -256);
        manager.getGame().textRenderer.draw(description.asFormattedString(), 18.0F, 18, -1);

        return startTime - this.startTime < 5000L ? Visibility.SHOW : Visibility.HIDE;
    }
}
