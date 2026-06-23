package com.tr1c.cloudcraft.visual;

import com.tr1c.cloudcraft.registry.ModIds;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloudFeedbackRulesTest {
    @Test
    void shouldKeepAmbientCloudParticlesSparse() {
        assertEquals(18, CloudFeedbackRules.SOLID_CLOUD_AMBIENT_PARTICLE_ROLLS);
        assertEquals(8, CloudFeedbackRules.GAS_CLOUD_AMBIENT_PARTICLE_ROLLS);
        assertTrue(CloudFeedbackRules.shouldEmitAmbientParticle(0));
        assertFalse(CloudFeedbackRules.shouldEmitAmbientParticle(1));
    }

    @Test
    void shouldScaleConverterParticlesWithConvertedBlocks() {
        assertEquals(0, CloudFeedbackRules.converterParticleCount(0));
        assertEquals(11, CloudFeedbackRules.converterParticleCount(1));
        assertEquals(36, CloudFeedbackRules.converterParticleCount(32));
    }

    @Test
    void shouldThrottleJetpackThrustSound() {
        assertTrue(CloudFeedbackRules.shouldPlayJetpackThrustSound(24));
        assertFalse(CloudFeedbackRules.shouldPlayJetpackThrustSound(25));
    }

    @Test
    void shouldRaiseThrustPitchByJetpackTier() {
        assertTrue(CloudFeedbackRules.jetpackThrustPitch(ModIds.CLOUD_JETPACK)
                < CloudFeedbackRules.jetpackThrustPitch(ModIds.STABILIZED_CLOUD_JETPACK));
        assertTrue(CloudFeedbackRules.jetpackThrustPitch(ModIds.STABILIZED_CLOUD_JETPACK)
                < CloudFeedbackRules.jetpackThrustPitch(ModIds.HIGH_PRESSURE_CLOUD_JETPACK));
    }
}
