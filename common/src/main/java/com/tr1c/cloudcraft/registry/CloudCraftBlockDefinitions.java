package com.tr1c.cloudcraft.registry;

import com.tr1c.cloudcraft.block.RotatableBlock;
import com.tr1c.cloudcraft.block.custom.cloud_block.CumulusCloudBlock;
import com.tr1c.cloudcraft.block.custom.cloud_block.CumulusCloudBlockGas;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class CloudCraftBlockDefinitions {
    private static final Map<String, BlockFactory> DEFINITIONS = definitions();

    private CloudCraftBlockDefinitions() {
    }

    public static List<String> ids() {
        return List.copyOf(DEFINITIONS.keySet());
    }

    public static Block create(String id, BlockBehaviour.Properties properties, Holder<MobEffect> cloudWalkerEffect, Supplier<Item> fragmentItem, Supplier<BlockState> solidState) {
        BlockFactory factory = DEFINITIONS.get(id);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown block definition: " + id);
        }
        return factory.create(properties, cloudWalkerEffect, fragmentItem, solidState);
    }

    public static Block createCumulusCloudBlock(BlockBehaviour.Properties properties, Holder<MobEffect> cloudWalkerEffect, Supplier<Item> fragmentItem) {
        return new CumulusCloudBlock(properties.destroyTime(0.2F)
                .explosionResistance(0.1F)
                .sound(SoundType.SNOW), cloudWalkerEffect, fragmentItem);
    }

    public static CumulusCloudBlockGas createCloudBlockGas(BlockBehaviour.Properties properties, Supplier<BlockState> solidState, Holder<MobEffect> cloudWalkerEffect) {
        return new CumulusCloudBlockGas(properties, solidState.get(), cloudWalkerEffect);
    }

    public static Block createGasStateConverter(BlockBehaviour.Properties properties) {
        return new RotatableBlock(properties.destroyTime(1.0F).explosionResistance(6.0F), false);
    }

    private static Map<String, BlockFactory> definitions() {
        Map<String, BlockFactory> definitions = new LinkedHashMap<>();
        definitions.put(ModIds.CUMULUS_CLOUD_BLOCK, (properties, effect, fragment, solid) -> createCumulusCloudBlock(properties, effect, fragment));
        // Gas block construction receives the solid state's supplier from platform registries.
        definitions.put(ModIds.CUMULUS_CLOUD_BLOCK_GAS, (properties, effect, fragment, solid) -> createCloudBlockGas(properties, solid, effect));
        definitions.put(ModIds.GAS_STATE_CONVERTER, (properties, effect, fragment, solid) -> createGasStateConverter(properties));
        return Collections.unmodifiableMap(definitions);
    }

    @FunctionalInterface
    private interface BlockFactory {
        Block create(BlockBehaviour.Properties properties, Holder<MobEffect> cloudWalkerEffect, Supplier<Item> fragmentItem, Supplier<BlockState> solidState);
    }
}
