package com.tr1c.cloudcraft.block.custom.cloud_block;

public final class CloudWalkRules {
    public static final float BASE_SAFE_FALL_DISTANCE = 1.5f;
    public static final float CLOUD_WALKER_SAFE_FALL_DISTANCE = 2.5f;
    public static final float CROUCHING_CLOUD_WALKER_SAFE_FALL_DISTANCE = 4.5f;

    private CloudWalkRules() {
    }

    public static boolean shouldBreakCloud(boolean hasCloudWalker, boolean crouching, float fallDistance) {
        float safeFallDistance = safeFallDistance(hasCloudWalker, crouching);
        return fallDistance > safeFallDistance || (!hasCloudWalker && !crouching);
    }

    public static float safeFallDistance(boolean hasCloudWalker, boolean crouching) {
        if (!hasCloudWalker) {
            return BASE_SAFE_FALL_DISTANCE;
        }
        if (crouching) {
            return CROUCHING_CLOUD_WALKER_SAFE_FALL_DISTANCE;
        }
        return CLOUD_WALKER_SAFE_FALL_DISTANCE;
    }

}
