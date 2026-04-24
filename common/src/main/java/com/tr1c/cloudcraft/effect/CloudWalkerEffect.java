package com.tr1c.cloudcraft.effect;

import com.tr1c.cloudcraft.block.custom.cloud_block.CloudTransformationRules;
import com.tr1c.cloudcraft.block.custom.cloud_block.CloudTransformationRuntime;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class CloudWalkerEffect extends MobEffect {
    public CloudWalkerEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        BlockPos center = entity.blockPosition();
        if (!CloudTransformationRuntime.hasGasCloud(level, center, CloudTransformationRules.crossOffsets())) {
            return super.applyEffectTick(level, entity, amplifier);
        }
        CloudTransformationRuntime.solidifyAroundEntity(level, center);
        return super.applyEffectTick(level, entity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
