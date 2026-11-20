package com.doncoelho.mysticalclochedseeds.mixin;

import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.block.MysticalCropBlock;
import com.doncoelho.mysticalclochedseeds.compat.CruxedFarmlandCompatibility;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = MysticalCropBlock.class, remap = false)
public abstract class MysticalCropBlockMixin {

    @Shadow
    @Final
    private Crop crop;

    @ModifyExpressionValue(
            method = "canGrow(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/blakebr0/mysticalagriculture/api/crop/Crop;getCruxBlock()Lnet/minecraft/world/level/block/Block;",
                    remap = false
            ),
            require = 1
    )
    private Block mysticalclochedseeds$allowCruxedFarmland(
            Block originalCrux,
            Level level,
            BlockPos pos
    ) {
        if (originalCrux == null) {
            return null;
        }

        Block farmland = level.getBlockState(pos.below()).getBlock();

        if (CruxedFarmlandCompatibility.matches(this.crop, farmland)) {
            return null;
        }

        return originalCrux;
    }
}