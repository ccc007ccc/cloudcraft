package com.tr1c.cloudcraft.config;

import com.tr1c.cloudcraft.block.custom.cloud_block.CloudMotionRules;
import com.tr1c.cloudcraft.cloudtech.CompressedAirRules;
import com.tr1c.cloudcraft.progression.CloudProgressionRules;
import com.tr1c.cloudcraft.registry.ModIds;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloudCraftConfigTest {
    @BeforeEach
    @AfterEach
    void resetConfig() {
        CloudCraftConfig.reset();
    }

    @Test
    void defaultsShouldPreserveExistingGameplayValues() {
        assertEquals(1200, CloudProgressionRules.maxPressure(ModIds.CLOUD_JETPACK));
        assertEquals(20, CloudProgressionRules.thrustCost(ModIds.CLOUD_JETPACK));
        assertEquals(150, CompressedAirRules.fillFromFragment(0));
        assertEquals(12, CompressedAirRules.rechargeRate(ModIds.CIRRUS_CLOUD_BLOCK_GAS));
        assertEquals(0.18D, CloudProgressionRules.thrustVerticalSpeed(ModIds.CLOUD_JETPACK), 0.0001D);
    }

    @Test
    void configuredMultipliersShouldScaleKeyGameplayRules() {
        CloudCraftConfig.apply(new CloudCraftConfig.Values(
                2.0D,
                0.5D,
                3.0D,
                1.5D,
                2.0D,
                0.5D,
                0.5D,
                2.0D));

        assertEquals(2400, CloudProgressionRules.maxPressure(ModIds.CLOUD_JETPACK));
        assertEquals(10, CloudProgressionRules.thrustCost(ModIds.CLOUD_JETPACK));
        assertEquals(6, CloudProgressionRules.hoverCost(ModIds.CLOUD_JETPACK));
        assertEquals(450, CompressedAirRules.fillFromFragment(0));
        assertEquals(42, CompressedAirRules.rechargeRate(ModIds.CIRRUS_CLOUD_BLOCK_GAS, ModIds.STABILIZED_CLOUD_JETPACK));
        assertEquals(0.27D, CloudProgressionRules.thrustVerticalSpeed(ModIds.CLOUD_JETPACK), 0.0001D);
        assertEquals(0.13D, CloudProgressionRules.thrustHorizontalAcceleration(ModIds.CLOUD_JETPACK), 0.0001D);
        assertEquals(0.15D, CloudProgressionRules.thrustHorizontalMaxSpeed(ModIds.CLOUD_JETPACK), 0.0001D);

        CloudMotionRules.Movement adjusted = CloudMotionRules.apply(
                new CloudMotionRules.Movement(1.0D, 0.0D, 0.0D),
                CloudMotionRules.STRATUS_GAS);
        assertEquals(0.5952D, adjusted.x(), 0.0001D);
    }

    @Test
    void zeroCostMultiplierShouldAllowCostFreeThrust() {
        CloudCraftConfig.apply(new CloudCraftConfig.Values(
                1.0D,
                0.0D,
                1.0D,
                1.0D,
                1.0D,
                1.0D,
                1.0D,
                1.0D));

        assertEquals(0, CloudProgressionRules.thrustCost(ModIds.CLOUD_JETPACK));
        assertTrue(CompressedAirRules.canThrust(0));
    }

    @Test
    void shouldRejectInvalidMultipliers() {
        assertThrows(IllegalArgumentException.class, () -> new CloudCraftConfig.Values(
                0.0D,
                1.0D,
                1.0D,
                1.0D,
                1.0D,
                1.0D,
                1.0D,
                1.0D));
        assertThrows(IllegalArgumentException.class, () -> new CloudCraftConfig.Values(
                1.0D,
                -1.0D,
                1.0D,
                1.0D,
                1.0D,
                1.0D,
                1.0D,
                1.0D));
    }
}
