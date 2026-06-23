package com.tr1c.cloudcraft.registry;

import com.tr1c.cloudcraft.block.custom.GasStateConverterBlock;
import com.tr1c.cloudcraft.block.custom.cloud_block.CloudBlock;
import com.tr1c.cloudcraft.block.custom.cloud_block.CloudMotionRules;
import com.tr1c.cloudcraft.block.custom.cloud_block.CumulusCloudBlock;
import com.tr1c.cloudcraft.block.custom.cloud_block.CumulusCloudBlockGas;
import com.tr1c.cloudcraft.block.custom.cloud_block.MotionGasCloudBlock;
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

    public static Block create(String id, BlockBehaviour.Properties properties, Holder<MobEffect> cloudWalkerEffect, Supplier<Item> fragmentItem, SolidStateLookup solidStateLookup) {
        BlockFactory factory = DEFINITIONS.get(id);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown block definition: " + id);
        }
        return factory.create(id, properties, cloudWalkerEffect, fragmentItem, solidStateLookup);
    }

    public static Block createCumulusCloudBlock(BlockBehaviour.Properties properties, Holder<MobEffect> cloudWalkerEffect, Supplier<Item> fragmentItem) {
        return new CumulusCloudBlock(cloudProperties(properties), cloudWalkerEffect, fragmentItem);
    }

    public static Block createCloudBlock(BlockBehaviour.Properties properties, Holder<MobEffect> cloudWalkerEffect, Supplier<Item> fragmentItem) {
        return new CloudBlock(cloudProperties(properties), cloudWalkerEffect, fragmentItem);
    }

    public static CumulusCloudBlockGas createCumulusCloudBlockGas(BlockBehaviour.Properties properties, BlockState solidState, Holder<MobEffect> cloudWalkerEffect) {
        return new CumulusCloudBlockGas(gasCloudProperties(properties), solidState, cloudWalkerEffect);
    }

    public static MotionGasCloudBlock createMotionGasCloudBlock(BlockBehaviour.Properties properties, BlockState solidState, Holder<MobEffect> cloudWalkerEffect, CloudMotionRules.Motion motion) {
        return new MotionGasCloudBlock(gasCloudProperties(properties), solidState, cloudWalkerEffect, motion);
    }

    public static Block createGasStateConverter(BlockBehaviour.Properties properties) {
        return new GasStateConverterBlock(properties.destroyTime(1.0F).explosionResistance(6.0F));
    }

    private static BlockBehaviour.Properties cloudProperties(BlockBehaviour.Properties properties) {
        return properties.destroyTime(0.2F)
                .explosionResistance(0.1F)
                .sound(SoundType.SNOW);
    }

    private static BlockBehaviour.Properties gasCloudProperties(BlockBehaviour.Properties properties) {
        return properties.destroyTime(0.2F)
                .explosionResistance(0.1F)
                .sound(SoundType.SNOW)
                .friction(0.995F);
    }

    private static Map<String, BlockFactory> definitions() {
        Map<String, BlockFactory> definitions = new LinkedHashMap<>();
        definitions.put(ModIds.CUMULUS_CLOUD_BLOCK, (id, properties, effect, fragment, solid) -> createCumulusCloudBlock(properties, effect, fragment));
        definitions.put(ModIds.CUMULUS_CLOUD_BLOCK_GAS, (id, properties, effect, fragment, solid) -> createCumulusCloudBlockGas(properties, solid.get(ModIds.CUMULUS_CLOUD_BLOCK), effect));
        definitions.put(ModIds.STRATUS_CLOUD_BLOCK, (id, properties, effect, fragment, solid) -> createCloudBlock(properties, effect, fragment));
        definitions.put(ModIds.STRATUS_CLOUD_BLOCK_GAS, (id, properties, effect, fragment, solid) -> createMotionGasCloudBlock(properties, solid.get(ModIds.STRATUS_CLOUD_BLOCK), effect, CloudMotionRules.STRATUS_GAS));
        definitions.put(ModIds.CIRRUS_CLOUD_BLOCK, (id, properties, effect, fragment, solid) -> createCloudBlock(properties, effect, fragment));
        definitions.put(ModIds.CIRRUS_CLOUD_BLOCK_GAS, (id, properties, effect, fragment, solid) -> createMotionGasCloudBlock(properties, solid.get(ModIds.CIRRUS_CLOUD_BLOCK), effect, CloudMotionRules.CIRRUS_GAS));
        definitions.put(ModIds.ALTOSTRATUS_CLOUD_BLOCK, (id, properties, effect, fragment, solid) -> createCloudBlock(properties, effect, fragment));
        definitions.put(ModIds.ALTOSTRATUS_CLOUD_BLOCK_GAS, (id, properties, effect, fragment, solid) -> createMotionGasCloudBlock(properties, solid.get(ModIds.ALTOSTRATUS_CLOUD_BLOCK), effect, CloudMotionRules.ALTOSTRATUS_GAS));
        definitions.put(ModIds.NIMBOSTRATUS_CLOUD_BLOCK, (id, properties, effect, fragment, solid) -> createCloudBlock(properties, effect, fragment));
        definitions.put(ModIds.NIMBOSTRATUS_CLOUD_BLOCK_GAS, (id, properties, effect, fragment, solid) -> createMotionGasCloudBlock(properties, solid.get(ModIds.NIMBOSTRATUS_CLOUD_BLOCK), effect, CloudMotionRules.NIMBOSTRATUS_GAS));
        definitions.put(ModIds.CUMULONIMBUS_CLOUD_BLOCK, (id, properties, effect, fragment, solid) -> createCloudBlock(properties, effect, fragment));
        definitions.put(ModIds.CUMULONIMBUS_CLOUD_BLOCK_GAS, (id, properties, effect, fragment, solid) -> createMotionGasCloudBlock(properties, solid.get(ModIds.CUMULONIMBUS_CLOUD_BLOCK), effect, CloudMotionRules.CUMULONIMBUS_GAS));
        definitions.put(ModIds.GAS_STATE_CONVERTER, (id, properties, effect, fragment, solid) -> createGasStateConverter(properties));
        return Collections.unmodifiableMap(definitions);
    }

    @FunctionalInterface
    private interface BlockFactory {
        Block create(String id, BlockBehaviour.Properties properties, Holder<MobEffect> cloudWalkerEffect, Supplier<Item> fragmentItem, SolidStateLookup solidStateLookup);
    }

    @FunctionalInterface
    public interface SolidStateLookup {
        BlockState get(String id);
    }
}
