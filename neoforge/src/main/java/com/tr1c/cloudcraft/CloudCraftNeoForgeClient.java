package com.tr1c.cloudcraft;

import com.tr1c.cloudcraft.block.NeoForgeModBlocks;
import com.tr1c.cloudcraft.registry.CloudCraftRenderLayerDefinitions;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = CloudCraft.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = CloudCraft.MOD_ID, value = Dist.CLIENT)
public class CloudCraftNeoForgeClient {
    public CloudCraftNeoForgeClient(ModContainer container) {
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> CloudCraftRenderLayerDefinitions.forEachTranslucentBlock(NeoForgeModBlocks::blockById,
                block -> ItemBlockRenderTypes.setRenderLayer(block, ChunkSectionLayer.TRANSLUCENT)));
    }
}
