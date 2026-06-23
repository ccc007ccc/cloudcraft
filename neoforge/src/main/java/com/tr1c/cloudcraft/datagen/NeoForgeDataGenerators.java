package com.tr1c.cloudcraft.datagen;

import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class NeoForgeDataGenerators {
    private NeoForgeDataGenerators() {
    }

    public static void registerClient(GatherDataEvent.Client event) {
        event.createProvider(CloudCraftAssetProvider::new);
        event.createProvider(CloudCraftAdvancementProvider::new);
        event.createProvider(CloudCraftRecipeProvider::new);
        event.createProvider(CloudCraftLootTableProvider::new);
    }
}
