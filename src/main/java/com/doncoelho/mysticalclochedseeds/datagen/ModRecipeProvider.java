package com.doncoelho.mysticalclochedseeds.datagen;

import blusunrize.immersiveengineering.api.crafting.ClocheRecipe;
import com.blakebr0.mysticalagriculture.api.MysticalAgricultureAPI;
import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.doncoelho.mysticalclochedseeds.MysticalClochedSeeds;
import com.doncoelho.mysticalclochedseeds.tier.TierHelper;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Generates the Garden Cloche compatibility recipes for Mystical Agriculture crops.
 *
 * For each valid crop, generates ONE recipe per farmland tier (vanilla + 5 Essence
 * Farmlands), for up to 6 recipes per crop. Each recipe carries the growth time,
 * second-seed chance and outputs computed from the difference (farmland tier - seed tier).
 */
public class ModRecipeProvider extends RecipeProvider {

    private static final Logger LOGGER = LogUtils.getLogger();

    // Correspondencia exata das seis crops Tier 6 do Mystical Agradditions.
    private static final java.util.Set<String> TIER_SIX_CROPS = java.util.Set.of(
            "nether_star", "dragon_egg", "gaia_spirit",
            "awakened_draconium", "neutronium", "nitro_crystal"
    );

    // As quatro crops adicionais dependem tambem dos respectivos mods.
    private static final java.util.Map<String, String> TIER_SIX_EXTRA_MODS = java.util.Map.of(
            "gaia_spirit", "botania",
            "awakened_draconium", "draconicevolution",
            "neutronium", "avaritia",
            "nitro_crystal", "powah"
    );

    private static final ResourceLocation FERTILIZED_ESSENCE_ID =
            ResourceLocation.fromNamespaceAndPath("mysticalagriculture", "fertilized_essence");
    private static final ResourceLocation INFERIUM_CROP_ID =
            ResourceLocation.fromNamespaceAndPath("mysticalagriculture", "inferium");

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        Item fertilized = resolveFertilizedEssence();
        Collection<Crop> crops = MysticalAgricultureAPI.getCropRegistry().getCrops();

        int generated = 0;
        for (Crop crop : crops) {
            if (!isValidCrop(crop)) continue;

            int seedTier = TierHelper.getTierValue(crop.getTier());

            // Tier 6 crops require special Cruxed Farmland recipes.

            if (seedTier >= TierHelper.AGRADDITIONS_TIER) {
                if (seedTier == TierHelper.AGRADDITIONS_TIER
                        && "mysticalagradditions".equals(crop.getId().getNamespace())
                        && TIER_SIX_CROPS.contains(crop.getName())) {

                    String cropName = crop.getName();
                    ResourceLocation soilId = ResourceLocation.fromNamespaceAndPath(
                            MysticalClochedSeeds.MODID, cropName + "_cruxed_farmland"
                    );
                    Item soilItem = BuiltInRegistries.ITEM.getOptional(soilId).orElse(Items.AIR);

                    if (soilItem == Items.AIR) {
                        LOGGER.warn("[MysticalClochedSeeds] Missing Tier 6 Cruxed Farmland: {}", soilId);
                    } else {
                        try {
                            ClocheRecipe recipe = ClocheRecipeBuilder.build(
                                    crop, seedTier, TierHelper.AGRADDITIONS_TIER,
                                    soilItem, false, fertilized
                            );
                            ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(
                                    MysticalClochedSeeds.MODID,
                                    "cloche_" + cropName + "_from_" + cropName + "_cruxed"
                            );
                            String extraMod = TIER_SIX_EXTRA_MODS.get(cropName);

                            if (extraMod == null) {
                                recipeOutput.withConditions(
                                        new ModLoadedCondition("mysticalagradditions")
                                ).accept(recipeId, recipe, null);
                            } else {
                                recipeOutput.withConditions(
                                        new ModLoadedCondition("mysticalagradditions"),
                                        new ModLoadedCondition(extraMod)
                                ).accept(recipeId, recipe, null);
                            }
                            generated++;
                        } catch (Exception e) {
                            LOGGER.warn("[MysticalClochedSeeds] Tier 6 recipe failed for '{}': {}",
                                    cropName, e.getMessage());
                        }
                    }
                }
                continue;
            }


            boolean inferium = isInferium(crop);

            for (int farmlandTier : TierHelper.FARMLAND_TIERS) {
                Item soilItem = TierHelper.getSoilItemForTier(farmlandTier);
                if (soilItem == null || soilItem == Items.AIR) continue;

                try {
                    ClocheRecipe recipe = ClocheRecipeBuilder.build(
                            crop, seedTier, farmlandTier, soilItem, inferium, fertilized
                    );
                    ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                            MysticalClochedSeeds.MODID,
                            "cloche_" + crop.getName() + "_from_" + TierHelper.farmlandTierName(farmlandTier)
                    );
                    boolean requiresAgradditions =
                            "mysticalagradditions".equals(crop.getTier().getModId())
                                    || farmlandTier == TierHelper.AGRADDITIONS_TIER;

                    if (requiresAgradditions) {
                        recipeOutput.withConditions(
                                new ModLoadedCondition("mysticalagradditions")
                        ).accept(id, recipe, null);
                    } else {
                        recipeOutput.accept(id, recipe, null);
                    }
                    generated++;
                } catch (Exception e) {
                    // E.g. a crop whose block is not a valid crop block for the render function.
                    LOGGER.warn("[MysticalClochedSeeds] Failed to generate recipe for crop '{}' (farmland tier {}): {}",
                            crop.getName(), farmlandTier, e.getMessage());
                }
            }
        }

        LOGGER.info("[MysticalClochedSeeds] Generated {} Cloche recipes for {} crops.", generated, crops.size());
    }

    private boolean isValidCrop(Crop crop) {
        if (crop == null) return false;
        if (!crop.isEnabled()) return false;
        if (crop.getTier() == null) return false;
        if (crop.getSeedsItem() == null) return false;
        if (crop.getEssenceItem() == null) return false;
        if (crop.getCropBlock() == null) return false;
        return true;
    }

    private boolean isInferium(Crop crop) {
        return INFERIUM_CROP_ID.equals(crop.getId());
    }

    /** Looks up the Fertilized Essence item in the registry; returns null if absent (guarded in the builder). */
    private Item resolveFertilizedEssence() {
        Item item = BuiltInRegistries.ITEM.get(FERTILIZED_ESSENCE_ID);
        if (item == null || item == Items.AIR) {
            LOGGER.warn("[MysticalClochedSeeds] Item '{}' not found; Fertilized Essence will be omitted from recipes.",
                    FERTILIZED_ESSENCE_ID);
            return null;
        }
        return item;
    }
}