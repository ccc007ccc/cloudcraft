package com.tr1c.cloudcraft.block.custom;

import com.tr1c.cloudcraft.block.RotatableBlock;
import com.tr1c.cloudcraft.block.custom.cloud_block.CloudTransformationRules;
import com.tr1c.cloudcraft.block.custom.cloud_block.CloudTransformationRuntime;
import com.tr1c.cloudcraft.world.CloudDimensionTravel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class GasStateConverterBlock extends RotatableBlock {
    private static final int SOLIDIFY_PARTICLE_COLOR = 0xC9F4FF;

    public GasStateConverterBlock(Properties properties) {
        super(properties, false);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isCrouching()) {
            return travel(level, player, state);
        }
        return convertNearbyGasClouds(level, pos, player);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isCrouching()) {
            return travel(level, player, state);
        }
        return convertNearbyGasClouds(level, pos, player);
    }

    private static InteractionResult travel(Level level, Player player, BlockState state) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (player instanceof ServerPlayer serverPlayer && CloudDimensionTravel.travel(serverPlayer, state)) {
            return InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult convertNearbyGasClouds(Level level, BlockPos pos, Player player) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        int converted = CloudTransformationRuntime.solidifyInRadius(level, pos, CloudTransformationRules.CONVERTER_RADIUS);
        if (converted == 0) {
            player.displayClientMessage(Component.translatable("message.cloudcraft.gas_state_converter.no_targets"), true);
            return InteractionResult.PASS;
        }

        level.levelEvent(2002, pos, SOLIDIFY_PARTICLE_COLOR);
        return InteractionResult.SUCCESS_SERVER;
    }
}
