package com.tr1c.cloudcraft.datagen;

import com.mojang.serialization.JsonOps;
import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class CloudCraftRecipeProvider implements DataProvider {
    private final PackOutput.PathProvider recipes;

    public CloudCraftRecipeProvider(PackOutput output) {
        this.recipes = output.createPathProvider(PackOutput.Target.DATA_PACK, "recipe");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        RecipeOutput recipeOutput = recipeOutput(output, futures);

        ShapedRecipeBuilder.shaped(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.CUMULUS_CLOUD_BLOCK))
                .define('F', item(ModIds.CUMULUS_CLOUD_FRAGMENT))
                .pattern("FF")
                .pattern("FF")
                .unlockedBy("has_cumulus_cloud_fragment", has(item(ModIds.CUMULUS_CLOUD_FRAGMENT)))
                .save(recipeOutput, recipeKey(ModIds.CUMULUS_CLOUD_BLOCK));

        ShapelessRecipeBuilder.shapeless(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.CUMULUS_CLOUD_FRAGMENT), 4)
                .requires(item(ModIds.CUMULUS_CLOUD_BLOCK))
                .unlockedBy("has_cumulus_cloud_block", has(item(ModIds.CUMULUS_CLOUD_BLOCK)))
                .save(recipeOutput, recipeKey(ModIds.CUMULUS_CLOUD_FRAGMENT));

        ShapelessRecipeBuilder.shapeless(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.STRATUS_WISP), 4)
                .requires(item(ModIds.STRATUS_CLOUD_BLOCK))
                .unlockedBy("has_stratus_cloud_block", has(item(ModIds.STRATUS_CLOUD_BLOCK)))
                .save(recipeOutput, recipeKey(ModIds.STRATUS_WISP));

        ShapelessRecipeBuilder.shapeless(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.STRATUS_CLOUD_BLOCK))
                .requires(item(ModIds.CUMULUS_CLOUD_BLOCK))
                .requires(Items.SNOWBALL)
                .unlockedBy("has_cumulus_cloud_block", has(item(ModIds.CUMULUS_CLOUD_BLOCK)))
                .save(recipeOutput, recipeKey(ModIds.STRATUS_CLOUD_BLOCK));

        ShapelessRecipeBuilder.shapeless(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.CIRRUS_CLOUD_BLOCK))
                .requires(item(ModIds.CUMULUS_CLOUD_BLOCK))
                .requires(Items.FEATHER)
                .unlockedBy("has_cumulus_cloud_block", has(item(ModIds.CUMULUS_CLOUD_BLOCK)))
                .save(recipeOutput, recipeKey(ModIds.CIRRUS_CLOUD_BLOCK));

        ShapelessRecipeBuilder.shapeless(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.ALTOSTRATUS_CLOUD_BLOCK))
                .requires(item(ModIds.STRATUS_CLOUD_BLOCK))
                .requires(Items.LIGHT_GRAY_DYE)
                .unlockedBy("has_stratus_cloud_block", has(item(ModIds.STRATUS_CLOUD_BLOCK)))
                .save(recipeOutput, recipeKey(ModIds.ALTOSTRATUS_CLOUD_BLOCK));

        ShapelessRecipeBuilder.shapeless(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.NIMBOSTRATUS_CLOUD_BLOCK))
                .requires(item(ModIds.STRATUS_CLOUD_BLOCK))
                .requires(Items.WATER_BUCKET)
                .unlockedBy("has_stratus_cloud_block", has(item(ModIds.STRATUS_CLOUD_BLOCK)))
                .save(recipeOutput, recipeKey(ModIds.NIMBOSTRATUS_CLOUD_BLOCK));

        ShapelessRecipeBuilder.shapeless(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.CUMULONIMBUS_CLOUD_BLOCK))
                .requires(item(ModIds.CUMULUS_CLOUD_BLOCK))
                .requires(Items.FIRE_CHARGE)
                .unlockedBy("has_cumulus_cloud_block", has(item(ModIds.CUMULUS_CLOUD_BLOCK)))
                .save(recipeOutput, recipeKey(ModIds.CUMULONIMBUS_CLOUD_BLOCK));

        ShapelessRecipeBuilder.shapeless(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.COMPRESSED_CANISTER))
                .requires(item(ModIds.CUMULUS_CLOUD_FRAGMENT))
                .requires(Items.COPPER_INGOT)
                .unlockedBy("has_cumulus_cloud_fragment", has(item(ModIds.CUMULUS_CLOUD_FRAGMENT)))
                .save(recipeOutput, recipeKey(ModIds.COMPRESSED_CANISTER));

        ShapelessRecipeBuilder.shapeless(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.CIRRUS_FILAMENT))
                .requires(item(ModIds.CIRRUS_CLOUD_BLOCK))
                .requires(Items.STRING)
                .unlockedBy("has_cirrus_cloud_block", has(item(ModIds.CIRRUS_CLOUD_BLOCK)))
                .save(recipeOutput, recipeKey(ModIds.CIRRUS_FILAMENT));

        ShapelessRecipeBuilder.shapeless(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.ALTOSTRATUS_VEIL), 4)
                .requires(item(ModIds.ALTOSTRATUS_CLOUD_BLOCK))
                .unlockedBy("has_altostratus_cloud_block", has(item(ModIds.ALTOSTRATUS_CLOUD_BLOCK)))
                .save(recipeOutput, recipeKey(ModIds.ALTOSTRATUS_VEIL));

        ShapelessRecipeBuilder.shapeless(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.NIMBOSTRATUS_FLEECE), 4)
                .requires(item(ModIds.NIMBOSTRATUS_CLOUD_BLOCK))
                .unlockedBy("has_nimbostratus_cloud_block", has(item(ModIds.NIMBOSTRATUS_CLOUD_BLOCK)))
                .save(recipeOutput, recipeKey(ModIds.NIMBOSTRATUS_FLEECE));

        ShapelessRecipeBuilder.shapeless(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.STORM_CORE))
                .requires(item(ModIds.CUMULONIMBUS_CLOUD_BLOCK))
                .requires(Items.BLAZE_POWDER)
                .unlockedBy("has_cumulonimbus_cloud_block", has(item(ModIds.CUMULONIMBUS_CLOUD_BLOCK)))
                .save(recipeOutput, recipeKey(ModIds.STORM_CORE));

        ShapedRecipeBuilder.shaped(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.BASIC_JETPACK_FRAME))
                .define('I', Items.IRON_INGOT)
                .define('C', item(ModIds.COMPRESSED_CANISTER))
                .pattern("I I")
                .pattern("ICI")
                .pattern(" I ")
                .unlockedBy("has_compressed_canister", has(item(ModIds.COMPRESSED_CANISTER)))
                .save(recipeOutput, recipeKey(ModIds.BASIC_JETPACK_FRAME));

        ShapedRecipeBuilder.shaped(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.STABILIZED_NOZZLE))
                .define('F', item(ModIds.CIRRUS_FILAMENT))
                .define('C', item(ModIds.COMPRESSED_CANISTER))
                .define('V', item(ModIds.ALTOSTRATUS_VEIL))
                .pattern(" F ")
                .pattern("VCV")
                .pattern(" F ")
                .unlockedBy("has_cirrus_filament", has(item(ModIds.CIRRUS_FILAMENT)))
                .save(recipeOutput, recipeKey(ModIds.STABILIZED_NOZZLE));

        ShapedRecipeBuilder.shaped(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.HIGH_PRESSURE_CHAMBER))
                .define('S', item(ModIds.STORM_CORE))
                .define('C', item(ModIds.COMPRESSED_CANISTER))
                .define('I', Items.IRON_BLOCK)
                .define('N', item(ModIds.NIMBOSTRATUS_FLEECE))
                .pattern("NIN")
                .pattern("SCS")
                .pattern("NIN")
                .unlockedBy("has_storm_core", has(item(ModIds.STORM_CORE)))
                .save(recipeOutput, recipeKey(ModIds.HIGH_PRESSURE_CHAMBER));

        ShapedRecipeBuilder.shaped(BuiltInRegistries.ITEM, RecipeCategory.TRANSPORTATION, item(ModIds.CLOUD_JETPACK))
                .define('F', item(ModIds.BASIC_JETPACK_FRAME))
                .define('C', item(ModIds.CUMULUS_CLOUD_FRAGMENT))
                .define('L', Items.LEATHER)
                .pattern("CLC")
                .pattern("CFC")
                .pattern(" L ")
                .unlockedBy("has_basic_jetpack_frame", has(item(ModIds.BASIC_JETPACK_FRAME)))
                .save(recipeOutput, recipeKey(ModIds.CLOUD_JETPACK));

        ShapedRecipeBuilder.shaped(BuiltInRegistries.ITEM, RecipeCategory.TRANSPORTATION, item(ModIds.STABILIZED_CLOUD_JETPACK))
                .define('J', item(ModIds.CLOUD_JETPACK))
                .define('N', item(ModIds.STABILIZED_NOZZLE))
                .define('F', item(ModIds.CIRRUS_FILAMENT))
                .pattern(" F ")
                .pattern("NJN")
                .pattern(" F ")
                .unlockedBy("has_cloud_jetpack", has(item(ModIds.CLOUD_JETPACK)))
                .save(recipeOutput, recipeKey(ModIds.STABILIZED_CLOUD_JETPACK));

        ShapedRecipeBuilder.shaped(BuiltInRegistries.ITEM, RecipeCategory.TRANSPORTATION, item(ModIds.HIGH_PRESSURE_CLOUD_JETPACK))
                .define('J', item(ModIds.STABILIZED_CLOUD_JETPACK))
                .define('H', item(ModIds.HIGH_PRESSURE_CHAMBER))
                .define('S', item(ModIds.STORM_CORE))
                .pattern(" S ")
                .pattern("H J")
                .pattern(" S ")
                .unlockedBy("has_stabilized_cloud_jetpack", has(item(ModIds.STABILIZED_CLOUD_JETPACK)))
                .save(recipeOutput, recipeKey(ModIds.HIGH_PRESSURE_CLOUD_JETPACK));

        ShapedRecipeBuilder.shaped(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.GAS_STATE_CONVERTER))
                .define('I', Items.IRON_INGOT)
                .define('B', Items.GLASS_BOTTLE)
                .define('G', Items.GLASS_PANE)
                .define('J', Items.GOLD_INGOT)
                .define('W', item(ModIds.STRATUS_WISP))
                .pattern("IWI")
                .pattern("BGB")
                .pattern("IJI")
                .unlockedBy("has_stratus_wisp", has(item(ModIds.STRATUS_WISP)))
                .save(recipeOutput, recipeKey(ModIds.GAS_STATE_CONVERTER));

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return CloudCraft.MOD_ID + " recipe provider";
    }

    private static ItemLike item(String id) {
        return BuiltInRegistries.ITEM.getValue(ModIds.id(id));
    }

    private static Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(item);
    }

    private static ResourceKey<Recipe<?>> recipeKey(String path) {
        return ResourceKey.create(Registries.RECIPE, ModIds.id(path));
    }

    private RecipeOutput recipeOutput(CachedOutput output, List<CompletableFuture<?>> futures) {
        return (RecipeOutput) Proxy.newProxyInstance(
                RecipeOutput.class.getClassLoader(),
                new Class<?>[]{RecipeOutput.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "accept" -> {
                        ResourceKey<?> key = (ResourceKey<?>) args[0];
                        Recipe<?> recipe = (Recipe<?>) args[1];
                        futures.add(saveRecipe(output, key, recipe));
                        yield null;
                    }
                    case "advancement" -> Advancement.Builder.advancement();
                    case "includeRootAdvancement" -> null;
                    default -> throw new UnsupportedOperationException("Unsupported RecipeOutput method: " + method.getName());
                });
    }

    private CompletableFuture<?> saveRecipe(CachedOutput output, ResourceKey<?> key, Recipe<?> recipe) {
        return DataProvider.saveStable(output, Recipe.CODEC.encodeStart(JsonOps.INSTANCE, recipe).getOrThrow(), recipes.json(key.identifier()));
    }
}
