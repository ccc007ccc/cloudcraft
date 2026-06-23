package com.tr1c.cloudcraft.block.custom.cloud_block;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.state.BlockState;

public class CumulusCloudBlockGas extends MotionGasCloudBlock {
    public CumulusCloudBlockGas(Properties properties, BlockState solidState, Holder<MobEffect> cloudWalkerEffect) {
        super(properties, solidState, cloudWalkerEffect, CloudMotionRules.CUMULUS_GAS);
    }
}
