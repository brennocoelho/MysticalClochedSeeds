package com.doncoelho.mysticalclochedseeds.client;

import blusunrize.immersiveengineering.api.crafting.ClocheRecipe;
import com.doncoelho.mysticalclochedseeds.MysticalClochedSeeds;
import com.doncoelho.mysticalclochedseeds.tier.TierHelper;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

/**
 * Registers a tinted soil texture in the Garden Cloche for each Mystical Agriculture
 * Essence Farmland. The textures are the vanilla moist farmland multiplied by the
 * farmland tier color (the same tint Mystical Agriculture applies in the world).
 *
 * Must run at runtime (client side). IE keeps the soil textures in a static in-memory
 * list, so registering them during data generation has no effect in game.
 */
@EventBusSubscriber(modid = MysticalClochedSeeds.MODID, value = Dist.CLIENT)
public final class ClocheSoilTextures {

    private static final Logger LOGGER = LogUtils.getLogger();

    /** Index = essence farmland tier (1..6). Index 0 is unused (vanilla tier is handled by IE). */
    private static final String[] TIER_TEXTURE_NAMES = {
            null,
            "inferium",
            "prudentium",
            "tertium",
            "imperium",
            "supremium",
            "insanium"
    };

    private ClocheSoilTextures() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ClocheSoilTextures::register);
    }

    private static void register() {
        int registered = 0;
        for (int tier : TierHelper.FARMLAND_TIERS) {
            if (tier == TierHelper.VANILLA_TIER) continue;
            Block farmland = TierHelper.getFarmlandBlockForTier(tier);
            if (farmland == null) continue;
            Item item = farmland.asItem();
            if (item == Items.AIR) continue;

            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                    MysticalClochedSeeds.MODID,
                    "block/farmland_moist_" + TIER_TEXTURE_NAMES[tier]
            );
            ClocheRecipe.registerSoilTexture(Ingredient.of(item), texture);
            registered++;
        }
        LOGGER.info("[MysticalClochedSeeds] Registered {} Essence Farmland soil textures for the Garden Cloche.", registered);
    }
}
