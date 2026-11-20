
package com.doncoelho.mysticalclochedseeds.crux;

import java.util.function.Predicate;

public enum CruxedFarmlandType {

    NETHER_STAR("nether_star", null),
    DRAGON_EGG("dragon_egg", null),
    GAIA_SPIRIT("gaia_spirit", "botania"),
    AWAKENED_DRACONIUM("awakened_draconium", "draconicevolution"),
    NEUTRONIUM("neutronium", "avaritia"),
    NITRO_CRYSTAL("nitro_crystal", "powah");

    public static final String AGRADDITIONS_MOD_ID =
            "mysticalagradditions";

    private final String cropName;
    private final String extraModId;

    CruxedFarmlandType(String cropName, String extraModId) {
        this.cropName = cropName;
        this.extraModId = extraModId;
    }

    /**
     * Checks whether all required mods are loaded.
     *
     * Mystical Agradditions is always required.
     * Some farmlands also require another integration.
     */
    public boolean isAvailable(Predicate<String> modLoaded) {
        if (!modLoaded.test(AGRADDITIONS_MOD_ID)) {
            return false;
        }

        return extraModId == null || modLoaded.test(extraModId);
    }

    public String cropName() {
        return cropName;
    }

    public String extraModId() {
        return extraModId;
    }

    public String cruxItemId() {
        return AGRADDITIONS_MOD_ID + ":" + cropName + "_crux";
    }

    public String farmlandRegistryName() {
        return cropName + "_cruxed_farmland";
    }

    public static CruxedFarmlandType fromCropName(String cropName) {
        for (CruxedFarmlandType type : values()) {
            if (type.cropName.equals(cropName)) {
                return type;
            }
        }

        return null;
    }
}
