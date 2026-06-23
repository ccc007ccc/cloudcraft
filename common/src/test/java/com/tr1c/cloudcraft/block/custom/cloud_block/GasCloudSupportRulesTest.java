package com.tr1c.cloudcraft.block.custom.cloud_block;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GasCloudSupportRulesTest {
    @Test
    void shouldSupportTopLayerWithCloudWalker() {
        assertTrue(GasCloudSupportRules.shouldSupport(true, false, true));
    }

    @Test
    void shouldNotSupportWithoutCloudWalker() {
        assertFalse(GasCloudSupportRules.shouldSupport(false, false, true));
    }

    @Test
    void shouldNotSupportWhenCrouching() {
        assertFalse(GasCloudSupportRules.shouldSupport(true, true, true));
    }

    @Test
    void shouldNotSupportInsideCloudLayer() {
        assertFalse(GasCloudSupportRules.shouldSupport(true, false, false));
    }
}
