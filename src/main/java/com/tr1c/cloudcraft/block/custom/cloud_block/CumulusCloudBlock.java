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
public class CumulusCloudBlock extends BaseCloudBlock {

    public CumulusCloudBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onWalk(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity living) {
            boolean hasCloudWalker = living.hasEffect(ModEffects.CLOUD_WALKER);

            // 如果有 Cloud Walker 效果且跌落距离小于等于 2，则不破坏方块
            if (hasCloudWalker && entity.fallDistance <= 2f) {
                return; // 直接跳过踩踏逻辑
            }

            // 跌落距离大于 1.5 或没有 Cloud Walker 效果，继续原来的逻辑
            if (!living.isCrouching() || entity.fallDistance > 1.5f) {
                RandomSource random = level.random;
                if (random.nextFloat() < 0.1f) {
                    popResource(level, pos, new ItemStack(ModItems.CUMULUS_CLOUD_FRAGMENT.get()));
                }
                level.destroyBlock(pos, false);
            }
        }
    }

}
