
package com.doncoelho.mysticalclochedseeds.compat;

import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.doncoelho.mysticalclochedseeds.MysticalClochedSeeds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public final class CruxedFarmlandCompatibility {

    private static final Set<String> SUPPORTED_CROPS = Set.of(
            "nether_star",
            "dragon_egg",
            "gaia_spirit",
            "awakened_draconium",
            "neutronium",
            "nitro_crystal"
    );

    private CruxedFarmlandCompatibility() {
    }

    public static boolean matches(Crop crop, Block farmland) {
        if (crop == null || crop.getTier() == null || farmland == null) {
            return false;
        }

        if (crop.getTier().getValue() != 6
                || !"mysticalagradditions".equals(crop.getModId())
                || !SUPPORTED_CROPS.contains(crop.getName())) {
            return false;
        }

        ResourceLocation expectedId =
                ResourceLocation.fromNamespaceAndPath(
                        MysticalClochedSeeds.MODID,
                        crop.getName() + "_cruxed_farmland"
                );

        return expectedId.equals(BuiltInRegistries.BLOCK.getKey(farmland));
    }
}
