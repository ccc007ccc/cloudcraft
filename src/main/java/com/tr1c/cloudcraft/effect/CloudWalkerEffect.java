package com.tr1c.cloudcraft.effect;

import com.tr1c.cloudcraft.block.ModBlocks;
import com.tr1c.cloudcraft.block.custom.cloud_block.CloudBlockGas;
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
        BlockPos below = entity.blockPosition().below();
        BlockState state = level.getBlockState(below);

        // 假设 CloudBlockGas 是可穿透的云
        if (state.getBlock() instanceof CloudBlockGas) {
            level.setBlockAndUpdate(below, ModBlocks.CLOUD_BLOCK.get().defaultBlockState());
        }

        return super.applyEffectTick(level, entity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true; // 每 tick 调用 applyEffectTick
    }
}
