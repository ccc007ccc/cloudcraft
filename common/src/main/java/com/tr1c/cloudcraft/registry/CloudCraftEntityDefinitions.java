package com.tr1c.cloudcraft.registry;

import com.tr1c.cloudcraft.entity.CloudWispEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class CloudCraftEntityDefinitions {
    private static final Map<String, Supplier<EntityType<?>>> DEFINITIONS = definitions();

    private CloudCraftEntityDefinitions() {
    }

    public static List<String> ids() {
        return List.copyOf(DEFINITIONS.keySet());
    }

    public static EntityType<CloudWispEntity> createCloudWisp() {
        return EntityType.Builder
                .of(CloudWispEntity::new, MobCategory.CREATURE)
                .sized(1.5F, 1.5F)
                .eyeHeight(0.8F)
                .clientTrackingRange(8)
                .updateInterval(3)
                .noLootTable()
                .build(ModIds.entityTypeKey(ModIds.CLOUD_WISP));
    }

    private static Map<String, Supplier<EntityType<?>>> definitions() {
        Map<String, Supplier<EntityType<?>>> definitions = new LinkedHashMap<>();
        definitions.put(ModIds.CLOUD_WISP, CloudCraftEntityDefinitions::createCloudWisp);
        return Map.copyOf(definitions);
    }
}
