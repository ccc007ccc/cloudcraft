package com.tr1c.cloudcraft.registry;

import com.tr1c.cloudcraft.CloudCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;

public final class ModIds {
    public static final String CLOUD = "cloud";
    public static final String CUMULUS_CLOUD_FRAGMENT = "cumulus_cloud_fragment";
    public static final String CUMULUS_CLOUD_BLOCK = "cumulus_cloud_block";
    public static final String CUMULUS_CLOUD_BLOCK_GAS = "cumulus_cloud_block_gas";
    public static final String GAS_STATE_CONVERTER = "gas_state_converter";
    public static final String CLOUD_WALKER = "cloud_walker";
    public static final String SOLID_CLOUD_POTION = "solid_cloud";
    public static final String SOLID_CLOUD_POTION_BOTTLE_MODEL = "potion_bottle_solid_cloud";
    public static final String CLOUD_TAB = "cloud_tab";

    private ModIds() {
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(CloudCraft.MOD_ID, path);
    }

    public static ResourceKey<Block> blockKey(String path) {
        return ResourceKey.create(Registries.BLOCK, id(path));
    }

    public static ResourceKey<Item> itemKey(String path) {
        return ResourceKey.create(Registries.ITEM, id(path));
    }

    public static ResourceKey<Potion> potionKey(String path) {
        return ResourceKey.create(Registries.POTION, id(path));
    }
}
