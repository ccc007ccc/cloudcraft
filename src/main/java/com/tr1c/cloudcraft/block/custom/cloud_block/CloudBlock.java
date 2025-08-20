package com.tr1c.cloudcraft.block.custom.cloud_block;

import com.tr1c.cloudcraft.effect.ModEffects;
import com.tr1c.cloudcraft.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 固态云方块，踩踏逻辑可掉落碎片
 */
public class CloudBlock extends BaseCloudBlock {

    public CloudBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onWalk(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity living) {
            // 判断蹲下和跌落距离
            if (!living.isCrouching() || entity.fallDistance > 1.5f) {
                RandomSource random = level.random;
                if (random.nextFloat() < 0.1f) {
                    popResource(level, pos, new ItemStack(ModItems.CLOUD_FRAGMENT.get()));
                }
                level.destroyBlock(pos, false);
            }
        }
    }

}
