package com.aws404.visiblebarriers.util;

import com.aws404.visiblebarriers.mixin.MinecraftClientMixin;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;

public class ItemUtils {
    public static void givePlayerItemStack(ItemStack itemStack) {
        MinecraftClient mc = MinecraftClient.getInstance();

        mc.player.inventory.addPickBlock(itemStack);
        mc.interactionManager.clickCreativeStack(mc.player.getStackInHand(Hand.MAIN_HAND), 36 + mc.player.inventory.selectedSlot);
    }

    public static ItemStack getCustomStructureBlock(String name, String mode) {
        return getCustomStructureBlock(name, mode, 0, 0, 0, 0, 0, 0);
    }

    public static ItemStack getCustomStructureBlock(String name, String mode, int sizeX, int sizeY, int sizeZ, int posX, int posY, int posZ) {
        MinecraftClient mc = MinecraftClient.getInstance();

        // Create NBT tag data
        CompoundTag tag = new CompoundTag();
        tag.putString("name", name);
        tag.putString("author", mc.player.getName().asString());
        tag.putString("metadata", "");
        tag.putInt("posX", posX);
        tag.putInt("posY", posY);
        tag.putInt("posZ", posZ);
        tag.putInt("sizeX", sizeX);
        tag.putInt("sizeY", sizeX);
        tag.putInt("sizeZ", sizeZ);
        tag.putString("rotation", "NONE");
        tag.putString("mirror", "NONE");
        tag.putString("mode", mode);
        tag.putBoolean("ignoreEntities", true);
        tag.putBoolean("powered", false);
        tag.putBoolean("showair", false);
        tag.putBoolean("showboundingbox", false);
        tag.putFloat("integrity", 1.0f);
        tag.putLong("seed", 1l);

        // Create structure block BlockEntity
        StructureBlockBlockEntity structureBlock = new StructureBlockBlockEntity();
        structureBlock.fromTag(tag);

        // Create ItemStack
        ItemStack item = ((MinecraftClientMixin) mc).invokeAddBlockEntityNbt(new ItemStack(Blocks.STRUCTURE_BLOCK), structureBlock);
        item.setCustomName(new LiteralText(name + " Placer"));

        return item;
    }
}
