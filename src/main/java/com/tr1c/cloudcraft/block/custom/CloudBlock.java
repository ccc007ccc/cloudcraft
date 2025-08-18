package com.tr1c.cloudcraft.block.custom;

import com.tr1c.cloudcraft.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CloudBlock extends Block {
    public CloudBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity livingEntity && (!livingEntity.isCrouching() || entity.fallDistance > 1.5f) && !level.isClientSide) {
            // 创建一个随机数生成器
            RandomSource random = level.random;
            // 10%的概率掉落云碎片
            if (random.nextFloat() < 0.1f) {
                popResource(level, pos, new ItemStack(ModItems.CLOUD_FRAGMENT.get()));
            }
            // 破坏方块
            level.destroyBlock(pos, false);
        }
        super.stepOn(level, pos, state, entity);
    }
}

