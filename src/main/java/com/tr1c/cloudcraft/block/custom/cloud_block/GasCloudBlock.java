package com.tr1c.cloudcraft.block.custom.cloud_block;

import com.tr1c.cloudcraft.effect.ModEffects;
import com.tr1c.cloudcraft.potion.ModPotion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.projectile.ThrownLingeringPotion;
import net.minecraft.world.entity.projectile.ThrownSplashPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 可穿透的气态云
 */
public abstract class GasCloudBlock extends HalfTransparentBlock {

    public GasCloudBlock(Properties properties) {
        super(properties);
    }

    /**
     * 返回固态云方块的状态
     * 子类必须实现
     */
    public abstract BlockState getSolidState();

    /** 将气态云转为固态云 */
    public void solidify(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            level.setBlock(pos, getSolidState(), 3);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {
        super.entityInside(state, level, pos, entity, effectApplier);

        if (!level.isClientSide) {
            // 处理喷溅型药水
            if (entity instanceof ThrownSplashPotion splashPotion) {
                handleCloudWalkerPotion(level, pos, splashPotion, 1.5);
            }

            // 处理滞留型药水
            else if (entity instanceof ThrownLingeringPotion lingeringPotion) {
                handleCloudWalkerPotion(level, pos, lingeringPotion, 3.0);
            }
        }
    }

    /**
     * 通用处理 cloud_walker 药水的方法
     * @param level 世界
     * @param pos 药水命中位置
     * @param potion 药水实体
     * @param radius 半径（球形）
     */
    private void handleCloudWalkerPotion(Level level, BlockPos pos, Entity potion, double radius) {
        if (!level.isClientSide) {
            ItemStack stack = null;

            if (potion instanceof ThrownSplashPotion splash) stack = splash.getItem();
            else if (potion instanceof ThrownLingeringPotion linger) stack = linger.getItem();
            else return;

            PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);

            // 遍历所有效果，检查是否有 cloud_walker
            boolean hasCloudWalker = false;
            for (MobEffectInstance effect : contents.getAllEffects()) {
                if (effect.getEffect() == ModEffects.CLOUD_WALKER) {
                    hasCloudWalker = true;
                    break;
                }
            }
            if (!hasCloudWalker) return;

            // 触发原版药水粒子和声音效果
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.levelEvent(2002, pos, contents.getColor());
            }

            int intRadius = (int) Math.ceil(radius);

            // 遍历球形范围内的方块
            for (int dx = -intRadius; dx <= intRadius; dx++) {
                for (int dy = -intRadius; dy <= intRadius; dy++) {
                    for (int dz = -intRadius; dz <= intRadius; dz++) {
                        BlockPos targetPos = pos.offset(dx, dy, dz);

                        // 球形判断
                        double distanceSq = pos.distSqr(targetPos);
                        if (distanceSq <= radius * radius) {
                            BlockState targetState = level.getBlockState(targetPos);
                            if (targetState.getBlock() instanceof GasCloudBlock gasBlock) {
                                gasBlock.solidify(level, targetPos);
                            }
                        }
                    }
                }
            }

            // 移除药水实体
            potion.discard();
        }
    }


    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty(); // 穿透
    }
}
