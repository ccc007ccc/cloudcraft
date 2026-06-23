package com.tr1c.cloudcraft.cloudtech;

import com.tr1c.cloudcraft.progression.CloudProgressionRules;
import com.tr1c.cloudcraft.registry.ModIds;

public final class CompressedAirRules {
    public static final int MAX_PRESSURE = 1200;
    public static final int CHARGE_PER_CLOUD_FRAGMENT = 150;
    public static final int THRUST_COST = 20;
    public static final int HOVER_COST = 12;
    public static final int IDLE_RECHARGE = 4;
    public static final int CIRRUS_RECHARGE = 12;
    public static final int ALTOSTRATUS_RECHARGE = 8;
    public static final int NIMBOSTRATUS_RECHARGE = 6;
    public static final int CUMULONIMBUS_RECHARGE = 16;

    private CompressedAirRules() {
    }

    public static int clamp(int pressure) {
        return clamp(pressure, MAX_PRESSURE);
    }

    public static int clamp(int pressure, int maxPressure) {
        return Math.max(0, Math.min(maxPressure, pressure));
    }

    public static int fillFromFragment(int pressure) {
        return add(pressure, CHARGE_PER_CLOUD_FRAGMENT);
    }

    public static int add(int pressure, int amount) {
        return add(pressure, amount, MAX_PRESSURE);
    }

    public static int add(int pressure, int amount, int maxPressure) {
        if (amount < 0) {
            throw new IllegalArgumentException("Compressed air amount must be non-negative");
        }
        return clamp(pressure + amount, maxPressure);
    }

    public static int consumeThrust(int pressure) {
        return consumeThrust(pressure, ModIds.CLOUD_JETPACK);
    }

    public static int consumeThrust(int pressure, String jetpackId) {
        return Math.max(0, pressure - CloudProgressionRules.thrustCost(jetpackId));
    }

    public static int consumeHover(int pressure, String jetpackId) {
        return Math.max(0, pressure - CloudProgressionRules.hoverCost(jetpackId));
    }

    public static boolean canThrust(int pressure) {
        return canThrust(pressure, ModIds.CLOUD_JETPACK);
    }

    public static boolean canThrust(int pressure, String jetpackId) {
        return pressure >= CloudProgressionRules.thrustCost(jetpackId);
    }

    public static int rechargeRate(String cloudId) {
        CloudPressureProfile profile = CloudPressureProfile.find(cloudId);
        return profile == null ? 0 : profile.rechargeRate();
    }

    public static int rechargeRate(String cloudId, String jetpackId) {
        return rechargeRate(cloudId) + CloudProgressionRules.rechargeBonus(jetpackId);
    }
}
