package com.tr1c.cloudcraft.block.custom.cloud_block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CumulusCloudBlockGas extends GasCloudBlock {
    private static final Vec3 SLOWDOWN = new Vec3(0.98, 0.98, 0.98);

    private final BlockState solidState;

    public CumulusCloudBlockGas(Properties properties, BlockState solidState, Holder<MobEffect> cloudWalkerEffect) {
        super(properties, cloudWalkerEffect);
        this.solidState = solidState;
    }

    @Override
    public BlockState getSolidState() {
        return solidState;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean intersects) {
        super.entityInside(state, level, pos, entity, effectApplier, intersects);
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(SLOWDOWN));
    }
}
