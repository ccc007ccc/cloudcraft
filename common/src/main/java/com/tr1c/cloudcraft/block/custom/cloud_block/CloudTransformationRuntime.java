package com.tr1c.cloudcraft.block.custom.cloud_block;

import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class CloudTransformationRuntime {
    private CloudTransformationRuntime() {
    }

    public static int solidifyAroundEntity(ServerLevel level, BlockPos center) {
        return solidify(level, center, CloudTransformationRules.effectOffsets());
    }

    public static int solidifyInRadius(Level level, BlockPos center, double radius) {
        return solidify(level, center, CloudTransformationRules.radiusOffsets(radius));
    }

    public static int gasifyInRadius(Level level, BlockPos center, double radius) {
        return gasify(level, center, CloudTransformationRules.radiusOffsets(radius));
    }

    public static boolean hasCloudWalker(ItemStack stack, Holder<MobEffect> cloudWalkerEffect) {
        PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        return hasCloudWalker(contents, cloudWalkerEffect);
    }

    public static boolean hasCloudWalker(PotionContents contents, Holder<MobEffect> cloudWalkerEffect) {
        for (MobEffectInstance effect : contents.getAllEffects()) {
            if (effect.getEffect().equals(cloudWalkerEffect)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasGasCloud(Level level, BlockPos center, Iterable<CloudTransformationRules.Offset> offsets) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (CloudTransformationRules.Offset offset : offsets) {
            mutablePos.set(center.getX() + offset.x(), center.getY() + offset.y(), center.getZ() + offset.z());
            if (level.getBlockState(mutablePos).getBlock() instanceof GasCloudBlock) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasSolidCloud(Level level, BlockPos center, Iterable<CloudTransformationRules.Offset> offsets) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (CloudTransformationRules.Offset offset : offsets) {
            mutablePos.set(center.getX() + offset.x(), center.getY() + offset.y(), center.getZ() + offset.z());
            String blockId = blockId(level.getBlockState(mutablePos));
            if (CloudTransformationRules.isSolidCloudId(blockId)) {
                return true;
            }
        }
        return false;
    }

    private static int solidify(Level level, BlockPos center, Iterable<CloudTransformationRules.Offset> offsets) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int converted = 0;
        for (CloudTransformationRules.Offset offset : offsets) {
            mutablePos.set(center.getX() + offset.x(), center.getY() + offset.y(), center.getZ() + offset.z());
            BlockState targetState = level.getBlockState(mutablePos);
            if (targetState.getBlock() instanceof GasCloudBlock gasBlock) {
                gasBlock.solidify(level, mutablePos.immutable());
                converted++;
            }
        }
        return converted;
    }

    private static int gasify(Level level, BlockPos center, Iterable<CloudTransformationRules.Offset> offsets) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int converted = 0;
        for (CloudTransformationRules.Offset offset : offsets) {
            mutablePos.set(center.getX() + offset.x(), center.getY() + offset.y(), center.getZ() + offset.z());
            BlockState targetState = level.getBlockState(mutablePos);
            String gasCloudId = CloudTransformationRules.gasCloudIdForSolidCloudId(blockId(targetState));
            if (gasCloudId == null) {
                continue;
            }
            Block gasBlock = BuiltInRegistries.BLOCK.getValue(ModIds.id(gasCloudId));
            level.setBlock(mutablePos, gasBlock.defaultBlockState(), 3);
            converted++;
        }
        return converted;
    }

    private static String blockId(BlockState state) {
        return state.getBlockHolder().unwrapKey().map(key -> key.identifier().getPath()).orElse("");
    }
}
