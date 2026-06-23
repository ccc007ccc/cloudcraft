package com.tr1c.cloudcraft.block.custom.cloud_block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownLingeringPotion;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class GasCloudBlock extends HalfTransparentBlock {
    private static final VoxelShape TOP_SUPPORT_SHAPE = box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);

    private final Holder<MobEffect> cloudWalkerEffect;

    public GasCloudBlock(Properties properties, Holder<MobEffect> cloudWalkerEffect) {
        super(properties);
        this.cloudWalkerEffect = cloudWalkerEffect;
    }

    public abstract BlockState getSolidState();

    public void solidify(Level level, BlockPos pos) {
        if (!level.isClientSide()) {
            level.setBlock(pos, getSolidState(), 3);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean intersects) {
        super.entityInside(state, level, pos, entity, effectApplier, intersects);

        if (level.isClientSide()) {
            return;
        }

        CloudWalkerPotionHit potionHit = cloudWalkerPotionHit(entity);
        if (potionHit == null) {
            return;
        }

        PotionContents contents = potionHit.stack().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        if (!CloudTransformationRuntime.hasCloudWalker(contents, cloudWalkerEffect)) {
            return;
        }

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.levelEvent(2002, pos, contents.getColor());
        }

        CloudTransformationRuntime.solidifyInRadius(level, pos, potionHit.radius());
        potionHit.entity().discard();
    }

    private static CloudWalkerPotionHit cloudWalkerPotionHit(Entity entity) {
        if (entity instanceof ThrownSplashPotion splashPotion) {
            return new CloudWalkerPotionHit(splashPotion, splashPotion.getItem(), CloudTransformationRules.SPLASH_POTION_RADIUS);
        }
        if (entity instanceof ThrownLingeringPotion lingeringPotion) {
            return new CloudWalkerPotionHit(lingeringPotion, lingeringPotion.getItem(), CloudTransformationRules.LINGERING_POTION_RADIUS);
        }
        return null;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (!canSupportEntity(level, pos, context)) {
            return Shapes.empty();
        }
        return TOP_SUPPORT_SHAPE;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    private boolean canSupportEntity(BlockGetter level, BlockPos pos, CollisionContext context) {
        if (!(context instanceof EntityCollisionContext entityContext)) {
            return false;
        }
        Entity entity = entityContext.getEntity();
        if (!(entity instanceof LivingEntity living)) {
            return false;
        }
        boolean topLayer = !(level.getBlockState(pos.above()).getBlock() instanceof GasCloudBlock);
        return context.isAbove(TOP_SUPPORT_SHAPE, pos, true)
                && GasCloudSupportRules.shouldSupport(living.hasEffect(cloudWalkerEffect), living.isCrouching(), topLayer);
    }

    private record CloudWalkerPotionHit(Entity entity, ItemStack stack, double radius) {
    }
}
