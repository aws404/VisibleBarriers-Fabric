package com.aws404.visiblebarriers.accessors;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InGameHud.class)
public interface InGameHudMixin {
    /**
     * Allows access to the overlayMessage variable
     */
    @Accessor("overlayMessage")
    String getOverlayMessage();
}
