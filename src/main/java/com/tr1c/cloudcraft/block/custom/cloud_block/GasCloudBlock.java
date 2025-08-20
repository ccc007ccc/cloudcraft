package com.tr1c.cloudcraft.block.custom.cloud_block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.projectile.ThrownSplashPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
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
        // 药水固化逻辑
        if (!level.isClientSide && entity instanceof ThrownSplashPotion potion) {
            ItemStack stack = potion.getItem();
            potion.onHitAsPotion((ServerLevel) level, stack, new BlockHitResult(potion.position(), Direction.UP, pos, false));

            // 粒子颜色
            PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            int color = contents.getColor();
            level.levelEvent(2002, pos, color);

            // 固化范围半径
            double radius = 1.1;
            for (double dx = -radius; dx <= radius; dx++) {
                for (double dy = -radius; dy <= radius; dy++) {
                    for (double dz = -radius; dz <= radius; dz++) {
                        BlockPos targetPos = pos.offset((int) dx, (int) dy, (int) dz);
                        BlockState targetState = level.getBlockState(targetPos);
                        // 仅固化气态云方块（可以加 instanceof 判断）
                        if (targetState.getBlock() instanceof GasCloudBlock gasBlock) {
                            gasBlock.solidify(level, targetPos);
                        }
                    }
                }
            }

            potion.discard();
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty(); // 穿透
    }
}
