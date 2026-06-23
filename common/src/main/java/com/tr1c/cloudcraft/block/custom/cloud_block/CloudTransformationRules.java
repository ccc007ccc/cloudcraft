package com.tr1c.cloudcraft.block.custom.cloud_block;

import com.tr1c.cloudcraft.registry.ModIds;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CloudTransformationRules {
    public static final double SPLASH_POTION_RADIUS = 1.5;
    public static final double LINGERING_POTION_RADIUS = 3.0;
    public static final double CONVERTER_RADIUS = 2.5;
    public static final float CLOUD_FRAGMENT_DROP_CHANCE = 0.1f;

    private static final List<Offset> EFFECT_OFFSETS = List.of(
            new Offset(0, -1, 0),
            new Offset(1, -1, 0),
            new Offset(-1, -1, 0),
            new Offset(0, -1, 1),
            new Offset(0, -1, -1)
    );
    private static final List<Offset> CROSS_OFFSETS = List.of(
            new Offset(0, -1, 0),
            new Offset(1, 0, 0),
            new Offset(-1, 0, 0),
            new Offset(0, 0, 1),
            new Offset(0, 0, -1)
    );
    private static final List<Offset> SPLASH_RADIUS_OFFSETS = computeRadiusOffsets(SPLASH_POTION_RADIUS);
    private static final List<Offset> LINGERING_RADIUS_OFFSETS = computeRadiusOffsets(LINGERING_POTION_RADIUS);
    private static final List<Offset> CONVERTER_RADIUS_OFFSETS = computeRadiusOffsets(CONVERTER_RADIUS);
    private static final Map<String, String> SOLID_TO_GAS_IDS = solidToGasIds();

    private CloudTransformationRules() {
    }

    public static List<Offset> effectOffsets() {
        return EFFECT_OFFSETS;
    }

    public static List<Offset> crossOffsets() {
        return CROSS_OFFSETS;
    }

    public static List<Offset> radiusOffsets(double radius) {
        if (radius == SPLASH_POTION_RADIUS) {
            return SPLASH_RADIUS_OFFSETS;
        }
        if (radius == LINGERING_POTION_RADIUS) {
            return LINGERING_RADIUS_OFFSETS;
        }
        if (radius == CONVERTER_RADIUS) {
            return CONVERTER_RADIUS_OFFSETS;
        }
        return computeRadiusOffsets(radius);
    }

    public static String gasCloudIdForSolidCloudId(String solidCloudId) {
        return SOLID_TO_GAS_IDS.get(solidCloudId);
    }

    public static boolean isSolidCloudId(String blockId) {
        return SOLID_TO_GAS_IDS.containsKey(blockId);
    }

    private static Map<String, String> solidToGasIds() {
        Map<String, String> ids = new LinkedHashMap<>();
        ids.put(ModIds.CUMULUS_CLOUD_BLOCK, ModIds.CUMULUS_CLOUD_BLOCK_GAS);
        ids.put(ModIds.STRATUS_CLOUD_BLOCK, ModIds.STRATUS_CLOUD_BLOCK_GAS);
        ids.put(ModIds.CIRRUS_CLOUD_BLOCK, ModIds.CIRRUS_CLOUD_BLOCK_GAS);
        ids.put(ModIds.ALTOSTRATUS_CLOUD_BLOCK, ModIds.ALTOSTRATUS_CLOUD_BLOCK_GAS);
        ids.put(ModIds.NIMBOSTRATUS_CLOUD_BLOCK, ModIds.NIMBOSTRATUS_CLOUD_BLOCK_GAS);
        ids.put(ModIds.CUMULONIMBUS_CLOUD_BLOCK, ModIds.CUMULONIMBUS_CLOUD_BLOCK_GAS);
        return Map.copyOf(ids);
    }

    private static List<Offset> computeRadiusOffsets(double radius) {
        int intRadius = (int) Math.ceil(radius);
        double radiusSquared = radius * radius;
        List<Offset> targets = new ArrayList<>((intRadius * 2 + 1) * (intRadius * 2 + 1) * (intRadius * 2 + 1));
        for (int dx = -intRadius; dx <= intRadius; dx++) {
            for (int dy = -intRadius; dy <= intRadius; dy++) {
                for (int dz = -intRadius; dz <= intRadius; dz++) {
                    if (dx * dx + dy * dy + dz * dz <= radiusSquared) {
                        targets.add(new Offset(dx, dy, dz));
                    }
                }
            }
        }
        return targets;
    }

    public record Offset(int x, int y, int z) {
    }
}
