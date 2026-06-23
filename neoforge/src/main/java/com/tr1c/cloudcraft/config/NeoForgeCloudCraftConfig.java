package com.tr1c.cloudcraft.config;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class NeoForgeCloudCraftConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.DoubleValue PRESSURE_CAPACITY_MULTIPLIER = multiplier("pressureCapacityMultiplier", 1.0D);
    private static final ModConfigSpec.DoubleValue PRESSURE_COST_MULTIPLIER = multiplierAllowZero("pressureCostMultiplier", 1.0D);
    private static final ModConfigSpec.DoubleValue PRESSURE_RECHARGE_MULTIPLIER = multiplierAllowZero("pressureRechargeMultiplier", 1.0D);
    private static final ModConfigSpec.DoubleValue JETPACK_VERTICAL_SPEED_MULTIPLIER = multiplierAllowZero("jetpackVerticalSpeedMultiplier", 1.0D);
    private static final ModConfigSpec.DoubleValue JETPACK_HORIZONTAL_ACCELERATION_MULTIPLIER = multiplierAllowZero("jetpackHorizontalAccelerationMultiplier", 1.0D);
    private static final ModConfigSpec.DoubleValue JETPACK_HORIZONTAL_MAX_SPEED_MULTIPLIER = multiplierAllowZero("jetpackHorizontalMaxSpeedMultiplier", 1.0D);
    private static final ModConfigSpec.DoubleValue GAS_CLOUD_HORIZONTAL_TARGET_SPEED_MULTIPLIER = multiplierAllowZero("gasCloudHorizontalTargetSpeedMultiplier", 1.0D);
    private static final ModConfigSpec.DoubleValue GAS_CLOUD_HORIZONTAL_CONVERGENCE_MULTIPLIER = multiplierAllowZero("gasCloudHorizontalConvergenceMultiplier", 1.0D);
    private static final ModConfigSpec SPEC = BUILDER.build();

    private NeoForgeCloudCraftConfig() {
    }

    public static void register(ModContainer modContainer, IEventBus modEventBus) {
        modContainer.registerConfig(ModConfig.Type.COMMON, SPEC);
        modEventBus.addListener(NeoForgeCloudCraftConfig::onConfigLoaded);
    }

    private static ModConfigSpec.DoubleValue multiplier(String name, double defaultValue) {
        return BUILDER.defineInRange(name, defaultValue, 0.01D, 64.0D);
    }

    private static ModConfigSpec.DoubleValue multiplierAllowZero(String name, double defaultValue) {
        return BUILDER.defineInRange(name, defaultValue, 0.0D, 64.0D);
    }

    private static void onConfigLoaded(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            CloudCraftConfig.apply(values());
        }
    }

    private static CloudCraftConfig.Values values() {
        return new CloudCraftConfig.Values(
                PRESSURE_CAPACITY_MULTIPLIER.get(),
                PRESSURE_COST_MULTIPLIER.get(),
                PRESSURE_RECHARGE_MULTIPLIER.get(),
                JETPACK_VERTICAL_SPEED_MULTIPLIER.get(),
                JETPACK_HORIZONTAL_ACCELERATION_MULTIPLIER.get(),
                JETPACK_HORIZONTAL_MAX_SPEED_MULTIPLIER.get(),
                GAS_CLOUD_HORIZONTAL_TARGET_SPEED_MULTIPLIER.get(),
                GAS_CLOUD_HORIZONTAL_CONVERGENCE_MULTIPLIER.get());
    }
}
