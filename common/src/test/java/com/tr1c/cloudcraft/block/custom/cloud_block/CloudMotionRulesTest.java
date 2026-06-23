package com.tr1c.cloudcraft.block.custom.cloud_block;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloudMotionRulesTest {
    @Test
    void shouldConvergeHorizontalMovementTowardTargetWithoutChangingDirection() {
        CloudMotionRules.Movement movement = new CloudMotionRules.Movement(1.0, -0.2, -1.0);

        CloudMotionRules.Movement adjusted = CloudMotionRules.apply(movement, CloudMotionRules.STRATUS_GAS);

        assertEquals(-adjusted.z(), adjusted.x(), 0.0001);
        assertEquals(1.1383, Math.hypot(adjusted.x(), adjusted.z()), 0.0001);
        assertEquals(-0.192, adjusted.y(), 0.0001);
    }

    @Test
    void shouldNotBoostTinyHorizontalMovement() {
        CloudMotionRules.Movement movement = new CloudMotionRules.Movement(0.001, 0.0, 0.0);

        CloudMotionRules.Movement adjusted = CloudMotionRules.apply(movement, CloudMotionRules.CIRRUS_GAS);

        assertEquals(0.001, adjusted.x(), 0.0001);
        assertEquals(0.0, adjusted.z(), 0.0001);
    }

    @Test
    void shouldGiveCloudGasTypesDistinctHorizontalMovement() {
        CloudMotionRules.Movement movement = new CloudMotionRules.Movement(1.0, 0.0, 0.0);

        assertEquals(0.974, CloudMotionRules.apply(movement, CloudMotionRules.CIRRUS_GAS).x(), 0.0001);
        assertEquals(0.9472, CloudMotionRules.apply(movement, CloudMotionRules.CUMULUS_GAS).x(), 0.0001);
        assertEquals(0.8964, CloudMotionRules.apply(movement, CloudMotionRules.ALTOSTRATUS_GAS).x(), 0.0001);
        assertEquals(0.8152, CloudMotionRules.apply(movement, CloudMotionRules.STRATUS_GAS).x(), 0.0001);
        assertEquals(0.7536, CloudMotionRules.apply(movement, CloudMotionRules.NIMBOSTRATUS_GAS).x(), 0.0001);
        assertEquals(0.7348, CloudMotionRules.apply(movement, CloudMotionRules.CUMULONIMBUS_GAS).x(), 0.0001);
    }

    @Test
    void shouldNotImmediatelyClampHorizontalMovementToTarget() {
        CloudMotionRules.Movement movement = new CloudMotionRules.Movement(1.0, 0.0, 0.0);

        double adjusted = CloudMotionRules.apply(movement, CloudMotionRules.STRATUS_GAS).x();

        assertTrue(adjusted > CloudMotionRules.STRATUS_GAS.horizontalTargetSpeed());
        assertTrue(adjusted < movement.x());
    }

    @Test
    void shouldMakeDenseCloudsAffectVerticalMovementMore() {
        CloudMotionRules.Movement movement = new CloudMotionRules.Movement(0.0, -1.0, 0.0);

        double altostratus = CloudMotionRules.apply(movement, CloudMotionRules.ALTOSTRATUS_GAS).y();
        double nimbostratus = CloudMotionRules.apply(movement, CloudMotionRules.NIMBOSTRATUS_GAS).y();
        double cumulonimbus = CloudMotionRules.apply(movement, CloudMotionRules.CUMULONIMBUS_GAS).y();

        assertEquals(-0.97, altostratus, 0.0001);
        assertEquals(-0.94, nimbostratus, 0.0001);
        assertEquals(-0.9, cumulonimbus, 0.0001);
    }

    @Test
    void shouldRejectInvalidMotionConfiguration() {
        assertThrows(IllegalArgumentException.class, () -> new CloudMotionRules.Motion(-0.1, 0.1, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new CloudMotionRules.Motion(0.1, -0.1, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new CloudMotionRules.Motion(0.1, 1.1, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new CloudMotionRules.Motion(0.1, 0.1, -0.1));
    }
}
