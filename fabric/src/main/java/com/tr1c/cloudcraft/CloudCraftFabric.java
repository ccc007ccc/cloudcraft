package com.tr1c.cloudcraft;

import com.tr1c.cloudcraft.block.FabricModBlocks;
import com.tr1c.cloudcraft.block.FabricModBlockEntities;
import com.tr1c.cloudcraft.cloudtech.FabricCloudTechBootstrap;
import com.tr1c.cloudcraft.config.FabricCloudCraftConfig;
import com.tr1c.cloudcraft.entity.FabricModEntities;
import com.tr1c.cloudcraft.item.FabricModCreativeModeTabs;
import com.tr1c.cloudcraft.item.FabricModItems;
import com.tr1c.cloudcraft.potion.FabricModPotions;
import com.tr1c.cloudcraft.registry.CloudCraftBrewingDefinitions;
import com.tr1c.cloudcraft.weather.FabricCloudWeatherBootstrap;
import com.tr1c.cloudcraft.world.FabricCloudDimensionBootstrap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class CloudCraftFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FabricCloudCraftConfig.load();
        FabricModBlocks.register();
        FabricModBlockEntities.register();
        FabricModEntities.register();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, FabricModCreativeModeTabs.CLOUD_TAB_KEY, FabricModCreativeModeTabs.CLOUD_TAB);

        CloudCraftBrewingDefinitions.BrewingMix solidCloud = CloudCraftBrewingDefinitions.solidCloud(FabricModItems.CUMULUS_CLOUD_FRAGMENT, FabricModPotions.SOLID_CLOUD);
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> builder.addMix(solidCloud.input(), solidCloud.ingredient(), solidCloud.result()));
        FabricCloudTechBootstrap.register();
        FabricCloudWeatherBootstrap.register();
        FabricCloudDimensionBootstrap.register();
    }
}
