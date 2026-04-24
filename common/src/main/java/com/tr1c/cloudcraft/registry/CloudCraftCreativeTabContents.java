package com.tr1c.cloudcraft.registry;

import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Block;

public final class CloudCraftCreativeTabContents {
    private CloudCraftCreativeTabContents() {
    }

    public static void populate(
            CreativeModeTab.Output output,
            Item cloud,
            Item cumulusCloudFragment,
            Holder<Potion> solidCloudPotion,
            Block cumulusCloudBlock,
            Block cumulusCloudBlockGas,
            Block gasStateConverter) {
        output.accept(cloud);
        output.accept(cumulusCloudFragment);

        output.accept(PotionContents.createItemStack(Items.POTION, solidCloudPotion));
        output.accept(PotionContents.createItemStack(Items.SPLASH_POTION, solidCloudPotion));
        output.accept(PotionContents.createItemStack(Items.LINGERING_POTION, solidCloudPotion));

        output.accept(cumulusCloudBlock);
        output.accept(cumulusCloudBlockGas);
        output.accept(gasStateConverter);
    }
}
