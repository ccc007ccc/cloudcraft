package com.tr1c.cloudcraft.block.custom.cloud_block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MotionGasCloudBlock extends GasCloudBlock {
    private final BlockState solidState;
    private final CloudMotionRules.Motion motion;

    public MotionGasCloudBlock(
            Properties properties,
            BlockState solidState,
            Holder<MobEffect> cloudWalkerEffect,
            CloudMotionRules.Motion motion) {
        super(properties, cloudWalkerEffect);
        this.solidState = solidState;
        this.motion = motion;
    }

    @Override
    public BlockState getSolidState() {
        return solidState;
    }

    @Override
    protected void entityInside(
            BlockState state,
            Level level,
            BlockPos pos,
            Entity entity,
            InsideBlockEffectApplier effectApplier,
            boolean intersects) {
        super.entityInside(state, level, pos, entity, effectApplier, intersects);
        var movement = entity.getDeltaMovement();
        CloudMotionRules.Movement adjusted = CloudMotionRules.apply(
                new CloudMotionRules.Movement(movement.x, movement.y, movement.z),
                motion);
        entity.setDeltaMovement(adjusted.x(), adjusted.y(), adjusted.z());
    }
}
