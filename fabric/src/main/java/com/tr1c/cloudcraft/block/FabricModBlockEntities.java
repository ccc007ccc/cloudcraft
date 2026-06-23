package com.tr1c.cloudcraft.block;

import com.tr1c.cloudcraft.block.entity.GasStateConverterBlockEntity;
import com.tr1c.cloudcraft.registry.ModIds;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class FabricModBlockEntities {
    public static final BlockEntityType<GasStateConverterBlockEntity> GAS_STATE_CONVERTER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ModIds.id(ModIds.GAS_STATE_CONVERTER),
            FabricBlockEntityTypeBuilder.create(GasStateConverterBlockEntity::new, FabricModBlocks.GAS_STATE_CONVERTER).build());

    private FabricModBlockEntities() {
    }

    public static void register() {
        // Class loading performs the Fabric registry call above.
    }
}
