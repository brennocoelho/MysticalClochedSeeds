
package com.doncoelho.mysticalclochedseeds.crux;

import com.doncoelho.mysticalclochedseeds.MysticalClochedSeeds;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.Map;

public final class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MysticalClochedSeeds.MODID);

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MysticalClochedSeeds.MODID);

    public static final Map<CruxedFarmlandType,
            DeferredBlock<CruxedFarmlandBlock>> CRUXED_FARMLANDS =
            new EnumMap<>(CruxedFarmlandType.class);

    static {
        for (CruxedFarmlandType type : CruxedFarmlandType.values()) {

            DeferredBlock<CruxedFarmlandBlock> block =
                    BLOCKS.registerBlock(
                            type.farmlandRegistryName(),
                            CruxedFarmlandBlock::new,
                            BlockBehaviour.Properties.ofFullCopy(
                                    Blocks.FARMLAND
                            )
                    );

            CRUXED_FARMLANDS.put(type, block);

            // Register the corresponding inventory item.
            ITEMS.registerSimpleBlockItem(block);
        }
    }

    private ModBlocks() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}
