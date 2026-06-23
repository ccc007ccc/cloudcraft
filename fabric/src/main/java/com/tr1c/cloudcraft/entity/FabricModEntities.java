package com.tr1c.cloudcraft.entity;

import com.tr1c.cloudcraft.registry.CloudCraftEntityDefinitions;
import com.tr1c.cloudcraft.registry.ModIds;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

public final class FabricModEntities {
    public static final EntityType<CloudWispEntity> CLOUD_WISP = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ModIds.entityTypeKey(ModIds.CLOUD_WISP),
            CloudCraftEntityDefinitions.createCloudWisp());

    private FabricModEntities() {
    }

    public static void register() {
        FabricDefaultAttributeRegistry.register(CLOUD_WISP, CloudWispEntity.createAttributes());
    }
}
