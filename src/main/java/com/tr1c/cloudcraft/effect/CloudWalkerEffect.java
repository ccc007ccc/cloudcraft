package com.tr1c.cloudcraft.effect;

import com.tr1c.cloudcraft.block.ModBlocks;
import com.tr1c.cloudcraft.block.custom.cloud_block.CumulusCloudBlockGas;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CloudWalkerEffect extends MobEffect {

    public CloudWalkerEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        BlockPos center = entity.blockPosition(); // 玩家所在方块
        int[][] offsets = new int[][] {
                {0, -1, 0},  // 脚下
                {1, -1, 0},  // 右下
                {-1, -1, 0}, // 左下
                {0, -1, 1},  // 前下
                {0, -1, -1}  // 后下
        };

        for (int[] offset : offsets) {
            BlockPos targetPos = center.offset(offset[0], offset[1], offset[2]);
            BlockState state = level.getBlockState(targetPos);

            // 只转换气态云为固态云
            if (state.getBlock() instanceof CumulusCloudBlockGas) {
                level.setBlockAndUpdate(targetPos, ModBlocks.CUMULUS_CLOUD_BLOCK.get().defaultBlockState());
            }
        }

        return super.applyEffectTick(level, entity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true; // 每 tick 都调用 applyEffectTick
    }
}
