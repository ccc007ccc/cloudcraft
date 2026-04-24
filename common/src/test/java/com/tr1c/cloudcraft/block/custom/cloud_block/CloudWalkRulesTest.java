package com.tr1c.cloudcraft.block.custom.cloud_block;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloudWalkRulesTest {
    @Test
    void shouldUseBaseSafeFallDistanceWithoutCloudWalker() {
        assertEquals(1.5f, CloudWalkRules.safeFallDistance(false, false));
        assertEquals(1.5f, CloudWalkRules.safeFallDistance(false, true));
    }

    @Test
    void shouldUseCloudWalkerSafeFallDistanceWhenStanding() {
        assertEquals(2.5f, CloudWalkRules.safeFallDistance(true, false));
    }

    @Test
    void shouldUseCrouchingCloudWalkerSafeFallDistanceWhenCrouching() {
        assertEquals(4.5f, CloudWalkRules.safeFallDistance(true, true));
    }

    @Test
    void shouldBreakWhenStandingWithoutCloudWalker() {
        assertTrue(CloudWalkRules.shouldBreakCloud(false, false, 0.0f));
    }

    @Test
    void shouldPreserveWithCloudWalkerWithinSafeThreshold() {
        assertFalse(CloudWalkRules.shouldBreakCloud(true, false, 2.5f));
        assertFalse(CloudWalkRules.shouldBreakCloud(true, false, 0.1f));
        assertFalse(CloudWalkRules.shouldBreakCloud(true, true, 4.5f));
    }

    @Test
    void shouldPreserveWhenCrouchingWithoutCloudWalkerWithinSafeThreshold() {
        assertFalse(CloudWalkRules.shouldBreakCloud(false, true, 1.5f));
    }

    @Test
    void shouldBreakBeyondSafeThreshold() {
        assertTrue(CloudWalkRules.shouldBreakCloud(false, true, 1.6f));
        assertTrue(CloudWalkRules.shouldBreakCloud(true, false, 2.6f));
        assertTrue(CloudWalkRules.shouldBreakCloud(true, true, 4.6f));
    }
}
