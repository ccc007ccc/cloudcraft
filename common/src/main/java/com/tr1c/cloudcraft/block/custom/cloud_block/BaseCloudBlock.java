package com.tr1c.cloudcraft.block.custom.cloud_block;

import com.tr1c.cloudcraft.visual.CloudFeedbackRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
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
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!CloudFeedbackRules.shouldEmitAmbientParticle(random.nextInt(CloudFeedbackRules.SOLID_CLOUD_AMBIENT_PARTICLE_ROLLS))) {
            return;
        }
        level.addParticle(
                ParticleTypes.CLOUD,
                pos.getX() + random.nextDouble(),
                pos.getY() + 0.82D + random.nextDouble() * 0.18D,
                pos.getZ() + random.nextDouble(),
                (random.nextDouble() - 0.5D) * 0.01D,
                0.006D,
                (random.nextDouble() - 0.5D) * 0.01D);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        this.onWalk(level, pos, state, entity);
        super.stepOn(level, pos, state, entity);
    }
}
