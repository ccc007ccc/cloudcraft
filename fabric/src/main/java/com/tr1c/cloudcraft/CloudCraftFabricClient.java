package com.tr1c.cloudcraft;

import com.tr1c.cloudcraft.block.FabricModBlocks;
import com.tr1c.cloudcraft.cloudtech.FabricCloudTechClient;
import com.tr1c.cloudcraft.entity.FabricModEntities;
import com.tr1c.cloudcraft.registry.CloudCraftRenderLayerDefinitions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.entity.GhastRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class CloudCraftFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CloudCraftRenderLayerDefinitions.forEachTranslucentBlock(FabricModBlocks::blockById, block ->
                BlockRenderLayerMap.putBlock(block, ChunkSectionLayer.TRANSLUCENT));
        EntityRendererRegistry.register(FabricModEntities.CLOUD_WISP, GhastRenderer::new);
        FabricCloudTechClient.register();
    }
}
