package com.tr1c.cloudcraft.block.custom.cloud_block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class CloudBlock extends BaseCloudBlock {
    private final Holder<MobEffect> cloudWalkerEffect;
    private final Supplier<Item> fragmentItem;

    public CloudBlock(Properties properties, Holder<MobEffect> cloudWalkerEffect, Supplier<Item> fragmentItem) {
        super(properties);
        this.cloudWalkerEffect = cloudWalkerEffect;
        this.fragmentItem = fragmentItem;
    }

    @Override
    public void onWalk(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (level.isClientSide() || !(entity instanceof LivingEntity living)) {
            return;
        }

        if (!shouldBreakCloud(living, (float) entity.fallDistance)) {
            return;
        }

        dropFragmentIfNeeded(level, pos);
        level.destroyBlock(pos, false);
    }

    boolean shouldBreakCloud(LivingEntity living, float fallDistance) {
        return CloudWalkRules.shouldBreakCloud(living.hasEffect(cloudWalkerEffect), living.isCrouching(), fallDistance);
    }

    private void dropFragmentIfNeeded(Level level, BlockPos pos) {
        RandomSource random = level.random;
        if (random.nextFloat() < CloudTransformationRules.CLOUD_FRAGMENT_DROP_CHANCE) {
            popResource(level, pos, new ItemStack(fragmentItem.get()));
        }
    }
}
