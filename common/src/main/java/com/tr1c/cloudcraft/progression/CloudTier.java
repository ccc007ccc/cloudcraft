package com.tr1c.cloudcraft.progression;

public enum CloudTier {
    CUMULUS("cumulus"),
    STRATUS("stratus"),
    CIRRUS("cirrus"),
    ALTOSTRATUS("altostratus"),
    NIMBOSTRATUS("nimbostratus"),
    CUMULONIMBUS("cumulonimbus");

    private final String id;

    CloudTier(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public static CloudTier fromId(String id) {
        for (CloudTier tier : values()) {
            if (tier.id.equals(id)) {
                return tier;
            }
        }
        throw new IllegalArgumentException("Unknown cloud tier: " + id);
    }
}
