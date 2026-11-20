
package com.doncoelho.mysticalclochedseeds.crux;

import com.doncoelho.mysticalclochedseeds.MysticalClochedSeeds;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;

public final class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MysticalClochedSeeds.MODID);

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MysticalClochedSeeds.MODID);

    public static final Map<CruxedFarmlandType,
            DeferredBlock<CruxedFarmlandBlock>> CRUXED_FARMLANDS =
            new EnumMap<>(CruxedFarmlandType.class);

    private ModBlocks() {
    }

    /**
     * Registers only the farmlands whose required mods
     * are present in the current modpack.
     *
     * Must be called during mod construction, before
     * NeoForge fires its registry events.
     */
    public static void register(IEventBus modEventBus) {

        Predicate<String> modLoaded = ModList.get()::isLoaded;

        for (CruxedFarmlandType type : CruxedFarmlandType.values()) {

            if (!type.isAvailable(modLoaded)) {
                continue;
            }

            DeferredBlock<CruxedFarmlandBlock> block =
                    BLOCKS.registerBlock(
                            type.farmlandRegistryName(),
                            CruxedFarmlandBlock::new,
                            BlockBehaviour.Properties.ofFullCopy(
                                    Blocks.FARMLAND
                            )
                    );

            CRUXED_FARMLANDS.put(type, block);

            ITEMS.registerSimpleBlockItem(block);
        }

        if (CRUXED_FARMLANDS.isEmpty()) {
            return;
        }

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }

    /**
     * Returns true if at least one farmland has been
     * scheduled for registration.
     */
    public static boolean hasRegisteredFarmlands() {
        return !CRUXED_FARMLANDS.isEmpty();
    }
}
