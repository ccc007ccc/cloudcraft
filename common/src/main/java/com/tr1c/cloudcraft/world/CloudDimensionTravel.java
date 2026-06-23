package com.tr1c.cloudcraft.world;

import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Set;

public final class CloudDimensionTravel {
    private static final int CLOUD_LANDING_Y = 104;
    private static final int CLOUD_PLATFORM_RADIUS = 2;
    private static final int CLEAR_HEIGHT = 3;

    private CloudDimensionTravel() {
    }

    public static boolean travel(ServerPlayer player, BlockState converterState) {
        boolean leavingCloudDimension = player.level().dimension().equals(CloudDimensionKeys.CLOUD_DIMENSION);
        ResourceKey<Level> targetKey = leavingCloudDimension ? Level.OVERWORLD : CloudDimensionKeys.CLOUD_DIMENSION;
        ServerLevel targetLevel = player.level().getServer().getLevel(targetKey);
        if (targetLevel == null) {
            player.displayClientMessage(Component.translatable("message.cloudcraft.cloud_dimension_unavailable"), true);
            return false;
        }

        BlockPos destinationAnchor = destinationAnchor(player, targetLevel, leavingCloudDimension);
        if (!leavingCloudDimension) {
            prepareCloudLanding(targetLevel, destinationAnchor, converterState);
        }

        player.setDeltaMovement(0.0, 0.0, 0.0);
        player.resetFallDistance();
        player.teleportTo(
                targetLevel,
                destinationAnchor.getX() + 0.5,
                destinationAnchor.getY() + 1.0,
                destinationAnchor.getZ() + 0.5,
                Set.<Relative>of(),
                player.getYRot(),
                player.getXRot(),
                true);
        return true;
    }

    public static void prepareCloudLanding(ServerLevel level, BlockPos anchorPos, BlockState converterState) {
        BlockState cloudState = cumulusCloudState();
        for (int dx = -CLOUD_PLATFORM_RADIUS; dx <= CLOUD_PLATFORM_RADIUS; dx++) {
            for (int dz = -CLOUD_PLATFORM_RADIUS; dz <= CLOUD_PLATFORM_RADIUS; dz++) {
                level.setBlock(anchorPos.offset(dx, -1, dz), cloudState, 3);
            }
        }
        for (int dy = 0; dy < CLEAR_HEIGHT; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    level.setBlock(anchorPos.offset(dx, dy, dz), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
        level.setBlock(anchorPos, converterState, 3);
    }

    private static BlockPos destinationAnchor(ServerPlayer player, ServerLevel targetLevel, boolean leavingCloudDimension) {
        int x = Mth.floor(player.getX());
        int z = Mth.floor(player.getZ());
        if (!leavingCloudDimension) {
            return new BlockPos(x, CLOUD_LANDING_Y, z);
        }

        int y = targetLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        return new BlockPos(x, Math.max(targetLevel.getMinY() + 1, y), z);
    }

    private static BlockState cumulusCloudState() {
        Block block = BuiltInRegistries.BLOCK.getValue(ModIds.id(ModIds.CUMULUS_CLOUD_BLOCK));
        if (block == null || block == Blocks.AIR) {
            return Blocks.WHITE_WOOL.defaultBlockState();
        }
        return block.defaultBlockState();
    }
}
