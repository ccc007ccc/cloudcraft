package com.tr1c.cloudcraft.registry;

import com.tr1c.cloudcraft.block.entity.GasStateConverterBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;

public final class CloudCraftBlockEntityDefinitions {
    private static final List<String> IDS = List.of(ModIds.GAS_STATE_CONVERTER);

    private CloudCraftBlockEntityDefinitions() {
    }

    public static List<String> ids() {
        return IDS;
    }

    @SuppressWarnings("unchecked")
    public static BlockEntityType<GasStateConverterBlockEntity> gasStateConverterType() {
        BlockEntityType<?> type = BuiltInRegistries.BLOCK_ENTITY_TYPE.getValue(ModIds.id(ModIds.GAS_STATE_CONVERTER));
        if (type == null) {
            throw new IllegalStateException("Gas state converter block entity type is not registered");
        }
        return (BlockEntityType<GasStateConverterBlockEntity>) type;
    }
}
