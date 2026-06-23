package com.tr1c.cloudcraft.block.custom.cloud_block;

import com.tr1c.cloudcraft.config.CloudCraftConfig;

public final class CloudMotionRules {
    public static final Motion CUMULUS_GAS = new Motion(0.34, 0.08, 0.98);
    public static final Motion STRATUS_GAS = new Motion(0.16, 0.22, 0.96);
    public static final Motion CIRRUS_GAS = new Motion(0.48, 0.05, 0.99);
    public static final Motion ALTOSTRATUS_GAS = new Motion(0.26, 0.14, 0.97);
    public static final Motion NIMBOSTRATUS_GAS = new Motion(0.12, 0.28, 0.94);
    public static final Motion CUMULONIMBUS_GAS = new Motion(0.22, 0.34, 0.9);

    private CloudMotionRules() {
    }

    public static Movement apply(Movement movement, Motion motion) {
        HorizontalMovement horizontal = convergeHorizontalMovement(movement.x(), movement.z(), motion);
        return new Movement(
                horizontal.x(),
                movement.y() * motion.verticalDrag(),
                horizontal.z());
    }

    private static HorizontalMovement convergeHorizontalMovement(double x, double z, Motion motion) {
        double speed = Math.hypot(x, z);
        double targetSpeed = CloudCraftConfig.scaleGasCloudHorizontalTargetSpeed(motion.horizontalTargetSpeed());
        if (speed <= targetSpeed || speed == 0.0) {
            return new HorizontalMovement(x, z);
        }

        double convergence = CloudCraftConfig.scaleGasCloudHorizontalConvergence(motion.horizontalConvergence());
        double adjustedSpeed = speed + (targetSpeed - speed) * convergence;
        double scale = adjustedSpeed / speed;
        return new HorizontalMovement(x * scale, z * scale);
    }

    public record Movement(double x, double y, double z) {
    }

    public record Motion(double horizontalTargetSpeed, double horizontalConvergence, double verticalDrag) {
        public Motion {
            if (horizontalTargetSpeed < 0.0 || horizontalConvergence < 0.0 || horizontalConvergence > 1.0 || verticalDrag < 0.0) {
                throw new IllegalArgumentException("Cloud motion values must be within their valid ranges");
            }
        }
    }

    private record HorizontalMovement(double x, double z) {
    }
}
