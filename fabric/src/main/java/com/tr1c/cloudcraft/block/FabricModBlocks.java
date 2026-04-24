package com.tr1c.cloudcraft.block;

import com.tr1c.cloudcraft.block.custom.cloud_block.CumulusCloudBlockGas;
import com.tr1c.cloudcraft.effect.FabricModEffects;
import com.tr1c.cloudcraft.item.FabricModItems;
import com.tr1c.cloudcraft.registry.CloudCraftBlockDefinitions;
import com.tr1c.cloudcraft.registry.CloudCraftRegistryDefinitions;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class FabricModBlocks {
    private static final Map<String, Block> BLOCK_BY_ID = registerBlocks();

    public static final Block CUMULUS_CLOUD_BLOCK = blockById(ModIds.CUMULUS_CLOUD_BLOCK);
    public static final CumulusCloudBlockGas CUMULUS_CLOUD_BLOCK_GAS = (CumulusCloudBlockGas) blockById(ModIds.CUMULUS_CLOUD_BLOCK_GAS);
    public static final Block GAS_STATE_CONVERTER = blockById(ModIds.GAS_STATE_CONVERTER);

    private FabricModBlocks() {
    }

    private static Map<String, Block> registerBlocks() {
        Map<String, Block> blocks = new LinkedHashMap<>();
        Set<String> blockItemIds = Set.copyOf(CloudCraftRegistryDefinitions.blockItemIds());

        for (String id : CloudCraftRegistryDefinitions.blockIds()) {
            Block block = createBlock(id, blocks);
            if (blockItemIds.contains(id)) {
                BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(ModIds.itemKey(id)).useBlockDescriptionPrefix());
                FabricModItems.register(blockItem, ModIds.itemKey(id));
            }
            blocks.put(id, Registry.register(BuiltInRegistries.BLOCK, ModIds.blockKey(id), block));
        }

        return Map.copyOf(blocks);
    }

    private static Block createBlock(String id, Map<String, Block> blocks) {
        return CloudCraftBlockDefinitions.create(
                id,
                BlockBehaviour.Properties.of().setId(ModIds.blockKey(id)),
                FabricModEffects.CLOUD_WALKER,
                () -> FabricModItems.CUMULUS_CLOUD_FRAGMENT,
                () -> blocks.get(ModIds.CUMULUS_CLOUD_BLOCK).defaultBlockState());
    }

    public static Block blockById(String id) {
        Block block = BLOCK_BY_ID.get(id);
        if (block == null) {
            throw new IllegalArgumentException("Unknown block: " + id);
        }
        return block;
    }
}
