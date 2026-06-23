package com.tr1c.cloudcraft.block;

import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.block.custom.cloud_block.CumulusCloudBlockGas;
import com.tr1c.cloudcraft.block.custom.cloud_block.MotionGasCloudBlock;
import com.tr1c.cloudcraft.effect.NeoForgeModEffects;
import com.tr1c.cloudcraft.item.NeoForgeModItems;
import com.tr1c.cloudcraft.registry.CloudCraftBlockDefinitions;
import com.tr1c.cloudcraft.registry.CloudCraftRegistryDefinitions;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class NeoForgeModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CloudCraft.MOD_ID);
    private static final Map<String, DeferredBlock<? extends Block>> BLOCK_BY_ID = registerBlocks();

    public static final DeferredBlock<Block> CUMULUS_CLOUD_BLOCK = deferredBlockById(ModIds.CUMULUS_CLOUD_BLOCK);
    public static final DeferredBlock<CumulusCloudBlockGas> CUMULUS_CLOUD_BLOCK_GAS = deferredBlockById(
            ModIds.CUMULUS_CLOUD_BLOCK_GAS);
    public static final DeferredBlock<Block> STRATUS_CLOUD_BLOCK = deferredBlockById(ModIds.STRATUS_CLOUD_BLOCK);
    public static final DeferredBlock<MotionGasCloudBlock> STRATUS_CLOUD_BLOCK_GAS = deferredBlockById(ModIds.STRATUS_CLOUD_BLOCK_GAS);
    public static final DeferredBlock<Block> CIRRUS_CLOUD_BLOCK = deferredBlockById(ModIds.CIRRUS_CLOUD_BLOCK);
    public static final DeferredBlock<MotionGasCloudBlock> CIRRUS_CLOUD_BLOCK_GAS = deferredBlockById(ModIds.CIRRUS_CLOUD_BLOCK_GAS);
    public static final DeferredBlock<Block> ALTOSTRATUS_CLOUD_BLOCK = deferredBlockById(ModIds.ALTOSTRATUS_CLOUD_BLOCK);
    public static final DeferredBlock<MotionGasCloudBlock> ALTOSTRATUS_CLOUD_BLOCK_GAS = deferredBlockById(ModIds.ALTOSTRATUS_CLOUD_BLOCK_GAS);
    public static final DeferredBlock<Block> NIMBOSTRATUS_CLOUD_BLOCK = deferredBlockById(ModIds.NIMBOSTRATUS_CLOUD_BLOCK);
    public static final DeferredBlock<MotionGasCloudBlock> NIMBOSTRATUS_CLOUD_BLOCK_GAS = deferredBlockById(ModIds.NIMBOSTRATUS_CLOUD_BLOCK_GAS);
    public static final DeferredBlock<Block> CUMULONIMBUS_CLOUD_BLOCK = deferredBlockById(ModIds.CUMULONIMBUS_CLOUD_BLOCK);
    public static final DeferredBlock<MotionGasCloudBlock> CUMULONIMBUS_CLOUD_BLOCK_GAS = deferredBlockById(ModIds.CUMULONIMBUS_CLOUD_BLOCK_GAS);
    public static final DeferredBlock<Block> GAS_STATE_CONVERTER = deferredBlockById(ModIds.GAS_STATE_CONVERTER);

    private NeoForgeModBlocks() {
    }

    private static Map<String, DeferredBlock<? extends Block>> registerBlocks() {
        Map<String, DeferredBlock<? extends Block>> blocks = new LinkedHashMap<>();
        Set<String> blockItemIds = Set.copyOf(CloudCraftRegistryDefinitions.blockItemIds());

        for (String id : CloudCraftRegistryDefinitions.blockIds()) {
            DeferredBlock<? extends Block> block = BLOCKS.register(id, () -> createBlock(id, blocks));
            if (blockItemIds.contains(id)) {
                NeoForgeModItems.ITEMS.register(id,
                        () -> new BlockItem(block.get(), new Item.Properties().setId(ModIds.itemKey(id))));
            }
            blocks.put(id, block);
        }

        return Map.copyOf(blocks);
    }

    private static Block createBlock(String id, Map<String, DeferredBlock<? extends Block>> blocks) {
        return CloudCraftBlockDefinitions.create(
                id,
                BlockBehaviour.Properties.of().setId(ModIds.blockKey(id)),
                NeoForgeModEffects.CLOUD_WALKER,
                () -> NeoForgeModItems.CUMULUS_CLOUD_FRAGMENT.get(),
                solidId -> blocks.get(solidId).get().defaultBlockState());
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }

    public static Block blockById(String id) {
        DeferredBlock<? extends Block> block = BLOCK_BY_ID.get(id);
        if (block == null) {
            throw new IllegalArgumentException("Unknown block: " + id);
        }
        return block.get();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Block> DeferredBlock<T> deferredBlockById(String id) {
        DeferredBlock<? extends Block> block = BLOCK_BY_ID.get(id);
        if (block == null) {
            throw new IllegalArgumentException("Unknown block: " + id);
        }
        return (DeferredBlock<T>) block;
    }
}
