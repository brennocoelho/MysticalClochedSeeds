
package com.doncoelho.mysticalclochedseeds.tier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClocheCalculatorTest {

    @Test
    void vanillaDirtShouldIncreaseGrowthTime() {
        // Inferium Seed (Tier 1) + Vanilla Dirt (Tier 0)
        assertEquals(1400, ClocheCalculator.calculateTime(1, 0));
    }

    @Test
    void matchingFarmlandShouldReduceGrowthTimeBy20Percent() {
        // Inferium Seed + Inferium Farmland
        assertEquals(960, ClocheCalculator.calculateTime(1, 1));
    }

    @Test
    void higherTierFarmlandShouldIncreaseGrowthSpeed() {
        // Inferium Seed + Prudentium Farmland
        assertEquals(780, ClocheCalculator.calculateTime(1, 2));

        // Inferium Seed + Supremium Farmland
        assertEquals(480, ClocheCalculator.calculateTime(1, 5));
    }

    @Test
    void insaniumFarmlandShouldRespectMaximumReduction() {
        // Inferium Seed + Insanium Farmland
        // The maximum reduction remains capped at 60%.
        assertEquals(480, ClocheCalculator.calculateTime(1, 6));
    }

    @Test
    void lowerTierFarmlandShouldApplyPenalty() {
        // Supremium Seed (Tier 5) + Inferium Farmland (Tier 1)
        assertEquals(2000, ClocheCalculator.calculateTime(5, 1));
    }


    @Test
    void vanillaDirtShouldNeverDropExtraSeeds() {
        assertEquals(
                0.0f,
                ClocheCalculator.calculateSeedDropChance(1, 0),
                0.0001f
        );
    }

    @Test
    void matchingFarmlandShouldHave20PercentSeedChance() {
        assertEquals(
                0.20f,
                ClocheCalculator.calculateSeedDropChance(3, 3),
                0.0001f
        );
    }

    @Test
    void differentFarmlandTiersShouldHave10PercentSeedChance() {
        // Higher-tier farmland
        assertEquals(
                0.10f,
                ClocheCalculator.calculateSeedDropChance(1, 6),
                0.0001f
        );

        // Lower-tier farmland
        assertEquals(
                0.10f,
                ClocheCalculator.calculateSeedDropChance(5, 2),
                0.0001f
        );
    }

    @Test
    void fertilizedEssenceShouldHave10PercentChance() {
        assertEquals(
                0.10f,
                ClocheCalculator.fertilizedChance(),
                0.0001f
        );
    }


    @Test
    void vanillaAndMatchingFarmlandShouldNotIncreaseInferiumOutput() {
        var vanilla = ClocheCalculator.calculateInferiumOutput(1, 0);
        var matching = ClocheCalculator.calculateInferiumOutput(1, 1);

        assertEquals(1, vanilla.guaranteed());
        assertEquals(0.0f, vanilla.extraChance(), 0.0001f);

        assertEquals(1, matching.guaranteed());
        assertEquals(0.0f, matching.extraChance(), 0.0001f);
    }

    @Test
    void higherTierFarmlandShouldIncreaseInferiumOutput() {
        // Prudentium Farmland: +50%
        var prudentium = ClocheCalculator.calculateInferiumOutput(1, 2);

        assertEquals(1, prudentium.guaranteed());
        assertEquals(0.5f, prudentium.extraChance(), 0.0001f);

        // Supremium Farmland: +200%
        var supremium = ClocheCalculator.calculateInferiumOutput(1, 5);

        assertEquals(3, supremium.guaranteed());
        assertEquals(0.0f, supremium.extraChance(), 0.0001f);
    }

    @Test
    void insaniumFarmlandShouldProvideMaximumInferiumBonus() {
        // Inferium Seed (Tier 1) + Insanium Farmland (Tier 6)
        // Base 1 + 250% bonus = 3 guaranteed + 50% chance of 1 extra.
        var result = ClocheCalculator.calculateInferiumOutput(1, 6);

        assertEquals(3, result.guaranteed());
        assertEquals(0.5f, result.extraChance(), 0.0001f);
    }

}
