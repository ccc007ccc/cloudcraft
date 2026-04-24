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

        ShapedRecipeBuilder.shaped(BuiltInRegistries.ITEM, RecipeCategory.MISC, item(ModIds.GAS_STATE_CONVERTER))
                .define('I', Items.IRON_INGOT)
                .define('B', Items.GLASS_BOTTLE)
                .define('G', Items.GLASS_PANE)
                .define('J', Items.GOLD_INGOT)
                .pattern("III")
                .pattern("BGB")
                .pattern("IJI")
                .unlockedBy("has_glass_bottle", has(Items.GLASS_BOTTLE))
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
