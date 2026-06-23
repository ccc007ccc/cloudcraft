package com.tr1c.cloudcraft.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public final class FabricCloudDimensionBootstrap {
    private FabricCloudDimensionBootstrap() {
    }

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(CloudDimensionBootstrap::ensureCloudDimension);
    }
}
