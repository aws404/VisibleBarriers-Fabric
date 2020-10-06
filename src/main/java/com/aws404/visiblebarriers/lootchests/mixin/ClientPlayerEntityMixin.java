package com.aws404.visiblebarriers.lootchests.mixin;

import com.aws404.visiblebarriers.config.ConfigManager;
import com.aws404.visiblebarriers.lootchests.LootChestManager;
import com.aws404.visiblebarriers.util.VersionUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
                VersionUtils.sendMessage("Loot chest file loaded!");
            } catch (IOException e) {
                VersionUtils.sendMessage("Error parsing loot chest file!");
            }
            ci.cancel();
        }
        if (message.startsWith("/setting")) {
            String[] args = message.split(" ");
            ConfigManager.SETTINGS.get(args[1]).setValue(args[2]);
            ci.cancel();
        }
    }
}
