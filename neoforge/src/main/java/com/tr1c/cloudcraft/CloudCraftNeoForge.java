package com.tr1c.cloudcraft;

import com.tr1c.cloudcraft.block.NeoForgeModBlocks;
import com.tr1c.cloudcraft.datagen.NeoForgeDataGenerators;
import com.tr1c.cloudcraft.effect.NeoForgeModEffects;
import com.tr1c.cloudcraft.item.NeoForgeModCreativeModeTabs;
import com.tr1c.cloudcraft.item.NeoForgeModItems;
import com.tr1c.cloudcraft.potion.NeoForgeModPotions;
import com.tr1c.cloudcraft.registry.CloudCraftBrewingDefinitions;
import com.tr1c.cloudcraft.test.NeoForgeGameTests;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@Mod(CloudCraft.MOD_ID)
public class CloudCraftNeoForge {
    public CloudCraftNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener((RegisterGameTestsEvent event) -> NeoForgeGameTests.register(event));
        modEventBus.addListener(this::gatherClientData);

        NeoForgeModBlocks.register(modEventBus);
        NeoForgeModItems.register(modEventBus);
        NeoForgeModPotions.register(modEventBus);
        NeoForgeModEffects.register(modEventBus);
        NeoForgeGameTests.register(modEventBus);
        NeoForgeModCreativeModeTabs.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
    }

    private void gatherClientData(GatherDataEvent.Client event) {
        NeoForgeDataGenerators.register(event);
    }

    @SubscribeEvent
    public void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        CloudCraftBrewingDefinitions.BrewingMix solidCloud = CloudCraftBrewingDefinitions.solidCloud(NeoForgeModItems.CUMULUS_CLOUD_FRAGMENT.get(), NeoForgeModPotions.SOLID_CLOUD);
        event.getBuilder().addMix(solidCloud.input(), solidCloud.ingredient(), solidCloud.result());
    }
}
