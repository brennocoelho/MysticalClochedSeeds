
package com.doncoelho.mysticalclochedseeds.crux;

import com.doncoelho.mysticalclochedseeds.MysticalClochedSeeds;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(
                    Registries.CREATIVE_MODE_TAB,
                    MysticalClochedSeeds.MODID
            );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab>
            MAIN_TAB = TABS.register(
            "main",
            () -> CreativeModeTab.builder()

                    .title(Component.translatable(
                            "itemGroup.mysticalclochedseeds"
                    ))

                    .icon(() -> new ItemStack(
                            ModBlocks.CRUXED_FARMLANDS
                                    .get(CruxedFarmlandType.NETHER_STAR)
                                    .get()
                    ))

                    .displayItems((parameters, output) -> {

                        for (var block : ModBlocks.CRUXED_FARMLANDS.values()) {
                            output.accept(block.get());
                        }

                    })

                    .build()
    );

    private ModCreativeTabs() {
    }

    public static void register(IEventBus modEventBus) {
        TABS.register(modEventBus);
    }
}
