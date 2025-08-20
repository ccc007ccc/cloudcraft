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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 气态云方块，可被药水固化成固态云
 */
public class CloudBlockGas extends GasCloudBlock {

    private final BlockState solidState;

    public CloudBlockGas(Properties properties, BlockState solidState) {
        super(properties);
        this.solidState = solidState;
    }

    @Override
    public BlockState getSolidState() {
        return solidState;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {
        super.entityInside(state, level, pos, entity, effectApplier);

        // 额外减速（可选，自定义调节）
        Vec3 slowDown = new Vec3(0.95, 0.95, 0.95);

        entity.setDeltaMovement(entity.getDeltaMovement().multiply(slowDown));
    }

}
