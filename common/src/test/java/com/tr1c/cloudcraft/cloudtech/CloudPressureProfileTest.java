package com.tr1c.cloudcraft.cloudtech;

import com.tr1c.cloudcraft.progression.CloudTier;
import com.tr1c.cloudcraft.registry.ModIds;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CloudPressureProfileTest {
    @Test
    void shouldResolveGasCloudsToProfiles() {
        assertEquals(CloudTier.CUMULUS, CloudPressureProfile.find(ModIds.CUMULUS_CLOUD_BLOCK_GAS).tier());
        assertEquals(CloudTier.CIRRUS, CloudPressureProfile.find(ModIds.CIRRUS_CLOUD_BLOCK_GAS).tier());
        assertEquals(CloudTier.CUMULONIMBUS, CloudPressureProfile.find(ModIds.CUMULONIMBUS_CLOUD_BLOCK_GAS).tier());
    }

    @Test
    void shouldResolveSolidCloudsToProfiles() {
        assertEquals(CloudTier.CUMULUS, CloudPressureProfile.find(ModIds.CUMULUS_CLOUD_BLOCK).tier());
        assertEquals(CloudTier.STRATUS, CloudPressureProfile.find(ModIds.STRATUS_CLOUD_BLOCK).tier());
        assertEquals(CloudTier.CIRRUS, CloudPressureProfile.find(ModIds.CIRRUS_CLOUD_BLOCK).tier());
        assertEquals(CloudTier.ALTOSTRATUS, CloudPressureProfile.find(ModIds.ALTOSTRATUS_CLOUD_BLOCK).tier());
        assertEquals(CloudTier.NIMBOSTRATUS, CloudPressureProfile.find(ModIds.NIMBOSTRATUS_CLOUD_BLOCK).tier());
        assertEquals(CloudTier.CUMULONIMBUS, CloudPressureProfile.find(ModIds.CUMULONIMBUS_CLOUD_BLOCK).tier());
    }

    @Test
    void shouldReturnNullForUnknownCloudId() {
        assertNull(CloudPressureProfile.find("missing_cloud_gas"));
    }
}
