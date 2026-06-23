package com.tr1c.cloudcraft.world;

import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public final class CloudDimensionKeys {
    public static final ResourceKey<Level> CLOUD_DIMENSION = ResourceKey.create(Registries.DIMENSION, ModIds.id(ModIds.CLOUD_DIMENSION));
    public static final ResourceKey<Biome> CUMULUS_FIELDS = ResourceKey.create(Registries.BIOME, ModIds.id(ModIds.CUMULUS_FIELDS));

    private CloudDimensionKeys() {
    }
}
