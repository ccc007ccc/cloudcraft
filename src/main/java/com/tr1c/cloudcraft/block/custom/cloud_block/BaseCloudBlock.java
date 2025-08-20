package com.tr1c.cloudcraft.block.custom.cloud_block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 这是所有云方块继承的父类
 */
public abstract class BaseCloudBlock extends Block {

    public BaseCloudBlock(Properties properties) {
        super(properties);
    }

    /**
     * 子类覆盖这个方法，实现当玩家踩在云上时的特殊效果
     */
    public void onWalk(Level level, BlockPos pos, BlockState state, Entity entity) {
        // 默认不做任何事
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        this.onWalk(level, pos, state, entity);
        super.stepOn(level, pos, state, entity);
    }
}
