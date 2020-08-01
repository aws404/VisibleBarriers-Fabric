package com.aws404.visiblebarriers.mixin;

import com.aws404.visiblebarriers.VisibleBarriers;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {

    @Shadow
    MinecraftClient client;

    /**
     * Overwrites the default particle renderer
     * Uses better logic for testing if the player is holding a barrier and only spawns particles if mode is set to Vanilla
     */
    @Overwrite
    public void doRandomBlockDisplayTicks(int xCenter, int yCenter, int i) {
        Random random = new Random();
        boolean showBarrierParticles =
                client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE
                && !VisibleBarriers.SHOWING_BARRIERS
                && (this.client.player.inventory.getMainHandStack().getItem() == Item.fromBlock(Blocks.BARRIER)
                        || this.client.player.inventory.offHand.get(0).getItem() == Item.fromBlock(Blocks.BARRIER));

        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for(int k = 0; k < 667; ++k) {
            client.world.randomBlockDisplayTick(xCenter, yCenter, i, 16, random, showBarrierParticles, mutable);
            client.world.randomBlockDisplayTick(xCenter, yCenter, i, 32, random, showBarrierParticles, mutable);
        }
    }
}
