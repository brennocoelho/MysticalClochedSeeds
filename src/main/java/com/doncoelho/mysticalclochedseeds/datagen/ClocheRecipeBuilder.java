package com.doncoelho.mysticalclochedseeds.datagen;

import blusunrize.immersiveengineering.api.crafting.ClocheRecipe;
import blusunrize.immersiveengineering.api.crafting.ClocheRenderFunction;
import blusunrize.immersiveengineering.api.crafting.StackWithChance;
import blusunrize.immersiveengineering.client.utils.ClocheRenderFunctions;
import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.doncoelho.mysticalclochedseeds.tier.ClocheCalculator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the "technical assembly" of a ClocheRecipe:
 * creation of the Ingredient, StackWithChance, FluidIngredient and render function.
 *
 * The guaranteed essence output is ALWAYS the first item in the list, because
 * ClocheRecipe's constructor uses outputs.get(0) as the main result.
 */
public final class ClocheRecipeBuilder {

    /** Fixed guaranteed essence amount for Resource Crops (non-Inferium). */
    private static final int RESOURCE_ESSENCE_AMOUNT = 1;

    private ClocheRecipeBuilder() {
    }

    public static ClocheRecipe build(
            Crop crop,
            int seedTier,
            int farmlandTier,
            Item soilItem,
            boolean isInferium,
            @Nullable Item fertilizedEssence
    ) {
        Item essence = crop.getEssenceItem();
        Item seedItem = crop.getSeedsItem();
        Block cropBlock = crop.getCropBlock();

        List<StackWithChance> outputs = new ArrayList<>();

        // --- 1. Guaranteed essence output (always first) ---
        if (isInferium) {
            ClocheCalculator.InferiumOutput out =
                    ClocheCalculator.calculateInferiumOutput(seedTier, farmlandTier);
            outputs.add(new StackWithChance(new ItemStack(essence, out.guaranteed()), 1.0f));
            if (out.extraChance() > 0.0f) {
                outputs.add(new StackWithChance(new ItemStack(essence, 1), out.extraChance()));
            }
        } else {
            outputs.add(new StackWithChance(new ItemStack(essence, RESOURCE_ESSENCE_AMOUNT), 1.0f));
        }

        // --- 2. Second seed (chance) ---
        float seedChance = ClocheCalculator.calculateSeedDropChance(seedTier, farmlandTier);
        if (seedChance > 0.0f && seedItem != null) {
            outputs.add(new StackWithChance(new ItemStack(seedItem, 1), seedChance));
        }

        // --- 3. Fertilized Essence (chance) ---
        float fertChance = ClocheCalculator.fertilizedChance();
        if (fertChance > 0.0f && fertilizedEssence != null) {
            outputs.add(new StackWithChance(new ItemStack(fertilizedEssence, 1), fertChance));
        }

        // --- 4. Final assembly ---
        int time = ClocheCalculator.calculateTime(seedTier, farmlandTier);
        Ingredient seed = Ingredient.of(seedItem);
        Ingredient soil = Ingredient.of(soilItem);
        FluidIngredient water = FluidIngredient.of(new FluidStack(Fluids.WATER, 1));
        ClocheRenderFunction render = new ClocheRenderFunctions.RenderFunctionCrop(cropBlock);

        return new ClocheRecipe(outputs, seed, soil, time, water, render);
    }
}