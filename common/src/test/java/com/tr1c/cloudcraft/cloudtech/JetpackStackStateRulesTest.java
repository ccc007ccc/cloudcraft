package com.tr1c.cloudcraft.cloudtech;

import com.tr1c.cloudcraft.progression.CloudProgressionRules;
import com.tr1c.cloudcraft.registry.ModIds;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JetpackStackStateRulesTest {
    @Test
    void shouldExposeTierSpecificCapacityValues() {
        assertEquals(1200, CloudProgressionRules.maxPressure(ModIds.CLOUD_JETPACK));
        assertEquals(1800, CloudProgressionRules.maxPressure(ModIds.STABILIZED_CLOUD_JETPACK));
        assertEquals(2400, CloudProgressionRules.maxPressure(ModIds.HIGH_PRESSURE_CLOUD_JETPACK));
    }

    @Test
    void shouldExposeTierSpecificHoverCosts() {
        assertEquals(12, CloudProgressionRules.hoverCost(ModIds.CLOUD_JETPACK));
        assertEquals(9, CloudProgressionRules.hoverCost(ModIds.STABILIZED_CLOUD_JETPACK));
        assertEquals(6, CloudProgressionRules.hoverCost(ModIds.HIGH_PRESSURE_CLOUD_JETPACK));
    }
}
