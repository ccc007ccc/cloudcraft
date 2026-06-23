package com.tr1c.cloudcraft.block.custom.cloud_block;

public final class CloudMotionRules {
    public static final Motion CUMULUS_GAS = new Motion(1.0, 0.98);
    public static final Motion STRATUS_GAS = new Motion(1.0, 0.96);
    public static final Motion CIRRUS_GAS = new Motion(1.0, 0.99);
    public static final Motion ALTOSTRATUS_GAS = new Motion(1.0, 0.97);
    public static final Motion NIMBOSTRATUS_GAS = new Motion(1.0, 0.94);
    public static final Motion CUMULONIMBUS_GAS = new Motion(1.0, 0.9);

    private CloudMotionRules() {
    }

    public static Movement apply(Movement movement, Motion motion) {
        return new Movement(
                movement.x() * motion.horizontalDrag(),
                movement.y() * motion.verticalDrag(),
                movement.z() * motion.horizontalDrag());
    }

    public record Movement(double x, double y, double z) {
    }

    public record Motion(double horizontalDrag, double verticalDrag) {
        public Motion {
            if (horizontalDrag < 0.0 || verticalDrag < 0.0) {
                throw new IllegalArgumentException("Cloud motion drag values must be non-negative");
            }
        }
    }
}
