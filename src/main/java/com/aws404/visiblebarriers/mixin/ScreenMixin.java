package com.aws404.visiblebarriers.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class ScreenMixin {
    /**
     * Changes to allow any character to be entered into the box when on a server
     * The server can then handle the name when the done button is pressed and will error if its invalid
     * @reason compatibility for other versions
     */
    @Inject(at = @At("HEAD"), method = "isValidCharacterForName", cancellable = true)
    private void isValidCharacterForName(String name, char character, int cursorPos, CallbackInfoReturnable<Boolean> info) {
        if (!MinecraftClient.getInstance().isInSingleplayer())
            info.setReturnValue(true);
    }
}
