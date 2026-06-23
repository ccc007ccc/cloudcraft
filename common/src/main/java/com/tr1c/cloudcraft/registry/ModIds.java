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
    public static final String COMPRESSED_CANISTER = "compressed_canister";
    public static final String CIRRUS_FILAMENT = "cirrus_filament";
    public static final String STORM_CORE = "storm_core";
    public static final String BASIC_JETPACK_FRAME = "basic_jetpack_frame";
    public static final String STABILIZED_NOZZLE = "stabilized_nozzle";
    public static final String HIGH_PRESSURE_CHAMBER = "high_pressure_chamber";
    public static final String CLOUD_JETPACK = "cloud_jetpack";
    public static final String STABILIZED_CLOUD_JETPACK = "stabilized_cloud_jetpack";
    public static final String HIGH_PRESSURE_CLOUD_JETPACK = "high_pressure_cloud_jetpack";
    public static final String CUMULUS_CLOUD_BLOCK = "cumulus_cloud_block";
    public static final String CUMULUS_CLOUD_BLOCK_GAS = "cumulus_cloud_block_gas";
    public static final String STRATUS_CLOUD_BLOCK = "stratus_cloud_block";
    public static final String STRATUS_CLOUD_BLOCK_GAS = "stratus_cloud_block_gas";
    public static final String CIRRUS_CLOUD_BLOCK = "cirrus_cloud_block";
    public static final String CIRRUS_CLOUD_BLOCK_GAS = "cirrus_cloud_block_gas";
    public static final String ALTOSTRATUS_CLOUD_BLOCK = "altostratus_cloud_block";
    public static final String ALTOSTRATUS_CLOUD_BLOCK_GAS = "altostratus_cloud_block_gas";
    public static final String NIMBOSTRATUS_CLOUD_BLOCK = "nimbostratus_cloud_block";
    public static final String NIMBOSTRATUS_CLOUD_BLOCK_GAS = "nimbostratus_cloud_block_gas";
    public static final String CUMULONIMBUS_CLOUD_BLOCK = "cumulonimbus_cloud_block";
    public static final String CUMULONIMBUS_CLOUD_BLOCK_GAS = "cumulonimbus_cloud_block_gas";
    public static final String GAS_STATE_CONVERTER = "gas_state_converter";
    public static final String CLOUD_WALKER = "cloud_walker";
    public static final String SOLID_CLOUD_POTION = "solid_cloud";
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
