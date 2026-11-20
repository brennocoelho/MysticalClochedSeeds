package com.doncoelho.mysticalclochedseeds.crux;

public enum CruxedFarmlandType {

    NETHER_STAR("nether_star"),
    DRAGON_EGG("dragon_egg"),
    GAIA_SPIRIT("gaia_spirit"),
    AWAKENED_DRACONIUM("awakened_draconium"),
    NEUTRONIUM("neutronium"),
    NITRO_CRYSTAL("nitro_crystal");

    private final String cropName;

    CruxedFarmlandType(String cropName) {
        this.cropName = cropName;
    }

    public String cropName() {
        return cropName;
    }

    public String cruxItemId() {
        return "mysticalagradditions:" + cropName + "_crux";
    }

    public String farmlandRegistryName() {
        return cropName + "_cruxed_farmland";
    }
}