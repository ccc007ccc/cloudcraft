package com.tr1c.cloudcraft.weather;

import com.tr1c.cloudcraft.registry.ModIds;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloudWeatherRulesTest {
    @Test
    void shouldThrottleWeatherTicks() {
        assertEquals(40, CloudWeatherRules.TICK_INTERVAL);
        assertFalse(CloudWeatherRules.shouldTick(0));
        assertFalse(CloudWeatherRules.shouldTick(39));
        assertTrue(CloudWeatherRules.shouldTick(40));
    }

    @Test
    void shouldClassifyStormAndRainCloudsAcrossStates() {
        assertEquals(CloudWeatherRules.CloudKind.CUMULONIMBUS, CloudWeatherRules.cloudKind(ModIds.CUMULONIMBUS_CLOUD_BLOCK));
        assertEquals(CloudWeatherRules.CloudKind.CUMULONIMBUS, CloudWeatherRules.cloudKind(ModIds.CUMULONIMBUS_CLOUD_BLOCK_GAS));
        assertEquals(CloudWeatherRules.CloudKind.NIMBOSTRATUS, CloudWeatherRules.cloudKind(ModIds.NIMBOSTRATUS_CLOUD_BLOCK));
        assertEquals(CloudWeatherRules.CloudKind.NIMBOSTRATUS, CloudWeatherRules.cloudKind(ModIds.NIMBOSTRATUS_CLOUD_BLOCK_GAS));
        assertEquals(CloudWeatherRules.CloudKind.NONE, CloudWeatherRules.cloudKind(ModIds.CUMULUS_CLOUD_BLOCK));
    }

    @Test
    void shouldPrioritizeThunderstormOverRainWhenBothAreReady() {
        var counts = new CloudWeatherRules.CloudCounts(
                CloudWeatherRules.NIMBOSTRATUS_RAIN_THRESHOLD,
                CloudWeatherRules.CUMULONIMBUS_THUNDER_THRESHOLD);

        assertEquals(CloudWeatherRules.WeatherEvent.THUNDERSTORM, CloudWeatherRules.selectEvent(counts, 0, 0));
        assertEquals(CloudWeatherRules.WeatherEvent.RAIN, CloudWeatherRules.selectEvent(counts, 1, 0));
        assertEquals(CloudWeatherRules.WeatherEvent.NONE, CloudWeatherRules.selectEvent(counts, 1, 1));
    }
}
