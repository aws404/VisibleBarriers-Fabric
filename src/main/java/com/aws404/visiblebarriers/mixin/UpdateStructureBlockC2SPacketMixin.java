package com.aws404.visiblebarriers.mixin;

import net.minecraft.network.packet.c2s.play.UpdateStructureBlockC2SPacket;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(UpdateStructureBlockC2SPacket.class)
public class UpdateStructureBlockC2SPacketMixin {
    @Shadow
    private BlockPos size;

    /**
     * Changes max receivable structure size from 32 to 64
     * The server will not send one > 32 if it does not support it anyway
     * @reason comparability for servers which allow this
     */
    @Inject(at = @At("RETURN"), method = "read", cancellable = true)
    private void read(PacketByteBuf buf, CallbackInfo info) throws IOException {
        this.size = new BlockPos(MathHelper.clamp(buf.readByte(), 0, 64), MathHelper.clamp(buf.readByte(), 0, 64), MathHelper.clamp(buf.readByte(), 0, 64));

    }
}
