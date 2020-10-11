package com.aws404.visiblebarriers.lootchests.mixin;

import com.aws404.visiblebarriers.lootchests.LootChestManager;
import com.aws404.visiblebarriers.util.VersionUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void sendChatMessage(String message, CallbackInfo ci) {
        if (message.startsWith("/reloadchests")) {
            try {
                LootChestManager.reloadLootChests();
                VersionUtils.sendMessage(new TranslatableText("message.visiblebarriers.chest_file_reload"));
            } catch (IOException e) {
                VersionUtils.sendMessage(new TranslatableText("message.visiblebarriers.chest_file_error", e.getMessage()));
            }
            ci.cancel();
        }
    }
}
