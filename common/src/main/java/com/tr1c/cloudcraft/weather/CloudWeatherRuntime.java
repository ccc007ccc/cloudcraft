package com.tr1c.cloudcraft.weather;

import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.registry.ModIds;
import com.tr1c.cloudcraft.world.CloudDimensionKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class CloudWeatherRuntime {
    private CloudWeatherRuntime() {
    }

    public static void tick(ServerLevel level) {
        if (!level.dimension().equals(CloudDimensionKeys.CLOUD_DIMENSION)
                || !CloudWeatherRules.shouldTick(level.getGameTime())) {
            return;
        }

        for (var player : level.players()) {
            runWeatherCycle(
                    level,
                    player.blockPosition(),
                    level.random.nextInt(CloudWeatherRules.THUNDERSTORM_ROLLS),
                    level.random.nextInt(CloudWeatherRules.RAIN_ROLLS));
        }
    }

    public static CloudWeatherRules.WeatherEvent runWeatherCycle(
            ServerLevel level,
            BlockPos center,
            int thunderstormRoll,
            int rainRoll) {
        CloudWeatherRules.CloudCounts counts = scanClouds(level, center);
        CloudWeatherRules.WeatherEvent event = CloudWeatherRules.selectEvent(counts, thunderstormRoll, rainRoll);
        return switch (event) {
            case THUNDERSTORM -> triggerThunderstorm(level, center);
            case RAIN -> condenseRain(level, center) ? CloudWeatherRules.WeatherEvent.RAIN : CloudWeatherRules.WeatherEvent.NONE;
            case NONE -> CloudWeatherRules.WeatherEvent.NONE;
        };
    }

    public static CloudWeatherRules.CloudCounts scanClouds(ServerLevel level, BlockPos center) {
        CloudWeatherRules.CloudCounts counts = new CloudWeatherRules.CloudCounts(0, 0);
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (CloudWeatherRules.Offset offset : CloudWeatherRules.scanOffsets()) {
            mutablePos.set(center.getX() + offset.x(), center.getY() + offset.y(), center.getZ() + offset.z());
            counts = counts.add(CloudWeatherRules.cloudKind(blockId(level.getBlockState(mutablePos))));
        }
        return counts;
    }

    private static CloudWeatherRules.WeatherEvent triggerThunderstorm(ServerLevel level, BlockPos center) {
        BlockPos cloudPos = nearestCloud(level, center, CloudWeatherRules.CloudKind.CUMULONIMBUS);
        if (cloudPos == null) {
            return CloudWeatherRules.WeatherEvent.NONE;
        }

        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);
        if (lightning != null) {
            lightning.setVisualOnly(true);
            lightning.setPos(cloudPos.getX() + 0.5D, cloudPos.getY() + 1.0D, cloudPos.getZ() + 0.5D);
            level.addFreshEntity(lightning);
        }
        level.playSound(null, cloudPos, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 2.0F, 1.15F);
        return CloudWeatherRules.WeatherEvent.THUNDERSTORM;
    }

    private static boolean condenseRain(ServerLevel level, BlockPos center) {
        BlockPos cloudPos = nearestCloud(level, center, CloudWeatherRules.CloudKind.NIMBOSTRATUS);
        if (cloudPos == null) {
            return false;
        }
        for (int depth = 1; depth <= CloudWeatherRules.WATER_SEARCH_DEPTH; depth++) {
            BlockPos targetPos = cloudPos.below(depth);
            BlockState targetState = level.getBlockState(targetPos);
            BlockPos supportPos = targetPos.below();
            if (targetState.isAir() && level.getBlockState(supportPos).isFaceSturdy(level, supportPos, Direction.UP)) {
                level.setBlock(targetPos, Blocks.WATER.defaultBlockState(), 3);
                level.sendParticles(
                        ParticleTypes.RAIN,
                        targetPos.getX() + 0.5D,
                        targetPos.getY() + 0.75D,
                        targetPos.getZ() + 0.5D,
                        16,
                        0.35D,
                        0.2D,
                        0.35D,
                        0.02D);
                level.playSound(null, targetPos, SoundEvents.WATER_AMBIENT, SoundSource.WEATHER, 0.6F, 1.4F);
                return true;
            }
        }
        return false;
    }

    private static BlockPos nearestCloud(ServerLevel level, BlockPos center, CloudWeatherRules.CloudKind kind) {
        BlockPos nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (CloudWeatherRules.Offset offset : CloudWeatherRules.scanOffsets()) {
            mutablePos.set(center.getX() + offset.x(), center.getY() + offset.y(), center.getZ() + offset.z());
            if (CloudWeatherRules.cloudKind(blockId(level.getBlockState(mutablePos))) != kind) {
                continue;
            }
            double distance = mutablePos.distSqr(center);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearest = mutablePos.immutable();
            }
        }
        return nearest;
    }

    private static String blockId(BlockState state) {
        return state.getBlockHolder().unwrapKey().map(key -> {
            if (!CloudCraft.MOD_ID.equals(key.identifier().getNamespace())) {
                return "";
            }
            return key.identifier().getPath();
        }).orElse("");
    }
}
