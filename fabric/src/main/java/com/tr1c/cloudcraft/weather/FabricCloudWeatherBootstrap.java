package com.tr1c.cloudcraft.weather;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public final class FabricCloudWeatherBootstrap {
    private FabricCloudWeatherBootstrap() {
    }

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(CloudWeatherRuntime::tick);
    }
}
