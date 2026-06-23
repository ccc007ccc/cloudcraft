package com.tr1c.cloudcraft.progression;

import com.tr1c.cloudcraft.registry.ModIds;

import java.util.Map;
import java.util.Set;

public final class CloudProgressionRules {
    private static final Set<String> JETPACK_IDS = Set.of(
            ModIds.CLOUD_JETPACK,
            ModIds.STABILIZED_CLOUD_JETPACK,
            ModIds.HIGH_PRESSURE_CLOUD_JETPACK);
    private static final Map<String, CloudTier> MATERIAL_TIERS = Map.of(
            ModIds.CUMULUS_CLOUD_FRAGMENT, CloudTier.CUMULUS,
            ModIds.COMPRESSED_CANISTER, CloudTier.CUMULUS,
            ModIds.CIRRUS_FILAMENT, CloudTier.CIRRUS,
            ModIds.STABILIZED_NOZZLE, CloudTier.ALTOSTRATUS,
            ModIds.STORM_CORE, CloudTier.CUMULONIMBUS,
            ModIds.HIGH_PRESSURE_CHAMBER, CloudTier.CUMULONIMBUS);
    private static final Map<String, CloudTier> JETPACK_TIERS = Map.of(
            ModIds.CLOUD_JETPACK, CloudTier.CUMULUS,
            ModIds.STABILIZED_CLOUD_JETPACK, CloudTier.ALTOSTRATUS,
            ModIds.HIGH_PRESSURE_CLOUD_JETPACK, CloudTier.CUMULONIMBUS);
    private static final Map<String, Integer> MAX_PRESSURE = Map.of(
            ModIds.CLOUD_JETPACK, 1200,
            ModIds.STABILIZED_CLOUD_JETPACK, 1800,
            ModIds.HIGH_PRESSURE_CLOUD_JETPACK, 2400);
    private static final Map<String, Integer> THRUST_COST = Map.of(
            ModIds.CLOUD_JETPACK, 20,
            ModIds.STABILIZED_CLOUD_JETPACK, 16,
            ModIds.HIGH_PRESSURE_CLOUD_JETPACK, 12);
    private static final Map<String, Double> THRUST_VERTICAL_SPEED = Map.of(
            ModIds.CLOUD_JETPACK, 0.18D,
            ModIds.STABILIZED_CLOUD_JETPACK, 0.22D,
            ModIds.HIGH_PRESSURE_CLOUD_JETPACK, 0.26D);
    private static final Map<String, Double> THRUST_HORIZONTAL_ACCELERATION = Map.of(
            ModIds.CLOUD_JETPACK, 0.065D,
            ModIds.STABILIZED_CLOUD_JETPACK, 0.085D,
            ModIds.HIGH_PRESSURE_CLOUD_JETPACK, 0.105D);
    private static final Map<String, Double> THRUST_HORIZONTAL_MAX_SPEED = Map.of(
            ModIds.CLOUD_JETPACK, 0.30D,
            ModIds.STABILIZED_CLOUD_JETPACK, 0.37D,
            ModIds.HIGH_PRESSURE_CLOUD_JETPACK, 0.44D);
    private static final Map<String, Integer> HOVER_COST = Map.of(
            ModIds.CLOUD_JETPACK, 12,
            ModIds.STABILIZED_CLOUD_JETPACK, 9,
            ModIds.HIGH_PRESSURE_CLOUD_JETPACK, 6);
    private static final Map<String, Integer> RECHARGE_BONUS = Map.of(
            ModIds.CLOUD_JETPACK, 0,
            ModIds.STABILIZED_CLOUD_JETPACK, 2,
            ModIds.HIGH_PRESSURE_CLOUD_JETPACK, 4);
    private static final Set<String> UNLOCK_IDS = Set.of(
            ModIds.BASIC_JETPACK_FRAME,
            ModIds.STABILIZED_NOZZLE,
            ModIds.HIGH_PRESSURE_CHAMBER,
            ModIds.CLOUD_JETPACK,
            ModIds.STABILIZED_CLOUD_JETPACK,
            ModIds.HIGH_PRESSURE_CLOUD_JETPACK);

    private CloudProgressionRules() {
    }

    public static boolean isJetpackId(String itemId) {
        return JETPACK_IDS.contains(itemId);
    }

    public static CloudTier tierForMaterial(String itemId) {
        return MATERIAL_TIERS.getOrDefault(itemId, CloudTier.CUMULUS);
    }

    public static CloudTier tierForJetpack(String itemId) {
        return JETPACK_TIERS.getOrDefault(itemId, CloudTier.CUMULUS);
    }

    public static int maxPressure(String itemId) {
        return MAX_PRESSURE.getOrDefault(itemId, MAX_PRESSURE.get(ModIds.CLOUD_JETPACK));
    }

    public static int thrustCost(String itemId) {
        return THRUST_COST.getOrDefault(itemId, THRUST_COST.get(ModIds.CLOUD_JETPACK));
    }

    public static double thrustVerticalSpeed(String itemId) {
        return THRUST_VERTICAL_SPEED.getOrDefault(itemId, THRUST_VERTICAL_SPEED.get(ModIds.CLOUD_JETPACK));
    }

    public static double thrustHorizontalAcceleration(String itemId) {
        return THRUST_HORIZONTAL_ACCELERATION.getOrDefault(itemId, THRUST_HORIZONTAL_ACCELERATION.get(ModIds.CLOUD_JETPACK));
    }

    public static double thrustHorizontalMaxSpeed(String itemId) {
        return THRUST_HORIZONTAL_MAX_SPEED.getOrDefault(itemId, THRUST_HORIZONTAL_MAX_SPEED.get(ModIds.CLOUD_JETPACK));
    }

    public static int hoverCost(String itemId) {
        return HOVER_COST.getOrDefault(itemId, HOVER_COST.get(ModIds.CLOUD_JETPACK));
    }

    public static int rechargeBonus(String itemId) {
        return RECHARGE_BONUS.getOrDefault(itemId, 0);
    }

    public static Set<String> unlockIds() {
        return UNLOCK_IDS;
    }
}
