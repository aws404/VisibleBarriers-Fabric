package com.aws404.visiblebarriers.tools;

import com.aws404.visiblebarriers.mixin.InGameHudMixin;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class StructureBlockNameRenderer {

    private static final String PLACEHOLDER_MESSAGE = "undefined";
    private static String lastMessage = PLACEHOLDER_MESSAGE;

    private static final Vec3i[] OFFSETS = {
            new Vec3i(0,0,0),
            new Vec3i(-1,0,0),
            new Vec3i(1,0,0),
            new Vec3i(0,0,-1),
            new Vec3i(0,0,1),
            new Vec3i(0,-1,0),
            new Vec3i(0,1,0),
    };

    public StructureBlockNameRenderer() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.world == null || client.player == null)
                return;

            // Get the ray trace from the camera
            HitResult hitResult = client.getCameraEntity().rayTrace(40.0D, 0.0f, false);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos targetedBlockPos = ((BlockHitResult)hitResult).getBlockPos();

                boolean found = false;

                for (int i = 0; i < OFFSETS.length; i++) {
                    BlockPos testPos = targetedBlockPos.add(OFFSETS[i]);

                    if (client.world.getBlockState(testPos).getBlock() == Blocks.STRUCTURE_BLOCK) {
                        targetedBlockPos = testPos;
                        found = true;
                        break;
                    }
                }

                // Test if block was found
                if (found) {
                    BlockEntity blockEntity = client.world.getBlockEntity(targetedBlockPos);
                    String name = ((StructureBlockBlockEntity)blockEntity).getStructureName();

                    // Ignore if name is blank
                    if (name != "" && name != null) {
                        lastMessage = new TranslatableText("message.visiblebarriers.structure_name", name).setStyle(new Style().setColor(Formatting.GOLD)).asFormattedString();
                        client.inGameHud.setOverlayMessage(lastMessage, false);
                    }

                } else if (((InGameHudMixin) client.inGameHud).getOverlayMessage().equalsIgnoreCase(lastMessage)) {
                    client.inGameHud.setOverlayMessage("", false);
                    lastMessage = PLACEHOLDER_MESSAGE;
                }
            }
        });
    }
}
