package com.tr1c.cloudcraft.cloudtech;

public final class JetpackFlightRules {
    private static final double THRUST_IDLE_HORIZONTAL_DRAG = 0.98;
    private static final double INPUT_EPSILON = 1.0E-4;

    private JetpackFlightRules() {
    }

    public static Movement applyThrustMovement(double movementX, double movementY, double movementZ,
                                               double moveIntentX, double moveIntentZ,
                                               double verticalSpeed, double horizontalAcceleration, double horizontalMaxSpeed) {
        Movement horizontal = new Movement(movementX, 0.0D, movementZ);
        Movement inputDirection = movementInputDirection(moveIntentX, moveIntentZ);
        if (inputDirection.horizontalDistanceSqr() > INPUT_EPSILON) {
            horizontal = horizontal.add(inputDirection.scale(horizontalAcceleration));
            double horizontalSpeed = horizontal.horizontalDistance();
            if (horizontalSpeed > horizontalMaxSpeed) {
                horizontal = horizontal.scale(horizontalMaxSpeed / horizontalSpeed);
            }
        } else {
            horizontal = horizontal.scale(THRUST_IDLE_HORIZONTAL_DRAG);
        }
        return new Movement(horizontal.x(), Math.max(movementY, verticalSpeed), horizontal.z());
    }

    private static Movement movementInputDirection(double inputX, double inputZ) {
        double inputLengthSqr = inputX * inputX + inputZ * inputZ;
        if (inputLengthSqr <= INPUT_EPSILON) {
            return Movement.ZERO;
        }
        double inputLength = Math.sqrt(inputLengthSqr);
        return new Movement(inputX / inputLength, 0.0D, inputZ / inputLength);
    }

    public record Movement(double x, double y, double z) {
        private static final Movement ZERO = new Movement(0.0D, 0.0D, 0.0D);

        Movement add(Movement other) {
            return new Movement(x + other.x, y + other.y, z + other.z);
        }

        Movement scale(double scale) {
            return new Movement(x * scale, y * scale, z * scale);
        }

        public double horizontalDistance() {
            return Math.sqrt(horizontalDistanceSqr());
        }

        double horizontalDistanceSqr() {
            return x * x + z * z;
        }
    }
}
