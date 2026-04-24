package com.tr1c.cloudcraft.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.registry.CloudCraftRegistryDefinitions;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class CloudCraftAssetProvider implements DataProvider {
    private static final List<String> ITEM_DEFINITION_IDS = itemDefinitionIds();
    private static final List<String> GENERATED_ITEM_MODEL_IDS = generatedItemModelIds();
    private static final List<String> BLOCK_PARENT_ITEM_MODEL_IDS = CloudCraftRegistryDefinitions.blockItemIds();

    private final PackOutput.PathProvider itemDefinitions;
    private final PackOutput.PathProvider itemModels;
    private final PackOutput.PathProvider blockStates;
    private final PackOutput.PathProvider blockModels;

    public CloudCraftAssetProvider(PackOutput output) {
        this.itemDefinitions = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
        this.itemModels = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
        this.blockStates = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.blockModels = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/block");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (String itemId : ITEM_DEFINITION_IDS) {
            save(futures, output, itemDefinitions, itemId, itemDefinition(assetPath("item", itemId)));
        }

        for (String itemId : GENERATED_ITEM_MODEL_IDS) {
            save(futures, output, itemModels, itemId, generatedItemModel(itemTexturePath(itemId)));
        }

        for (String itemId : BLOCK_PARENT_ITEM_MODEL_IDS) {
            save(futures, output, itemModels, itemId, blockParentItemModel(assetPath("block", itemId)));
        }

        save(futures, output, blockStates, ModIds.CUMULUS_CLOUD_BLOCK, singleVariantBlockState(assetPath("block", ModIds.CUMULUS_CLOUD_BLOCK)));
        save(futures, output, blockStates, ModIds.CUMULUS_CLOUD_BLOCK_GAS, singleVariantBlockState(assetPath("block", ModIds.CUMULUS_CLOUD_BLOCK_GAS)));
        save(futures, output, blockStates, ModIds.GAS_STATE_CONVERTER, horizontalFacingBlockState(assetPath("block", ModIds.GAS_STATE_CONVERTER)));

        save(futures, output, blockModels, ModIds.CUMULUS_CLOUD_BLOCK, cubeAllBlockModel(assetPath("block/cloud_block", ModIds.CUMULUS_CLOUD_BLOCK), false));
        save(futures, output, blockModels, ModIds.CUMULUS_CLOUD_BLOCK_GAS, cubeAllBlockModel(assetPath("block/cloud_block", ModIds.CUMULUS_CLOUD_BLOCK_GAS), true));

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return CloudCraft.MOD_ID + " asset provider";
    }

    private static List<String> itemDefinitionIds() {
        List<String> ids = new ArrayList<>(CloudCraftRegistryDefinitions.itemIds());
        ids.addAll(CloudCraftRegistryDefinitions.blockItemIds());
        ids.addAll(CloudCraftRegistryDefinitions.potionBottleItemModelIds());
        return List.copyOf(ids);
    }

    private static List<String> generatedItemModelIds() {
        List<String> ids = new ArrayList<>(CloudCraftRegistryDefinitions.itemIds());
        ids.addAll(CloudCraftRegistryDefinitions.potionBottleItemModelIds());
        return List.copyOf(ids);
    }

    private static String assetPath(String kind, String path) {
        return CloudCraft.MOD_ID + ":" + kind + "/" + path;
    }

    private static String itemTexturePath(String itemId) {
        return assetPath("item", itemId);
    }

    private static void save(List<CompletableFuture<?>> futures, CachedOutput output, PackOutput.PathProvider pathProvider, String path, JsonElement json) {
        futures.add(DataProvider.saveStable(output, json, pathProvider.json(ModIds.id(path))));
    }

    private static JsonObject itemDefinition(String model) {
        JsonObject root = new JsonObject();
        JsonObject modelObject = new JsonObject();
        modelObject.addProperty("type", "minecraft:model");
        modelObject.addProperty("model", model);
        root.add("model", modelObject);
        return root;
    }

    private static JsonObject generatedItemModel(String texture) {
        JsonObject root = parentModel("item/generated");
        root.add("textures", textureLayers(texture));
        return root;
    }

    private static JsonObject blockParentItemModel(String parent) {
        return parentModel(parent);
    }

    private static JsonObject singleVariantBlockState(String model) {
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();
        JsonObject variant = new JsonObject();
        variant.addProperty("model", model);
        variants.add("", variant);
        root.add("variants", variants);
        return root;
    }

    private static JsonObject horizontalFacingBlockState(String model) {
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();
        variants.add("facing=north", modelVariant(model, null));
        variants.add("facing=east", modelVariant(model, 90));
        variants.add("facing=south", modelVariant(model, 180));
        variants.add("facing=west", modelVariant(model, 270));
        root.add("variants", variants);
        return root;
    }

    private static JsonObject modelVariant(String model, Integer yRotation) {
        JsonObject variant = new JsonObject();
        variant.addProperty("model", model);
        if (yRotation != null) {
            variant.addProperty("y", yRotation);
        }
        return variant;
    }

    private static JsonObject cubeAllBlockModel(String texture, boolean translucent) {
        JsonObject root = parentModel("block/cube_all");
        root.add("textures", textureMapping("all", texture));
        if (translucent) {
            root.addProperty("render_type", "translucent");
        }
        return root;
    }

    private static JsonObject parentModel(String parent) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", parent);
        return root;
    }

    private static JsonObject textureLayers(String layer0) {
        return textureMapping("layer0", layer0);
    }

    private static JsonObject textureMapping(String key, String value) {
        JsonObject textures = new JsonObject();
        textures.addProperty(key, value);
        return textures;
    }
}
