package com.tr1c.cloudcraft.block.custom;

import com.tr1c.cloudcraft.registry.ModIds;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GasStateConverterOperationTest {
    @Test
    void shouldResolveCatalystsToConverterOperations() {
        assertEquals(GasStateConverterOperation.SOLIDIFY, GasStateConverterOperation.byCatalyst(ModIds.CUMULUS_CLOUD_FRAGMENT));
        assertEquals(GasStateConverterOperation.GASIFY, GasStateConverterOperation.byCatalyst(ModIds.CIRRUS_FILAMENT));
        assertNull(GasStateConverterOperation.byCatalyst(ModIds.COMPRESSED_CANISTER));
    }

    @Test
    void shouldResolveStableOperationIds() {
        assertEquals(GasStateConverterOperation.SOLIDIFY, GasStateConverterOperation.byId("solidify"));
        assertEquals(GasStateConverterOperation.GASIFY, GasStateConverterOperation.byId("gasify"));
        assertNull(GasStateConverterOperation.byId("missing"));
    }
}
