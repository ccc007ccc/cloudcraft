package com.tr1c.cloudcraft.block.custom.cloud_block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseCloudBlock extends Block {
    public BaseCloudBlock(Properties properties) {
        super(properties);
    }

    public void onWalk(Level level, BlockPos pos, BlockState state, Entity entity) {
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        this.onWalk(level, pos, state, entity);
        super.stepOn(level, pos, state, entity);
    }
}
