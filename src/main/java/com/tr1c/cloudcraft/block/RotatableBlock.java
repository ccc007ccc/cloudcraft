package com.tr1c.cloudcraft.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

/**
 * 通用旋转方块，支持水平或全方向旋转
 */
public class RotatableBlock extends Block {

    // 水平旋转方向 (NORTH, SOUTH, EAST, WEST)
    public static final EnumProperty<Direction> FACING_HORIZONTAL = BlockStateProperties.HORIZONTAL_FACING;

    // 六方向旋转 (NORTH, SOUTH, EAST, WEST, UP, DOWN)
    public static final EnumProperty<Direction> FACING_6 = BlockStateProperties.FACING;

    private final boolean verticalAllowed;

    /**
     * @param properties 方块属性
     * @param allowVertical 是否允许上下方向旋转，如果 false 则只允许水平旋转
     */
    public RotatableBlock(Properties properties, boolean allowVertical) {
        super(properties);
        this.verticalAllowed = allowVertical;

        // 默认状态
        this.registerDefaultState(this.stateDefinition.any().setValue(
                allowVertical ? FACING_6 : FACING_HORIZONTAL,
                Direction.NORTH
        ));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(verticalAllowed ? FACING_6 : FACING_HORIZONTAL);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction dir = verticalAllowed ? context.getNearestLookingDirection().getOpposite()
                : context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(verticalAllowed ? FACING_6 : FACING_HORIZONTAL, dir);
    }
}
