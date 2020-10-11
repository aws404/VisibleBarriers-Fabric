package com.aws404.visiblebarriers.devices.mixin;

import com.aws404.visiblebarriers.util.ItemUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    private static MinecraftClient instance;

    /**
     * Handles the use of the custom device placers
     */
    @Inject(method = "doItemUse()V", at = @At("HEAD"), cancellable = true)
    private void doItemUse(CallbackInfo ci) {
        ItemStack item = instance.player.inventory.getMainHandStack();

        // Guild Banner Placer
        if (item.getName().asString().contains("Guild Banner Placer")) {
            if (instance.crosshairTarget.getType() == HitResult.Type.BLOCK) {
                BlockHitResult hit = (BlockHitResult) instance.crosshairTarget;
                BlockState block = instance.world.getBlockState(hit.getBlockPos());

                if (!block.getBlock().equals(Blocks.SEA_LANTERN)) {
                    // Sets titles (takes 3 functions because minecrafts title system is weird)
                    instance.inGameHud.setTitles(null, null, 5, 10, 5);
                    instance.inGameHud.setTitles(null, new LiteralText("Missing Sea Lantern").formatted(Formatting.RED), -1, -1, -1);
                    instance.inGameHud.setTitles(new LiteralText(""),null, -1, -1, -1);
                    ci.cancel();
                }
            }
        }

        if (item.getItem().equals(Blocks.STRUCTURE_BLOCK.asItem())) {
            String placerType = item.getName().asString().split(" ")[0];

            // Ore Placer
            if (item.getName().asString().contains(" Ore Placer") && instance.crosshairTarget.getType() == HitResult.Type.BLOCK) {
                BlockHitResult hitResult = (BlockHitResult) instance.crosshairTarget;

                Vec3d hit = instance.crosshairTarget.getPos();
                ArrayList<Direction> directions = new ArrayList<>(Collections.singleton(hitResult.getSide()));

                if (hitResult.getSide().getAxis() != Direction.Axis.Y) {
                    boolean swapAxis = hitResult.getSide().getAxis() == Direction.Axis.X;
                    double hitPoint = swapAxis ? hit.z % 1 : hit.x % 1;

                    if ((hitPoint <= 0.3 && hitPoint >= 0) || (hitPoint <= -0.7 && hitPoint >= -1)) {
                        directions.add(swapAxis ? Direction.SOUTH : Direction.EAST);
                    }
                    if ((hitPoint >= 0.7 && hitPoint <= 1) || (hitPoint >= -0.3 && hitPoint <= 0)) {
                        directions.add(swapAxis ? Direction.NORTH : Direction.WEST);
                    }
                }

                instance.player.inventory.main.set(instance.player.inventory.selectedSlot, ItemUtils.getCustomStructureBlock(placerType + " Ore Placer", "device:gather:" + placerType + getOreRotation(directions), "SAVE"));
                instance.interactionManager.clickCreativeStack(instance.player.getStackInHand(Hand.MAIN_HAND), 36 + instance.player.inventory.selectedSlot);
            }

            // Fish Placer
            if (item.getName().asString().contains(" Fish Placer")) {
                HitResult rawRayTrace = instance.getCameraEntity().raycast(10.0D, 0.0F, true);
                if (rawRayTrace.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = ((BlockHitResult) rawRayTrace).getBlockPos();
                    Fluid fluid = instance.world.getFluidState(pos).getFluid();
                    BlockState above = instance.world.getBlockState(pos.add(0, 1, 0));
                    if (!fluid.matchesType(Fluids.EMPTY) && above.isAir() && pos.getSquaredDistance(instance.player.getX(), instance.player.getY(), instance.player.getZ(), true) <= 10) {
                        BlockState block = instance.world.getBlockState(pos.add(0, -2, 0));
                        if (!block.isAir())
                            instance.interactionManager.attackBlock(pos.add(0, -2, 0), Direction.UP);

                        instance.player.inventory.main.set(instance.player.inventory.selectedSlot, ItemUtils.getCustomStructureBlock(placerType + " Fish Placer", "device:gather:" + placerType, "SAVE"));
                        instance.interactionManager.clickCreativeStack(instance.player.getStackInHand(Hand.MAIN_HAND), 36 + instance.player.inventory.selectedSlot);

                        sendPlacementAtCustomLocation(pos.add(0, -2, 0), true);

                        ci.cancel();
                    }
                }
            }
        }
    }

    /**
     * Sends a custom packet to the server with adjusted values to account for old placing logic
     */
    private void sendPlacementAtCustomLocation(BlockPos placementPos, boolean inWater) {
        if (!instance.player.shouldCancelInteraction() && instance.interactionManager.getCurrentGameMode() == GameMode.CREATIVE) {
            ItemStack itemStack = instance.player.getMainHandStack();
            // Send to server
            BlockPos severPos = placementPos.add(0 , inWater ? 1 : 0, 0);
            instance.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(new Vec3d(severPos.getX(), severPos.getY(), severPos.getZ()), Direction.DOWN, severPos, false)));

            int i = itemStack.getCount();

            // Place on client
            itemStack.useOnBlock(new ItemUsageContext(instance.player, Hand.MAIN_HAND, new BlockHitResult(new Vec3d(placementPos.getX(), placementPos.getY(), placementPos.getZ()), Direction.DOWN, placementPos, false)));
            itemStack.setCount(i);
        }
    }

    /**
     * Gets the ore rotation values for the given directions
     * @param directions a list of directions to account for (1 or 2)
     * @return the string to be appended on the end
     */
    private static String getOreRotation(List<Direction> directions) {
        if (directions.size() >= 2) {
            if (directions.contains(Direction.SOUTH) && directions.contains(Direction.EAST)) {
                return "Corner";
            }
            if (directions.contains(Direction.SOUTH) && directions.contains(Direction.WEST)) {
                return "Corner:90";
            }
            if (directions.contains(Direction.NORTH) && directions.contains(Direction.WEST)) {
                return "Corner:180";
            }
            if (directions.contains(Direction.NORTH) && directions.contains(Direction.EAST)) {
                return "Corner:270";
            }
        } else if (directions.size() == 1) {
            switch (directions.get(0)) {
                case SOUTH:
                    return "Wall";
                case WEST:
                    return "Wall:90";
                case NORTH:
                    return "Wall:180";
                case EAST:
                    return "Wall:270";
            }
        }

        return "";
    }
}
