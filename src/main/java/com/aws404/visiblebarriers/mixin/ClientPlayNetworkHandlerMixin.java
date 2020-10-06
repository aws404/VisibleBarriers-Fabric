package com.aws404.visiblebarriers.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.UnloadChunkS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    /**
     * Ignores the servers request to unload chunks.
     * @reason Wynncraft sends these packets which only allows loading of chunks at a short distance regardless of render distance
     */
    @Inject(method = "onUnloadChunk", at= @At("HEAD"), cancellable = true)
    private void onUnloadChunk(UnloadChunkS2CPacket packet, CallbackInfo ci) {
        ci.cancel();
    }

}
