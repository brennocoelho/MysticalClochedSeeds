package com.doncoelho.mysticalclochedseeds.tier;

/**
 * Holds all the "math" of the balancing tables. Reads its numbers from
 * {@link ClocheConstants} (fixed values decided at data-generation time).
 *
 * Difference = farmlandTier - seedTier:
 *   difference >= 0 -> applies a time reduction (bonus)
 *   difference <  0 -> applies a time penalty
 */
public final class ClocheCalculator {

    private ClocheCalculator() {
    }

    // ===================== TIME =====================

    public static int calculateTime(int seedTier, int farmlandTier) {
        int base = ClocheConstants.BASE_TIME_TICKS;
        int diff = farmlandTier - seedTier;

        if (diff < 0) {
            return base + (Math.abs(diff) * ClocheConstants.PENALTY_PER_TIER_TICKS);
        }

        int reduction = getReductionForDiff(diff);
        int time = Math.round(base * (1.0f - reduction / 100.0f));
        return Math.max(1, time);
    }

    private static int getReductionForDiff(int diff) {
        return switch (diff) {
            case 0 -> ClocheConstants.REDUCTION_SAME_TIER_PERCENT;
            case 1 -> ClocheConstants.REDUCTION_PLUS_1_PERCENT;
            case 2 -> ClocheConstants.REDUCTION_PLUS_2_PERCENT;
            case 3 -> ClocheConstants.REDUCTION_PLUS_3_PERCENT;
            default -> ClocheConstants.REDUCTION_PLUS_4_PERCENT; // 4 or more
        };
    }

    // ===================== SEED DROP =====================

    /** Chance (0.0 - 1.0) of dropping a second seed. */
    public static float calculateSeedDropChance(int seedTier, int farmlandTier) {
        // Vanilla farmland (tier 0) never drops a second seed.
        if (farmlandTier == TierHelper.VANILLA_TIER) {
            return 0.0f;
        }
        if (farmlandTier == seedTier) {
            return ClocheConstants.SEED_CHANCE_SAME_TIER_PERCENT / 100.0f;
        }
        // Any other Essence Farmland (above or below the seed's tier).
        return ClocheConstants.SEED_CHANCE_OTHER_ESSENCE_PERCENT / 100.0f;
    }

    // ===================== FERTILIZED ESSENCE =====================

    /** Chance (0.0 - 1.0) of dropping Fertilized Essence -- identical on any farmland. */
    public static float fertilizedChance() {
        return ClocheConstants.FERTILIZED_ESSENCE_CHANCE_PERCENT / 100.0f;
    }

    // ===================== INFERIUM OUTPUT =====================

    /**
     * Calculates the Inferium Essence output.
     * Guaranteed base + a bonus of (bonusPerTier% * difference) for higher-tier farmland.
     * The integer part of the bonus becomes guaranteed output; the fractional part
     * becomes a secondary chance.
     *
     * Example (base 1, bonus 50%):
     *   diff 0 -> 1x guaranteed
     *   diff 1 -> 1x guaranteed + 50% chance of +1x
     *   diff 2 -> 2x guaranteed
     *   diff 3 -> 2x guaranteed + 50% chance of +1x
     *   diff 4 -> 3x guaranteed
     */
    public static InferiumOutput calculateInferiumOutput(int seedTier, int farmlandTier) {
        int base = ClocheConstants.INFERIUM_BASE_OUTPUT;
        int diff = farmlandTier - seedTier;

        if (diff <= 0) {
            // Same tier, vanilla, or lower: no output bonus.
            return new InferiumOutput(base, 0.0f);
        }

        float bonusTotal = base * (ClocheConstants.INFERIUM_BONUS_PER_TIER_PERCENT / 100.0f) * diff;
        int guaranteedBonus = (int) Math.floor(bonusTotal);
        float fraction = bonusTotal - guaranteedBonus;

        return new InferiumOutput(base + guaranteedBonus, fraction);
    }

    /**
     * Result of the Inferium output calculation.
     *
     * @param guaranteed  guaranteed amount (100% chance)
     * @param extraChance chance (0.0-1.0) of an additional +1x; 0 when there is no fraction
     */
    public record InferiumOutput(int guaranteed, float extraChance) {
    }
}