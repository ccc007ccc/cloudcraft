package com.tr1c.cloudcraft.block.custom.cloud_block;

import com.tr1c.cloudcraft.registry.ModIds;

import java.util.List;
import java.util.Map;

public final class CloudResourceRules {
    private static final List<Drop> SOLID_CLOUD_DROPS = List.of(
            new Drop(ModIds.CUMULUS_CLOUD_BLOCK, ModIds.CUMULUS_CLOUD_FRAGMENT, 2, 4),
            new Drop(ModIds.STRATUS_CLOUD_BLOCK, ModIds.STRATUS_WISP, 2, 4),
            new Drop(ModIds.CIRRUS_CLOUD_BLOCK, ModIds.CIRRUS_FILAMENT, 1, 2),
            new Drop(ModIds.ALTOSTRATUS_CLOUD_BLOCK, ModIds.ALTOSTRATUS_VEIL, 2, 4),
            new Drop(ModIds.NIMBOSTRATUS_CLOUD_BLOCK, ModIds.NIMBOSTRATUS_FLEECE, 2, 4),
            new Drop(ModIds.CUMULONIMBUS_CLOUD_BLOCK, ModIds.STORM_CORE, 1, 1));
    private static final Map<String, Drop> DROP_BY_SOLID_CLOUD_ID = SOLID_CLOUD_DROPS.stream()
            .collect(java.util.stream.Collectors.toUnmodifiableMap(Drop::solidCloudId, drop -> drop));

    private CloudResourceRules() {
    }

    public static List<Drop> solidCloudDrops() {
        return SOLID_CLOUD_DROPS;
    }

    public static Drop primaryDropForSolidCloud(String solidCloudId) {
        Drop drop = DROP_BY_SOLID_CLOUD_ID.get(solidCloudId);
        if (drop == null) {
            throw new IllegalArgumentException("Unknown solid cloud resource drop: " + solidCloudId);
        }
        return drop;
    }

    public record Drop(String solidCloudId, String itemId, int minCount, int maxCount) {
        public Drop {
            if (minCount < 1) {
                throw new IllegalArgumentException("minCount must be positive");
            }
            if (maxCount < minCount) {
                throw new IllegalArgumentException("maxCount must be >= minCount");
            }
        }
    }
}
