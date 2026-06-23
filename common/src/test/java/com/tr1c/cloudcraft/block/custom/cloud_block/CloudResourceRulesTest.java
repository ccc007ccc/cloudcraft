package com.tr1c.cloudcraft.block.custom.cloud_block;

import com.tr1c.cloudcraft.registry.ModIds;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CloudResourceRulesTest {
    @Test
    void shouldExposePrimaryMaterialForEverySolidCloudFamily() {
        Map<String, CloudResourceRules.Drop> drops = CloudResourceRules.solidCloudDrops().stream()
                .collect(Collectors.toMap(CloudResourceRules.Drop::solidCloudId, drop -> drop));

        assertEquals(6, drops.size());
        assertDrop(drops.get(ModIds.CUMULUS_CLOUD_BLOCK), ModIds.CUMULUS_CLOUD_FRAGMENT, 2, 4);
        assertDrop(drops.get(ModIds.STRATUS_CLOUD_BLOCK), ModIds.STRATUS_WISP, 2, 4);
        assertDrop(drops.get(ModIds.CIRRUS_CLOUD_BLOCK), ModIds.CIRRUS_FILAMENT, 1, 2);
        assertDrop(drops.get(ModIds.ALTOSTRATUS_CLOUD_BLOCK), ModIds.ALTOSTRATUS_VEIL, 2, 4);
        assertDrop(drops.get(ModIds.NIMBOSTRATUS_CLOUD_BLOCK), ModIds.NIMBOSTRATUS_FLEECE, 2, 4);
        assertDrop(drops.get(ModIds.CUMULONIMBUS_CLOUD_BLOCK), ModIds.STORM_CORE, 1, 1);
    }

    private static void assertDrop(CloudResourceRules.Drop drop, String itemId, int minCount, int maxCount) {
        assertEquals(itemId, drop.itemId());
        assertEquals(minCount, drop.minCount());
        assertEquals(maxCount, drop.maxCount());
    }
}
