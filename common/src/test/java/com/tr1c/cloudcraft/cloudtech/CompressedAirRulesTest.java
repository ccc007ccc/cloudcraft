package com.tr1c.cloudcraft.cloudtech;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompressedAirRulesTest {
    @Test
    void shouldClampPressureIntoValidRange() {
        assertEquals(0, CompressedAirRules.clamp(-1));
        assertEquals(400, CompressedAirRules.clamp(400));
        assertEquals(CompressedAirRules.MAX_PRESSURE, CompressedAirRules.clamp(CompressedAirRules.MAX_PRESSURE + 1));
    }

    @Test
    void shouldFillFromCloudFragmentWithoutOverflow() {
        assertEquals(150, CompressedAirRules.fillFromFragment(0));
        assertEquals(CompressedAirRules.MAX_PRESSURE, CompressedAirRules.fillFromFragment(CompressedAirRules.MAX_PRESSURE));
    }

    @Test
    void shouldConsumePressureForThrust() {
        assertEquals(80, CompressedAirRules.consumeThrust(100));
        assertEquals(0, CompressedAirRules.consumeThrust(10));
        assertEquals(84, CompressedAirRules.consumeThrust(100, com.tr1c.cloudcraft.registry.ModIds.STABILIZED_CLOUD_JETPACK));
    }

    @Test
    void shouldRequireEnoughPressureForThrust() {
        assertFalse(CompressedAirRules.canThrust(CompressedAirRules.THRUST_COST - 1));
        assertTrue(CompressedAirRules.canThrust(CompressedAirRules.THRUST_COST));
        assertTrue(CompressedAirRules.canThrust(16, com.tr1c.cloudcraft.registry.ModIds.STABILIZED_CLOUD_JETPACK));
    }

    @Test
    void shouldExposeCloudSpecificRechargeRates() {
        assertEquals(CompressedAirRules.CIRRUS_RECHARGE, CompressedAirRules.rechargeRate("cirrus_cloud_block_gas"));
        assertEquals(CompressedAirRules.CIRRUS_RECHARGE, CompressedAirRules.rechargeRate("cirrus_cloud_block"));
        assertEquals(CompressedAirRules.ALTOSTRATUS_RECHARGE, CompressedAirRules.rechargeRate("altostratus_cloud_block_gas"));
        assertEquals(CompressedAirRules.ALTOSTRATUS_RECHARGE, CompressedAirRules.rechargeRate("altostratus_cloud_block"));
        assertEquals(CompressedAirRules.NIMBOSTRATUS_RECHARGE, CompressedAirRules.rechargeRate("nimbostratus_cloud_block_gas"));
        assertEquals(CompressedAirRules.NIMBOSTRATUS_RECHARGE, CompressedAirRules.rechargeRate("nimbostratus_cloud_block"));
        assertEquals(CompressedAirRules.CUMULONIMBUS_RECHARGE, CompressedAirRules.rechargeRate("cumulonimbus_cloud_block_gas"));
        assertEquals(CompressedAirRules.CUMULONIMBUS_RECHARGE, CompressedAirRules.rechargeRate("cumulonimbus_cloud_block"));
        assertEquals(CompressedAirRules.CUMULUS_RECHARGE, CompressedAirRules.rechargeRate("cumulus_cloud_block_gas"));
        assertEquals(CompressedAirRules.CUMULUS_RECHARGE, CompressedAirRules.rechargeRate("cumulus_cloud_block"));
        assertEquals(CompressedAirRules.STRATUS_RECHARGE, CompressedAirRules.rechargeRate("stratus_cloud_block_gas"));
        assertEquals(CompressedAirRules.STRATUS_RECHARGE, CompressedAirRules.rechargeRate("stratus_cloud_block"));
        assertEquals(0, CompressedAirRules.rechargeRate("unknown"));
        assertEquals(CompressedAirRules.CIRRUS_RECHARGE + 2, CompressedAirRules.rechargeRate("cirrus_cloud_block_gas", com.tr1c.cloudcraft.registry.ModIds.STABILIZED_CLOUD_JETPACK));
        assertEquals(CompressedAirRules.CIRRUS_RECHARGE + 2, CompressedAirRules.rechargeRate("cirrus_cloud_block", com.tr1c.cloudcraft.registry.ModIds.STABILIZED_CLOUD_JETPACK));
    }

    @Test
    void shouldKeepEachCloudFamilyRechargeRateDistinct() {
        Set<Integer> rates = Set.of(
                CompressedAirRules.CUMULUS_RECHARGE,
                CompressedAirRules.STRATUS_RECHARGE,
                CompressedAirRules.CIRRUS_RECHARGE,
                CompressedAirRules.ALTOSTRATUS_RECHARGE,
                CompressedAirRules.NIMBOSTRATUS_RECHARGE,
                CompressedAirRules.CUMULONIMBUS_RECHARGE);

        assertEquals(6, rates.size());
    }

    @Test
    void shouldRejectNegativeAirAddition() {
        assertThrows(IllegalArgumentException.class, () -> CompressedAirRules.add(0, -1));
    }
}
