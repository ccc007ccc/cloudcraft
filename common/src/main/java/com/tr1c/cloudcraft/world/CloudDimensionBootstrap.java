package com.tr1c.cloudcraft.world;

import com.google.common.collect.ImmutableList;
import com.tr1c.cloudcraft.mixin.MinecraftServerAccessor;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WorldData;

import java.util.List;
import java.util.Optional;

public final class CloudDimensionBootstrap {
    private CloudDimensionBootstrap() {
    }

    public static boolean ensureCloudDimension(MinecraftServer server) {
        if (server.getLevel(CloudDimensionKeys.CLOUD_DIMENSION) != null) {
            return false;
        }

        MinecraftServerAccessor accessor = (MinecraftServerAccessor) server;
        ServerLevel overworld = server.overworld();
        WorldData worldData = server.getWorldData();
        ServerLevelData levelData = new DerivedLevelData(worldData, worldData.overworldData());
        long biomeSeed = BiomeManager.obfuscateSeed(worldData.worldGenOptions().seed());
        RandomSequences randomSequences = overworld.getRandomSequences();
        ServerLevel cloudLevel = new ServerLevel(
                server,
                accessor.cloudcraft$getExecutor(),
                accessor.cloudcraft$getStorageSource(),
                levelData,
                CloudDimensionKeys.CLOUD_DIMENSION,
                createLevelStem(server),
                worldData.isDebugWorld(),
                biomeSeed,
                ImmutableList.of(),
                false,
                randomSequences);

        cloudLevel.getWorldBorder().setAbsoluteMaxSize(server.getAbsoluteMaxWorldSize());
        server.getPlayerList().addWorldborderListener(cloudLevel);
        accessor.cloudcraft$getLevels().put(CloudDimensionKeys.CLOUD_DIMENSION, cloudLevel);
        return true;
    }

    private static LevelStem createLevelStem(MinecraftServer server) {
        Holder<DimensionType> dimensionType = server.registryAccess()
                .lookupOrThrow(Registries.DIMENSION_TYPE)
                .getOrThrow(CloudDimensionKeys.CLOUD_DIMENSION_TYPE);
        Holder<Biome> biome = server.registryAccess()
                .lookupOrThrow(Registries.BIOME)
                .getOrThrow(CloudDimensionKeys.CUMULUS_FIELDS);
        FlatLevelGeneratorSettings settings = createFlatSettings(biome);
        return new LevelStem(dimensionType, new FlatLevelSource(settings));
    }

    private static FlatLevelGeneratorSettings createFlatSettings(Holder<Biome> biome) {
        List<FlatLayerInfo> layers = List.of(
                new FlatLayerInfo(96, Blocks.AIR),
                new FlatLayerInfo(3, cumulusCloudBlock()));
        FlatLevelGeneratorSettings settings = new FlatLevelGeneratorSettings(
                Optional.<net.minecraft.core.HolderSet<StructureSet>>empty(),
                biome,
                List.<Holder<PlacedFeature>>of())
                .withBiomeAndLayers(layers, Optional.empty(), biome);
        settings.setDecoration();
        return settings;
    }

    private static Block cumulusCloudBlock() {
        Block block = BuiltInRegistries.BLOCK.getValue(ModIds.id(ModIds.CUMULUS_CLOUD_BLOCK));
        if (block == null || block == Blocks.AIR) {
            return Blocks.WHITE_WOOL;
        }
        return block;
    }
}
