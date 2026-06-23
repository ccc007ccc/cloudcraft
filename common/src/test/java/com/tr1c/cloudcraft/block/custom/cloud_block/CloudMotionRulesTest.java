package com.tr1c.cloudcraft.block.custom.cloud_block;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CloudMotionRulesTest {
    @Test
    void shouldPreserveHorizontalMovementWithoutChangingDirection() {
        CloudMotionRules.Movement movement = new CloudMotionRules.Movement(1.0, -0.2, -1.0);

        CloudMotionRules.Movement adjusted = CloudMotionRules.apply(movement, CloudMotionRules.STRATUS_GAS);

        assertEquals(1.0, adjusted.x(), 0.0001);
        assertEquals(-1.0, adjusted.z(), 0.0001);
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
    void shouldKeepAllCloudGasHorizontalMovement() {
        CloudMotionRules.Movement movement = new CloudMotionRules.Movement(1.0, 0.0, 0.0);

        assertEquals(1.0, CloudMotionRules.apply(movement, CloudMotionRules.CUMULUS_GAS).x(), 0.0001);
        assertEquals(1.0, CloudMotionRules.apply(movement, CloudMotionRules.STRATUS_GAS).x(), 0.0001);
        assertEquals(1.0, CloudMotionRules.apply(movement, CloudMotionRules.CIRRUS_GAS).x(), 0.0001);
        assertEquals(1.0, CloudMotionRules.apply(movement, CloudMotionRules.ALTOSTRATUS_GAS).x(), 0.0001);
        assertEquals(1.0, CloudMotionRules.apply(movement, CloudMotionRules.NIMBOSTRATUS_GAS).x(), 0.0001);
        assertEquals(1.0, CloudMotionRules.apply(movement, CloudMotionRules.CUMULONIMBUS_GAS).x(), 0.0001);
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
        assertThrows(IllegalArgumentException.class, () -> new CloudMotionRules.Motion(-0.1, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new CloudMotionRules.Motion(1.0, -0.1));
    }
}
