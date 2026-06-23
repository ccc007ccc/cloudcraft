package com.tr1c.cloudcraft.cloudtech;

import com.tr1c.cloudcraft.progression.CloudProgressionRules;
import com.tr1c.cloudcraft.registry.ModIds;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JetpackRuntimeTest {
    @Test
    void shouldAddHorizontalThrustFromForwardInput() {
        JetpackFlightRules.Movement movement = JetpackFlightRules.applyThrustMovement(0.0, 0.0, 0.0, 0.0, 1.0,
                CloudProgressionRules.thrustVerticalSpeed(ModIds.CLOUD_JETPACK),
                CloudProgressionRules.thrustHorizontalAcceleration(ModIds.CLOUD_JETPACK),
                CloudProgressionRules.thrustHorizontalMaxSpeed(ModIds.CLOUD_JETPACK));

        assertEquals(0.18, movement.y(), 0.0001);
        assertTrue(movement.z() > 0.06);
        assertEquals(0.0, movement.x(), 0.0001);
    }

    @Test
    void shouldNotCrushHorizontalSpeedWhenThrustingWithoutInput() {
        JetpackFlightRules.Movement movement = JetpackFlightRules.applyThrustMovement(0.20, -0.10, 0.0, 0.0, 0.0,
                CloudProgressionRules.thrustVerticalSpeed(ModIds.CLOUD_JETPACK),
                CloudProgressionRules.thrustHorizontalAcceleration(ModIds.CLOUD_JETPACK),
                CloudProgressionRules.thrustHorizontalMaxSpeed(ModIds.CLOUD_JETPACK));

        assertTrue(movement.x() > 0.19);
        assertEquals(0.18, movement.y(), 0.0001);
    }

    @Test
    void shouldClampHorizontalThrustToControlledFlightSpeed() {
        JetpackFlightRules.Movement accelerated = JetpackFlightRules.applyThrustMovement(0.0, 0.0, 0.40, 0.0, 1.0,
                CloudProgressionRules.thrustVerticalSpeed(ModIds.CLOUD_JETPACK),
                CloudProgressionRules.thrustHorizontalAcceleration(ModIds.CLOUD_JETPACK),
                CloudProgressionRules.thrustHorizontalMaxSpeed(ModIds.CLOUD_JETPACK));

        assertTrue(accelerated.horizontalDistance() <= 0.3001);
        assertTrue(accelerated.horizontalDistance() > 0.20);
    }

    @Test
    void shouldScaleFlightStatsByJetpackTier() {
        assertTrue(CloudProgressionRules.thrustVerticalSpeed(ModIds.CLOUD_JETPACK)
                < CloudProgressionRules.thrustVerticalSpeed(ModIds.STABILIZED_CLOUD_JETPACK));
        assertTrue(CloudProgressionRules.thrustVerticalSpeed(ModIds.STABILIZED_CLOUD_JETPACK)
                < CloudProgressionRules.thrustVerticalSpeed(ModIds.HIGH_PRESSURE_CLOUD_JETPACK));
        assertTrue(CloudProgressionRules.thrustHorizontalMaxSpeed(ModIds.CLOUD_JETPACK)
                < CloudProgressionRules.thrustHorizontalMaxSpeed(ModIds.STABILIZED_CLOUD_JETPACK));
        assertTrue(CloudProgressionRules.thrustHorizontalMaxSpeed(ModIds.STABILIZED_CLOUD_JETPACK)
                < CloudProgressionRules.thrustHorizontalMaxSpeed(ModIds.HIGH_PRESSURE_CLOUD_JETPACK));
    }
}
