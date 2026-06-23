package com.tr1c.cloudcraft.world;

import net.neoforged.neoforge.event.server.ServerStartedEvent;

public final class NeoForgeCloudDimensionBootstrap {
    private NeoForgeCloudDimensionBootstrap() {
    }

    public static void onServerStarted(ServerStartedEvent event) {
        CloudDimensionBootstrap.ensureCloudDimension(event.getServer());
    }
}
