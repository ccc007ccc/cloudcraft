package com.tr1c.cloudcraft.weather;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public final class NeoForgeCloudWeatherBootstrap {
    private NeoForgeCloudWeatherBootstrap() {
    }

    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel level) {
            CloudWeatherRuntime.tick(level);
        }
    }
}
