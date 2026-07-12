package com.doncoelho.mysticalclochedseeds.tier;

/**
 * Fixed balancing values for the Garden Cloche <-> Mystical Agriculture compatibility.
 *
 * These are the tuning "knobs" of the whole system. They were previously exposed
 * through a runtime config file, but because recipes are produced via data generation
 * (static JSON baked at build time), a runtime config could never actually affect them.
 * Keeping the values as code constants is the honest representation: they are decided
 * here, at generation time, and a maintainer changes balance by editing this file and
 * re-running data generation.
 *
 * All percentage values are expressed as whole numbers (e.g. 20 == 20%) and converted
 * to fractions where needed by {@link ClocheCalculator}. All time values are in ticks
 * (20 ticks == 1 second).
 */
public final class ClocheConstants {

    private ClocheConstants() {
    }

    // ===================== GROWTH TIME =====================

    /** Base growth time in ticks (baseline, before any bonus or penalty). */
    public static final int BASE_TIME_TICKS = 1200;

    /** Time reduction (%) when farmland tier == seed tier (difference 0). */
    public static final int REDUCTION_SAME_TIER_PERCENT = 20;

    /** Time reduction (%) when farmland is 1 tier above the seed. */
    public static final int REDUCTION_PLUS_1_PERCENT = 35;

    /** Time reduction (%) when farmland is 2 tiers above the seed. */
    public static final int REDUCTION_PLUS_2_PERCENT = 45;

    /** Time reduction (%) when farmland is 3 tiers above the seed. */
    public static final int REDUCTION_PLUS_3_PERCENT = 55;

    /** Time reduction (%) when farmland is 4 tiers above the seed. */
    public static final int REDUCTION_PLUS_4_PERCENT = 60;

    /** Additional ticks per tier that the farmland sits BELOW the seed's tier. */
    public static final int PENALTY_PER_TIER_TICKS = 200;

    // ===================== SEED DROP =====================

    /** Chance (%) of a second seed on an Essence Farmland of the same tier as the seed. */
    public static final int SEED_CHANCE_SAME_TIER_PERCENT = 20;

    /**
     * Chance (%) of a second seed on any other Essence Farmland (tier different from the seed).
     * Does not apply to vanilla farmland (tier 0), which never drops a second seed.
     */
    public static final int SEED_CHANCE_OTHER_ESSENCE_PERCENT = 10;

    // ===================== FERTILIZED ESSENCE =====================

    /** Chance (%) of dropping Fertilized Essence on harvest, on any farmland. */
    public static final int FERTILIZED_ESSENCE_CHANCE_PERCENT = 10;

    // ===================== INFERIUM OUTPUT =====================

    /** Guaranteed base amount of Inferium Essence per harvest. */
    public static final int INFERIUM_BASE_OUTPUT = 1;

    /**
     * Output bonus (%) per farmland tier above the seed's tier.
     * Per the wiki: 100% base + 50% per tier. The integer part of the accumulated
     * bonus becomes guaranteed output; the fractional part becomes a secondary chance.
     */
    public static final int INFERIUM_BONUS_PER_TIER_PERCENT = 50;
}