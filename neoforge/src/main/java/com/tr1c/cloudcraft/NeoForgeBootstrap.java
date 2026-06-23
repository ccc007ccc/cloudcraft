package com.tr1c.cloudcraft;

import com.tr1c.cloudcraft.block.NeoForgeModBlocks;
import com.tr1c.cloudcraft.block.NeoForgeModBlockEntities;
import com.tr1c.cloudcraft.cloudtech.NeoForgeCloudTechBootstrap;
import com.tr1c.cloudcraft.effect.NeoForgeModEffects;
import com.tr1c.cloudcraft.entity.NeoForgeModEntities;
import com.tr1c.cloudcraft.entity.CloudWispEntity;
import com.tr1c.cloudcraft.item.NeoForgeModCreativeModeTabs;
import com.tr1c.cloudcraft.item.NeoForgeModItems;
import com.tr1c.cloudcraft.potion.NeoForgeModPotions;
import com.tr1c.cloudcraft.registry.CloudCraftBrewingDefinitions;
import com.tr1c.cloudcraft.test.NeoForgeGameTests;
import com.tr1c.cloudcraft.weather.NeoForgeCloudWeatherBootstrap;
import com.tr1c.cloudcraft.world.NeoForgeCloudDimensionBootstrap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public final class NeoForgeBootstrap {
    private NeoForgeBootstrap() {
    }

    public static void registerModBus(IEventBus modEventBus) {
        modEventBus.addListener((RegisterGameTestsEvent event) -> NeoForgeGameTests.register(event));
        modEventBus.addListener(NeoForgeBootstrap::registerEntityAttributes);

        NeoForgeModBlocks.register(modEventBus);
        NeoForgeModBlockEntities.register(modEventBus);
        NeoForgeModEntities.register(modEventBus);
        NeoForgeModItems.register(modEventBus);
        NeoForgeModPotions.register(modEventBus);
        NeoForgeModEffects.register(modEventBus);
        NeoForgeGameTests.register(modEventBus);
        NeoForgeModCreativeModeTabs.register(modEventBus);
    }

    public static void registerGameBus() {
        NeoForge.EVENT_BUS.addListener(NeoForgeBootstrap::registerBrewingRecipes);
        NeoForge.EVENT_BUS.addListener(NeoForgeCloudWeatherBootstrap::onLevelTick);
        NeoForge.EVENT_BUS.addListener(NeoForgeCloudDimensionBootstrap::onServerStarted);
    }

    private static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        CloudCraftBrewingDefinitions.BrewingMix solidCloud = CloudCraftBrewingDefinitions.solidCloud(
                NeoForgeModItems.CUMULUS_CLOUD_FRAGMENT.get(),
                NeoForgeModPotions.SOLID_CLOUD);
        event.getBuilder().addMix(solidCloud.input(), solidCloud.ingredient(), solidCloud.result());
    }

    private static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(NeoForgeModEntities.CLOUD_WISP.get(), CloudWispEntity.createAttributes().build());
    }
}
