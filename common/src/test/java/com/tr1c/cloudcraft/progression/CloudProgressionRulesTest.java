package com.tr1c.cloudcraft.progression;

import com.tr1c.cloudcraft.registry.ModIds;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloudProgressionRulesTest {
    @Test
    void shouldExposeJetpackUpgradeChain() {
        assertTrue(CloudProgressionRules.isJetpackId(ModIds.CLOUD_JETPACK));
        assertTrue(CloudProgressionRules.isJetpackId(ModIds.STABILIZED_CLOUD_JETPACK));
        assertTrue(CloudProgressionRules.isJetpackId(ModIds.HIGH_PRESSURE_CLOUD_JETPACK));
    }

    @Test
    void shouldMapJetpacksToExpectedTiersAndCaps() {
        assertEquals(CloudTier.CUMULUS, CloudProgressionRules.tierForJetpack(ModIds.CLOUD_JETPACK));
        assertEquals(CloudTier.ALTOSTRATUS, CloudProgressionRules.tierForJetpack(ModIds.STABILIZED_CLOUD_JETPACK));
        assertEquals(CloudTier.CUMULONIMBUS, CloudProgressionRules.tierForJetpack(ModIds.HIGH_PRESSURE_CLOUD_JETPACK));
        assertEquals(1200, CloudProgressionRules.maxPressure(ModIds.CLOUD_JETPACK));
        assertEquals(1800, CloudProgressionRules.maxPressure(ModIds.STABILIZED_CLOUD_JETPACK));
        assertEquals(2400, CloudProgressionRules.maxPressure(ModIds.HIGH_PRESSURE_CLOUD_JETPACK));
    }

    @Test
    void shouldMapMaterialsToExpectedTiers() {
        assertEquals(CloudTier.CUMULUS, CloudProgressionRules.tierForMaterial(ModIds.COMPRESSED_CANISTER));
        assertEquals(CloudTier.CIRRUS, CloudProgressionRules.tierForMaterial(ModIds.CIRRUS_FILAMENT));
        assertEquals(CloudTier.CUMULONIMBUS, CloudProgressionRules.tierForMaterial(ModIds.STORM_CORE));
    }

    @Test
    void shouldExposeUnlockTrackedItems() {
        assertTrue(CloudProgressionRules.unlockIds().contains(ModIds.BASIC_JETPACK_FRAME));
        assertTrue(CloudProgressionRules.unlockIds().contains(ModIds.STABILIZED_CLOUD_JETPACK));
        assertTrue(CloudProgressionRules.unlockIds().contains(ModIds.HIGH_PRESSURE_CLOUD_JETPACK));
    }
}
