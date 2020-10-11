package com.aws404.visiblebarriers.itemchanger.mixin;

import com.aws404.visiblebarriers.itemchanger.CustomNamedItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract ItemStack setCustomName(@Nullable Text name);

    @Shadow protected abstract void updateEmptyState();

    @Shadow public abstract CompoundTag getOrCreateSubTag(String key);

    @Shadow public abstract Text getName();

    @Inject(method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V", at = @At("TAIL"))
    private void init(ItemConvertible item, int count, CallbackInfo ci) {
        if (item instanceof CustomNamedItem) {
            this.setOverrideName(((CustomNamedItem) item).getCreativeMenuName());
            this.setCustomName(((CustomNamedItem) item).getNameReplacement());

            String loreString = ((CustomNamedItem) item).getLoreLine();

            if (loreString != null) {
                CompoundTag compoundTag = this.getOrCreateSubTag("display");
                ListTag lore = new ListTag();
                lore.add(StringTag.of(loreString));
                compoundTag.put("Lore", lore);
            }

            updateEmptyState();
        }
    }

    @Inject(method = "getName", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getSubTag(Ljava/lang/String;)Lnet/minecraft/nbt/CompoundTag;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void getName(CallbackInfoReturnable<Text> cir, CompoundTag compoundTag) {
        if (compoundTag != null && compoundTag.contains("OverrideName", 8)) {
            String text = compoundTag.getString("OverrideName");
            if (text != null) {
                cir.setReturnValue(new TranslatableText(text).formatted(Formatting.RESET));
            }
        }
    }

    public void setOverrideName(String name) {
        CompoundTag compoundTag = this.getOrCreateSubTag("display");
        if (name != null) {
            compoundTag.putString("OverrideName", name);
        } else {
            compoundTag.remove("OverrideName");
        }
    }

}
