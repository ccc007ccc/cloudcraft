package com.tr1c.cloudcraft.registry;

import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public final class CloudCraftCreativeTabContents {
    private CloudCraftCreativeTabContents() {
    }

    public static void populate(
            CreativeModeTab.Output output,
            Function<String, Item> itemById,
            Function<String, Block> blockById,
            Holder<Potion> solidCloudPotion) {
        for (String id : CloudCraftRegistryDefinitions.itemIds()) {
            output.accept(itemById.apply(id));
        }

        output.accept(PotionContents.createItemStack(Items.POTION, solidCloudPotion));
        output.accept(PotionContents.createItemStack(Items.SPLASH_POTION, solidCloudPotion));
        output.accept(PotionContents.createItemStack(Items.LINGERING_POTION, solidCloudPotion));

        for (String id : CloudCraftRegistryDefinitions.blockItemIds()) {
            output.accept(blockById.apply(id));
        }
    }
}
