package com.tr1c.cloudcraft.block.custom;

import com.tr1c.cloudcraft.block.RotatableBlock;
import com.tr1c.cloudcraft.block.entity.GasStateConverterBlockEntity;
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
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class GasStateConverterBlock extends RotatableBlock implements EntityBlock {
    private static final int SOLIDIFY_PARTICLE_COLOR = 0xC9F4FF;
    private static final int GASIFY_PARTICLE_COLOR = 0xDDF8FF;

    public GasStateConverterBlock(Properties properties) {
        super(properties, false);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GasStateConverterBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isCrouching()) {
            return travel(level, player, state);
        }
        return missingCatalyst(level, player);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isCrouching()) {
            return travel(level, player, state);
        }
        GasStateConverterOperation operation = GasStateConverterOperation.byCatalyst(itemId(stack));
        if (operation == null) {
            return InteractionResult.PASS;
        }
        return convertNearbyClouds(level, pos, player, stack, operation);
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

    private static InteractionResult missingCatalyst(Level level, Player player) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        player.displayClientMessage(Component.translatable("message.cloudcraft.gas_state_converter.requires_catalyst"), true);
        return InteractionResult.PASS;
    }

    private static InteractionResult convertNearbyClouds(Level level, BlockPos pos, Player player, ItemStack catalyst, GasStateConverterOperation operation) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        int converted = switch (operation) {
            case SOLIDIFY -> CloudTransformationRuntime.solidifyInRadius(level, pos, CloudTransformationRules.CONVERTER_RADIUS);
            case GASIFY -> CloudTransformationRuntime.gasifyInRadius(level, pos, CloudTransformationRules.CONVERTER_RADIUS);
        };
        if (converted == 0) {
            player.displayClientMessage(noTargetsMessage(operation), true);
            return InteractionResult.PASS;
        }

        if (!player.isCreative()) {
            catalyst.shrink(1);
        }
        if (level.getBlockEntity(pos) instanceof GasStateConverterBlockEntity converter) {
            converter.recordConversion(operation, converted);
        }
        level.levelEvent(2002, pos, operation == GasStateConverterOperation.SOLIDIFY ? SOLIDIFY_PARTICLE_COLOR : GASIFY_PARTICLE_COLOR);
        return InteractionResult.SUCCESS_SERVER;
    }

    private static Component noTargetsMessage(GasStateConverterOperation operation) {
        return Component.translatable(operation == GasStateConverterOperation.SOLIDIFY
                ? "message.cloudcraft.gas_state_converter.no_gas_targets"
                : "message.cloudcraft.gas_state_converter.no_solid_targets");
    }

    private static String itemId(ItemStack stack) {
        return stack.getItem().builtInRegistryHolder().key().identifier().getPath();
    }
}
