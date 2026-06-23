package com.tr1c.cloudcraft.block;

import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.block.entity.GasStateConverterBlockEntity;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class NeoForgeModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CloudCraft.MOD_ID);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GasStateConverterBlockEntity>> GAS_STATE_CONVERTER =
            BLOCK_ENTITY_TYPES.register(
                    ModIds.GAS_STATE_CONVERTER,
                    () -> new BlockEntityType<>(GasStateConverterBlockEntity::new, NeoForgeModBlocks.GAS_STATE_CONVERTER.get()));

    private NeoForgeModBlockEntities() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }
}
