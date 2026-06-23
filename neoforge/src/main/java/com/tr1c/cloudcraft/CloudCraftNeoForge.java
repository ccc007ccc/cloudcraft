package com.tr1c.cloudcraft;

import com.tr1c.cloudcraft.config.NeoForgeCloudCraftConfig;
import com.tr1c.cloudcraft.datagen.NeoForgeDataGenerators;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(CloudCraft.MOD_ID)
public class CloudCraftNeoForge {
    public CloudCraftNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        NeoForgeCloudCraftConfig.register(modContainer, modEventBus);
        modEventBus.addListener(this::gatherClientData);
        NeoForgeBootstrap.registerModBus(modEventBus);
        NeoForgeBootstrap.registerGameBus();
    }

    private void gatherClientData(GatherDataEvent.Client event) {
        NeoForgeDataGenerators.registerClient(event);
    }

}
