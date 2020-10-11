package com.aws404.visiblebarriers.itemchanger.mixin;

import com.aws404.visiblebarriers.itemchanger.ItemManager;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Items.class)
public abstract class ItemsMixin {

    /**
     * Manages the adding of existing items to new creative menu categories
     */
    @Inject(method = "register(Lnet/minecraft/util/Identifier;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", at = @At("HEAD"), cancellable = true)
    private static void register(Identifier id, Item item, CallbackInfoReturnable<Item> cir) {
        if (ItemManager.GROUP_REPLACEMENTS.containsKey(id)) {
            if (item instanceof BlockItem) {
                item = new BlockItem(((BlockItem) item).getBlock(), (new FabricItemSettings()).rarity(Rarity.EPIC).group(ItemManager.GROUP_REPLACEMENTS.get(id)));
            } else {
                item = new Item((new FabricItemSettings()).rarity(Rarity.EPIC).group(ItemManager.GROUP_REPLACEMENTS.get(id)));
            }
        }

        if (item instanceof BlockItem) {
            ((BlockItem)item).appendBlocks(Item.BLOCK_ITEMS, item);
        }

        cir.setReturnValue(Registry.register(Registry.ITEM, id, item));
    }

}
