package com.tr1c.cloudcraft.block.custom.cloud_block;

public final class GasCloudSupportRules {
    private GasCloudSupportRules() {
    }

    public static boolean shouldSupport(boolean hasCloudWalker, boolean crouching, boolean topLayer) {
        return hasCloudWalker && !crouching && topLayer;
    }
}
