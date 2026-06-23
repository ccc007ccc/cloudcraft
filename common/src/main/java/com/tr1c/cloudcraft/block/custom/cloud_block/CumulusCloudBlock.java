package com.tr1c.cloudcraft.block.custom.cloud_block;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class CumulusCloudBlock extends CloudBlock {
    public CumulusCloudBlock(Properties properties, Holder<MobEffect> cloudWalkerEffect, Supplier<Item> fragmentItem) {
        super(properties, cloudWalkerEffect, fragmentItem);
    }
}
