package com.tr1c.cloudcraft.item;

import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.block.NeoForgeModBlocks;
import com.tr1c.cloudcraft.potion.NeoForgeModPotions;
import com.tr1c.cloudcraft.registry.CloudCraftCreativeTabContents;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class NeoForgeModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CloudCraft.MOD_ID);

    public static final Supplier<CreativeModeTab> CLOUD_TAB = CREATIVE_MODE_TABS.register(ModIds.CLOUD_TAB, () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(NeoForgeModItems.CLOUD.get()))
            .title(Component.translatable("itemGroup.cloudcraft"))
            .displayItems((parameters, output) -> CloudCraftCreativeTabContents.populate(
                    output,
                    NeoForgeModItems::itemById,
                    NeoForgeModBlocks::blockById,
                    NeoForgeModPotions.SOLID_CLOUD))
            .build());

    private NeoForgeModCreativeModeTabs() {
    }

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
