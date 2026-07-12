package com.doncoelho.mysticalclochedseeds.tier;

import com.blakebr0.mysticalagriculture.api.crop.CropTier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

/**
 * Single source of truth for tier logic and the MA -> number mapping.
 *
 * Tier convention:
 *   0 = Vanilla farmland (soil = DIRT in the Cloche)
 *   1 = Inferium
 *   2 = Prudentium
 *   3 = Tertium
 *   4 = Imperium
 *   5 = Supremium
 *
 * MA's ELEMENTAL tier has value == 1, so it is treated as tier 1.
 */
public final class TierHelper {

    public static final int VANILLA_TIER = 0;
    public static final int MIN_ESSENCE_TIER = 1;
    public static final int MAX_ESSENCE_TIER = 5;

    /** All farmland tiers we generate recipes for (vanilla + 1..5). */
    public static final int[] FARMLAND_TIERS = {0, 1, 2, 3, 4, 5};

    private TierHelper() {
    }

    /**
     * Normalizes an MA CropTier to the numeric value 0-5.
     * ELEMENTAL (value 1) and ONE (value 1) collapse to 1.
     */
    public static int getTierValue(CropTier tier) {
        if (tier == null) return MIN_ESSENCE_TIER;
        return tier.getValue();
    }

    /**
     * Returns the farmland block matching an Essence Farmland tier (1-5).
     * Returns null for tier 0 (vanilla has no dedicated farmland block here) or an invalid tier.
     */
    public static Block getFarmlandBlockForTier(int tier) {
        return switch (tier) {
            case 1 -> CropTier.ONE.getFarmland();
            case 2 -> CropTier.TWO.getFarmland();
            case 3 -> CropTier.THREE.getFarmland();
            case 4 -> CropTier.FOUR.getFarmland();
            case 5 -> CropTier.FIVE.getFarmland();
            default -> null;
        };
    }

    /**
     * Returns the soil ITEM the player inserts into the Garden Cloche for a given tier.
     *   tier 0 -> DIRT (the Cloche's default soil for vanilla crops)
     *   tier 1-5 -> the matching Essence Farmland item
     * Returns null if the farmland has no valid item.
     */
    public static Item getSoilItemForTier(int tier) {
        if (tier == VANILLA_TIER) {
            return Items.DIRT;
        }
        Block farmland = getFarmlandBlockForTier(tier);
        if (farmland == null) return null;
        Item item = farmland.asItem();
        if (item == null || item == Items.AIR) return null;
        return item;
    }

    /** Human-readable name of the farmland tier, used to build unique recipe IDs. */
    public static String farmlandTierName(int tier) {
        return switch (tier) {
            case 0 -> "vanilla";
            case 1 -> "inferium";
            case 2 -> "prudentium";
            case 3 -> "tertium";
            case 4 -> "imperium";
            case 5 -> "supremium";
            default -> "tier" + tier;
        };
    }
}