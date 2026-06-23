package com.tr1c.cloudcraft.weather;

import com.tr1c.cloudcraft.registry.ModIds;

import java.util.ArrayList;
import java.util.List;

public final class CloudWeatherRules {
    public static final int TICK_INTERVAL = 40;
    public static final int SCAN_RADIUS_XZ = 5;
    public static final int SCAN_RADIUS_Y = 4;
    public static final int WATER_SEARCH_DEPTH = 6;
    public static final int CUMULONIMBUS_THUNDER_THRESHOLD = 3;
    public static final int NIMBOSTRATUS_RAIN_THRESHOLD = 3;
    public static final int THUNDERSTORM_ROLLS = 5;
    public static final int RAIN_ROLLS = 2;

    private static final List<Offset> SCAN_OFFSETS = createScanOffsets();

    private CloudWeatherRules() {
    }

    public static boolean shouldTick(long gameTime) {
        return gameTime > 0 && gameTime % TICK_INTERVAL == 0;
    }

    public static List<Offset> scanOffsets() {
        return SCAN_OFFSETS;
    }

    public static CloudKind cloudKind(String blockId) {
        return switch (blockId) {
            case ModIds.CUMULONIMBUS_CLOUD_BLOCK, ModIds.CUMULONIMBUS_CLOUD_BLOCK_GAS -> CloudKind.CUMULONIMBUS;
            case ModIds.NIMBOSTRATUS_CLOUD_BLOCK, ModIds.NIMBOSTRATUS_CLOUD_BLOCK_GAS -> CloudKind.NIMBOSTRATUS;
            default -> CloudKind.NONE;
        };
    }

    public static WeatherEvent selectEvent(CloudCounts counts, int thunderstormRoll, int rainRoll) {
        if (counts.cumulonimbus() >= CUMULONIMBUS_THUNDER_THRESHOLD && thunderstormRoll == 0) {
            return WeatherEvent.THUNDERSTORM;
        }
        if (counts.nimbostratus() >= NIMBOSTRATUS_RAIN_THRESHOLD && rainRoll == 0) {
            return WeatherEvent.RAIN;
        }
        return WeatherEvent.NONE;
    }

    private static List<Offset> createScanOffsets() {
        List<Offset> offsets = new ArrayList<>();
        for (int y = -SCAN_RADIUS_Y; y <= SCAN_RADIUS_Y; y++) {
            for (int x = -SCAN_RADIUS_XZ; x <= SCAN_RADIUS_XZ; x++) {
                for (int z = -SCAN_RADIUS_XZ; z <= SCAN_RADIUS_XZ; z++) {
                    offsets.add(new Offset(x, y, z));
                }
            }
        }
        return List.copyOf(offsets);
    }

    public enum CloudKind {
        NONE,
        NIMBOSTRATUS,
        CUMULONIMBUS
    }

    public enum WeatherEvent {
        NONE,
        RAIN,
        THUNDERSTORM
    }

    public record CloudCounts(int nimbostratus, int cumulonimbus) {
        public CloudCounts add(CloudKind kind) {
            return switch (kind) {
                case NIMBOSTRATUS -> new CloudCounts(nimbostratus + 1, cumulonimbus);
                case CUMULONIMBUS -> new CloudCounts(nimbostratus, cumulonimbus + 1);
                case NONE -> this;
            };
        }
    }

    public record Offset(int x, int y, int z) {
    }
}
