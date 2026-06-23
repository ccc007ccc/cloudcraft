package com.tr1c.cloudcraft.entity;

import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.registry.CloudCraftEntityDefinitions;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class NeoForgeModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(
            net.minecraft.core.registries.Registries.ENTITY_TYPE,
            CloudCraft.MOD_ID);
    public static final DeferredHolder<EntityType<?>, EntityType<CloudWispEntity>> CLOUD_WISP = ENTITY_TYPES.register(
            ModIds.CLOUD_WISP,
            CloudCraftEntityDefinitions::createCloudWisp);

    private NeoForgeModEntities() {
    }

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }
}
