package com.tr1c.cloudcraft.config;

public final class CloudCraftConfig {
    private static volatile Values values = Values.DEFAULT;

    private CloudCraftConfig() {
    }

    public static Values values() {
        return values;
    }

    public static void apply(Values values) {
        CloudCraftConfig.values = values;
    }

    public static void reset() {
        values = Values.DEFAULT;
    }

    public static int scalePressureCapacity(int baseValue) {
        return scalePositiveInt(baseValue, values.pressureCapacityMultiplier());
    }

    public static int scalePressureCost(int baseValue) {
        return scaleNonNegativeInt(baseValue, values.pressureCostMultiplier());
    }

    public static int scalePressureRecharge(int baseValue) {
        return scaleNonNegativeInt(baseValue, values.pressureRechargeMultiplier());
    }

    public static double scaleJetpackVerticalSpeed(double baseValue) {
        return baseValue * values.jetpackVerticalSpeedMultiplier();
    }

    public static double scaleJetpackHorizontalAcceleration(double baseValue) {
        return baseValue * values.jetpackHorizontalAccelerationMultiplier();
    }

    public static double scaleJetpackHorizontalMaxSpeed(double baseValue) {
        return baseValue * values.jetpackHorizontalMaxSpeedMultiplier();
    }

    public static double scaleGasCloudHorizontalTargetSpeed(double baseValue) {
        return baseValue * values.gasCloudHorizontalTargetSpeedMultiplier();
    }

    public static double scaleGasCloudHorizontalConvergence(double baseValue) {
        return Math.min(1.0D, baseValue * values.gasCloudHorizontalConvergenceMultiplier());
    }

    private static int scalePositiveInt(int baseValue, double multiplier) {
        if (baseValue <= 0) {
            return baseValue;
        }
        return Math.max(1, scaleNonNegativeInt(baseValue, multiplier));
    }

    private static int scaleNonNegativeInt(int baseValue, double multiplier) {
        if (baseValue <= 0 || multiplier == 0.0D) {
            return 0;
        }
        double scaled = baseValue * multiplier;
        if (scaled >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return Math.max(0, (int) Math.round(scaled));
    }

    public record Values(
            double pressureCapacityMultiplier,
            double pressureCostMultiplier,
            double pressureRechargeMultiplier,
            double jetpackVerticalSpeedMultiplier,
            double jetpackHorizontalAccelerationMultiplier,
            double jetpackHorizontalMaxSpeedMultiplier,
            double gasCloudHorizontalTargetSpeedMultiplier,
            double gasCloudHorizontalConvergenceMultiplier) {
        public static final Values DEFAULT = new Values(
                1.0D,
                1.0D,
                1.0D,
                1.0D,
                1.0D,
                1.0D,
                1.0D,
                1.0D);

        public Values {
            requirePositive("pressureCapacityMultiplier", pressureCapacityMultiplier);
            requireNonNegative("pressureCostMultiplier", pressureCostMultiplier);
            requireNonNegative("pressureRechargeMultiplier", pressureRechargeMultiplier);
            requireNonNegative("jetpackVerticalSpeedMultiplier", jetpackVerticalSpeedMultiplier);
            requireNonNegative("jetpackHorizontalAccelerationMultiplier", jetpackHorizontalAccelerationMultiplier);
            requireNonNegative("jetpackHorizontalMaxSpeedMultiplier", jetpackHorizontalMaxSpeedMultiplier);
            requireNonNegative("gasCloudHorizontalTargetSpeedMultiplier", gasCloudHorizontalTargetSpeedMultiplier);
            requireNonNegative("gasCloudHorizontalConvergenceMultiplier", gasCloudHorizontalConvergenceMultiplier);
        }

        private static void requirePositive(String name, double value) {
            if (!Double.isFinite(value) || value <= 0.0D) {
                throw new IllegalArgumentException(name + " must be positive");
            }
        }

        private static void requireNonNegative(String name, double value) {
            if (!Double.isFinite(value) || value < 0.0D) {
                throw new IllegalArgumentException(name + " must be non-negative");
            }
        }
    }
}
