package com.tr1c.cloudcraft.visual;

import com.tr1c.cloudcraft.registry.ModIds;

public final class CloudFeedbackRules {
    public static final int SOLID_CLOUD_AMBIENT_PARTICLE_ROLLS = 18;
    public static final int GAS_CLOUD_AMBIENT_PARTICLE_ROLLS = 8;
    public static final int CONVERTER_BASE_PARTICLES = 8;
    public static final int CONVERTER_PARTICLES_PER_BLOCK = 3;
    public static final int CONVERTER_MAX_PARTICLES = 36;
    public static final int JETPACK_THRUST_SOUND_INTERVAL_TICKS = 12;

    private CloudFeedbackRules() {
    }

    public static boolean shouldEmitAmbientParticle(int randomRoll) {
        return randomRoll == 0;
    }

    public static int converterParticleCount(int convertedBlocks) {
        if (convertedBlocks <= 0) {
            return 0;
        }
        return Math.min(CONVERTER_MAX_PARTICLES, CONVERTER_BASE_PARTICLES + convertedBlocks * CONVERTER_PARTICLES_PER_BLOCK);
    }

    public static boolean shouldPlayJetpackThrustSound(int tickCount) {
        return tickCount % JETPACK_THRUST_SOUND_INTERVAL_TICKS == 0;
    }

    public static float jetpackThrustPitch(String jetpackId) {
        return switch (jetpackId) {
            case ModIds.STABILIZED_CLOUD_JETPACK -> 1.25F;
            case ModIds.HIGH_PRESSURE_CLOUD_JETPACK -> 1.4F;
            default -> 1.1F;
        };
    }
}
