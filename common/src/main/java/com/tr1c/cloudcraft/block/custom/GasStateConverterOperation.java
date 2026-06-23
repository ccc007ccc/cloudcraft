package com.tr1c.cloudcraft.block.custom;

import com.tr1c.cloudcraft.registry.ModIds;

public enum GasStateConverterOperation {
    SOLIDIFY("solidify", ModIds.CUMULUS_CLOUD_FRAGMENT),
    GASIFY("gasify", ModIds.CIRRUS_FILAMENT);

    private final String id;
    private final String catalystItemId;

    GasStateConverterOperation(String id, String catalystItemId) {
        this.id = id;
        this.catalystItemId = catalystItemId;
    }

    public String id() {
        return id;
    }

    public String catalystItemId() {
        return catalystItemId;
    }

    public static GasStateConverterOperation byCatalyst(String itemId) {
        for (GasStateConverterOperation operation : values()) {
            if (operation.catalystItemId.equals(itemId)) {
                return operation;
            }
        }
        return null;
    }

    public static GasStateConverterOperation byId(String id) {
        for (GasStateConverterOperation operation : values()) {
            if (operation.id.equals(id)) {
                return operation;
            }
        }
        return null;
    }
}
