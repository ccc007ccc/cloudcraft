package com.tr1c.cloudcraft.item;

import com.tr1c.cloudcraft.block.FabricModBlocks;
import com.tr1c.cloudcraft.potion.FabricModPotions;
import com.tr1c.cloudcraft.registry.CloudCraftCreativeTabContents;
import com.tr1c.cloudcraft.registry.ModIds;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class FabricModCreativeModeTabs {
    public static final ResourceKey<CreativeModeTab> CLOUD_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            ModIds.id(ModIds.CLOUD_TAB));

    public static final CreativeModeTab CLOUD_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(FabricModItems.CLOUD))
            .title(Component.translatable("itemGroup.cloudcraft"))
            .displayItems((parameters, output) -> CloudCraftCreativeTabContents.populate(
                    output,
                    FabricModItems::itemById,
                    FabricModBlocks::blockById,
                    FabricModPotions.SOLID_CLOUD))
            .build();

    private FabricModCreativeModeTabs() {
    }
}
