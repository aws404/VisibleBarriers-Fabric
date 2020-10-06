package com.aws404.visiblebarriers.util;

import com.aws404.visiblebarriers.accessors.MinecraftClientAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;

public class ItemUtils {
    /**
     * Gives the player an ItemStack in the same way as a pick block would
     * @param itemStack the stack to insert
     */
    public static void givePlayerItemStack(ItemStack itemStack) {
        MinecraftClient mc = MinecraftClient.getInstance();

        mc.player.inventory.addPickBlock(itemStack);
        mc.interactionManager.clickCreativeStack(mc.player.getStackInHand(Hand.MAIN_HAND), 36 + mc.player.inventory.selectedSlot);
    }

    /**
     * Gives a custom structure block with nbt data
     * @param name the structure name
     * @param mode the structure block mode
     * @return the ItemStack
     */
    public static ItemStack getCustomStructureBlock(String name, String mode) {
        return getCustomStructureBlock(name + " Placer", name, mode, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Gives a custom structure block with nbt data
     * @param itemName the name of the item
     * @param name the name of the structure
     * @param mode the structure block mode
     * @return the ItemStack
     */
    public static ItemStack getCustomStructureBlock(String itemName, String name, String mode) {
        return getCustomStructureBlock(itemName, name, mode, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Gives a custom structure block with nbt data
     * @param itemName the name of the item
     * @param name the name of the structure
     * @param mode the structure block mode
     * @param sizeX the size on the X axis
     * @param sizeY the size on the Y axis
     * @param sizeZ the size on the Z axis
     * @param posX the offset on the X axis
     * @param posY the offset on the Y axis
     * @param posZ the offset on the Z axis
     * @return the ItemStack
     */
    public static ItemStack getCustomStructureBlock(String itemName, String name, String mode, int sizeX, int sizeY, int sizeZ, int posX, int posY, int posZ) {
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
        ItemStack item = ((MinecraftClientAccessor) mc).invokeAddBlockEntityNbt(new ItemStack(Blocks.STRUCTURE_BLOCK), structureBlock);
        item.setCustomName(new LiteralText(itemName));

        return item;
    }

    public static ItemStack getCustomArmorStand(String itemName, CompoundTag standInformation) {
        ItemStack stack = new ItemStack(Items.ARMOR_STAND, 1);

        stack.putSubTag("parentB", standInformation);

        stack.getTag().putBoolean("PlacerTool", true);
        stack.getTag().putInt("otherEntities", 0);

        stack.setCustomName(new LiteralText(itemName));

        return stack;
    }

    public static ItemStack getArmorStandDevice(String itemName, String deviceName) {
        CompoundTag standInformation = new CompoundTag();

        standInformation.putBoolean("NoGravity", true);
        standInformation.putBoolean("ShowArms", true);
        standInformation.putBoolean("Invisible", true);
        standInformation.putBoolean("isDevice", true);
        standInformation.putBoolean("NoBasePlate", true);
        standInformation.putString("deviceName", deviceName);

        CompoundTag poseTag = new CompoundTag();
        ListTag blankPose = new ListTag();
        blankPose.add(FloatTag.of(0F));
        blankPose.add(FloatTag.of(0F));
        blankPose.add(FloatTag.of(0F));

        poseTag.put("RightArm", blankPose);
        poseTag.put("LeftArm", blankPose);
        poseTag.put("RightLeg", blankPose);
        poseTag.put("LeftLeg", blankPose);

        standInformation.put("Pose", poseTag);

        return getCustomArmorStand(itemName, standInformation);
    }

}
