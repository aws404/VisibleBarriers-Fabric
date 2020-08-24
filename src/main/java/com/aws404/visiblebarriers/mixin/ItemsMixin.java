package com.aws404.visiblebarriers.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Items.class)
public abstract class ItemsMixin {

    @Shadow
    private static Item register(Block block, Item iem) {
        return null;
    }

    /**
     * Adds a check to add blocks to creative menu (Quick and dirty way of doing this)
     * @reason to add blocks not normally in creative menu
     */
    @Inject(at = @At("HEAD"), method = "register(Lnet/minecraft/item/BlockItem;)Lnet/minecraft/item/Item;", cancellable = true)
    private static void register(BlockItem item, CallbackInfoReturnable<Item> info) {
        Block block = item.getBlock();
        ItemGroup newGroup = null;

        if (block == Blocks.BARRIER) {
            newGroup = ItemGroup.TOOLS;
        } else if (block == Blocks.COMMAND_BLOCK || block == Blocks.STRUCTURE_VOID || block == Blocks.STRUCTURE_BLOCK) {
            newGroup = ItemGroup.REDSTONE;
        }

        if (newGroup != null) {
            item = new BlockItem(block, (new Item.Settings()).group(newGroup));
            register(item.getBlock(), item);
            info.cancel();
        }
    }
}
