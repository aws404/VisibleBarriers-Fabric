package com.aws404.visiblebarriers.mixin;

import net.minecraft.client.gui.screen.ingame.AbstractCommandBlockScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractCommandBlockScreen.class)
public class CommandBlockScreenMixin {

    @Shadow protected TextFieldWidget previousOutputTextField;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        previousOutputTextField.tick();
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void isActive(CallbackInfo ci) {
        previousOutputTextField.setEditable(true);
    }
}
