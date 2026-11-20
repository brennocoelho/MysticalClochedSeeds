
package com.doncoelho.mysticalclochedseeds.compat;

import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.doncoelho.mysticalclochedseeds.MysticalClochedSeeds;
import com.doncoelho.mysticalclochedseeds.crux.CruxedFarmlandType;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public final class CruxedFarmlandCompatibility {

    private CruxedFarmlandCompatibility() {
    }

    public static boolean matches(Crop crop, Block farmland) {

        if (crop == null || crop.getTier() == null || farmland == null) {
            return false;
        }

        if (crop.getTier().getValue() != 6
                || !CruxedFarmlandType.AGRADDITIONS_MOD_ID.equals(
                crop.getModId())) {
            return false;
        }

        CruxedFarmlandType type =
                CruxedFarmlandType.fromCropName(crop.getName());

        if (type == null) {
            return false;
        }

        ResourceLocation expectedId =
                ResourceLocation.fromNamespaceAndPath(
                        MysticalClochedSeeds.MODID,
                        type.farmlandRegistryName()
                );

        return expectedId.equals(
                BuiltInRegistries.BLOCK.getKey(farmland)
        );
    }
}
