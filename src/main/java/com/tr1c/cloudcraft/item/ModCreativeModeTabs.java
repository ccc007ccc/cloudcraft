package com.tr1c.cloudcraft.item;

import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CloudCraft.MOD_ID);

    public static final Supplier<CreativeModeTab> CLOUD_TAB =
            CREATIVE_MODE_TABS.register("cloud_tab",() -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.CLOUD.get()))
                    .title(Component.translatable("itemGroup.cloudcraft"))
                    .displayItems((parameters, output) -> {
//                        output.accept(ModItems.CLOUD);
                        output.accept(ModBlocks.CLOUD_BLOCK);
                        output.accept(ModItems.CLOUD_FRAGMENT);
                    })
                    .build());


    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
