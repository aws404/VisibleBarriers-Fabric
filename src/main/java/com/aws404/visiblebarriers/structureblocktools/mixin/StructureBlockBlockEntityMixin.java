package com.aws404.visiblebarriers.structureblocktools.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureBlockBlockEntity.class)
public abstract class StructureBlockBlockEntityMixin extends BlockEntity {

    // Now stores the raw structure name to be used if identifier invalid
    private String rawStructureName = "";

    @Shadow
    private BlockPos size;
    @Shadow
    private Identifier structureName;

    public StructureBlockBlockEntityMixin(BlockEntityType<?> type) {
        super(type);
    }

    /**
     * Changes to use rawStructureName if identifier is broken
     */
    @Inject(at = @At("HEAD"), method = "getStructureName", cancellable = true)
    public void getStructureName(CallbackInfoReturnable<String> info) {
        info.setReturnValue(this.structureName == null ? this.rawStructureName : this.structureName.toString());
    }

    /**
     * Changes to use rawStructureName if identifier is broken
     */
    @Inject(at = @At("HEAD"), method = "getStructurePath", cancellable = true)
    public void getStructurePath(CallbackInfoReturnable<String> info) {
        info.setReturnValue(this.structureName == null ? this.rawStructureName : this.structureName.getPath());
    }

    /**
     * Changes to also check rawStructureName
     */
    @Inject(at = @At("HEAD"), method = "hasStructureName", cancellable = true)
    public void hasStructureName(CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(this.structureName != null && this.rawStructureName == "");
    }

    /**
     * Changes to also set rawStructureName
     */
    @Inject(at = @At("HEAD"), method = "setStructureName", cancellable = true)
    public void setStructureName(String string, CallbackInfo info) {
        this.rawStructureName = string;
    }

    /**
     * Changes max structure size from 32 to 64
     */
    @Inject(at = @At("RETURN"), method = "fromTag")
    public void fromTag(CompoundTag tag, CallbackInfo info) {
        int l = MathHelper.clamp(tag.getInt("sizeX"), 0, 64);
        int m = MathHelper.clamp(tag.getInt("sizeY"), 0, 64);
        int n = MathHelper.clamp(tag.getInt("sizeZ"), 0, 64);
        this.size = new BlockPos(l, m, n);
    }
}
