package com.tr1c.cloudcraft.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class CloudCraftAdvancementProvider implements DataProvider {
    private static final List<AdvancementDefinition> ADVANCEMENTS = List.of(
            root(ModIds.CLOUD),
            task("cloud_fragment", "root", ModIds.CUMULUS_CLOUD_FRAGMENT, ModIds.CUMULUS_CLOUD_FRAGMENT),
            task(ModIds.COMPRESSED_CANISTER, "cloud_fragment", ModIds.COMPRESSED_CANISTER, ModIds.COMPRESSED_CANISTER),
            task(ModIds.CIRRUS_FILAMENT, "cloud_fragment", ModIds.CIRRUS_FILAMENT, ModIds.CIRRUS_FILAMENT),
            task(ModIds.STORM_CORE, "cloud_fragment", ModIds.STORM_CORE, ModIds.STORM_CORE),
            task(ModIds.BASIC_JETPACK_FRAME, ModIds.COMPRESSED_CANISTER, ModIds.BASIC_JETPACK_FRAME, ModIds.BASIC_JETPACK_FRAME),
            goal(ModIds.CLOUD_JETPACK, ModIds.BASIC_JETPACK_FRAME, ModIds.CLOUD_JETPACK, ModIds.CLOUD_JETPACK),
            goal(ModIds.STABILIZED_CLOUD_JETPACK, ModIds.CLOUD_JETPACK, ModIds.STABILIZED_CLOUD_JETPACK, ModIds.STABILIZED_CLOUD_JETPACK),
            challenge(ModIds.HIGH_PRESSURE_CLOUD_JETPACK, ModIds.STABILIZED_CLOUD_JETPACK, ModIds.HIGH_PRESSURE_CLOUD_JETPACK, ModIds.HIGH_PRESSURE_CLOUD_JETPACK),
            task(ModIds.GAS_STATE_CONVERTER, ModIds.COMPRESSED_CANISTER, ModIds.GAS_STATE_CONVERTER, ModIds.GAS_STATE_CONVERTER),
            dimensionGoal("enter_cloud_dimension", ModIds.GAS_STATE_CONVERTER, ModIds.GAS_STATE_CONVERTER, ModIds.CLOUD_DIMENSION)
    );

    private final PackOutput.PathProvider advancements;

    public CloudCraftAdvancementProvider(PackOutput output) {
        this.advancements = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancement");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (AdvancementDefinition advancement : ADVANCEMENTS) {
            futures.add(DataProvider.saveStable(output, advancementJson(advancement), advancements.json(ModIds.id(advancement.id()))));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return CloudCraft.MOD_ID + " advancement provider";
    }

    private static AdvancementDefinition root(String criterionItem) {
        return new AdvancementDefinition("root", null, ModIds.CLOUD, criterionItem, CriterionType.ITEM, "task", true, false);
    }

    private static AdvancementDefinition task(String id, String parent, String iconItem, String criterionItem) {
        return new AdvancementDefinition(id, parent, iconItem, criterionItem, CriterionType.ITEM, "task", false, false);
    }

    private static AdvancementDefinition goal(String id, String parent, String iconItem, String criterionItem) {
        return new AdvancementDefinition(id, parent, iconItem, criterionItem, CriterionType.ITEM, "goal", false, false);
    }

    private static AdvancementDefinition challenge(String id, String parent, String iconItem, String criterionItem) {
        return new AdvancementDefinition(id, parent, iconItem, criterionItem, CriterionType.ITEM, "challenge", false, true);
    }

    private static AdvancementDefinition dimensionGoal(String id, String parent, String iconItem, String dimensionId) {
        return new AdvancementDefinition(id, parent, iconItem, dimensionId, CriterionType.DIMENSION, "goal", false, false);
    }

    private static JsonElement advancementJson(AdvancementDefinition definition) {
        JsonObject root = new JsonObject();
        if (definition.parent() != null) {
            root.addProperty("parent", CloudCraft.MOD_ID + ":" + definition.parent());
        }
        root.add("display", display(definition));
        root.add("criteria", criteria(definition));
        root.add("requirements", requirements(criterionName(definition)));
        return root;
    }

    private static JsonObject display(AdvancementDefinition definition) {
        JsonObject display = new JsonObject();
        display.add("icon", icon(definition.iconItem()));
        display.add("title", translation("advancements." + CloudCraft.MOD_ID + "." + definition.id() + ".title"));
        display.add("description", translation("advancements." + CloudCraft.MOD_ID + "." + definition.id() + ".description"));
        display.addProperty("frame", definition.frame());
        display.addProperty("show_toast", !definition.root());
        display.addProperty("announce_to_chat", definition.announce());
        display.addProperty("hidden", false);
        if (definition.root()) {
            display.addProperty("background", "minecraft:textures/gui/advancements/backgrounds/adventure.png");
        }
        return display;
    }

    private static JsonObject icon(String itemId) {
        JsonObject icon = new JsonObject();
        icon.addProperty("id", CloudCraft.MOD_ID + ":" + itemId);
        return icon;
    }

    private static JsonObject translation(String key) {
        JsonObject text = new JsonObject();
        text.addProperty("translate", key);
        return text;
    }

    private static JsonObject criteria(AdvancementDefinition definition) {
        JsonObject criteria = new JsonObject();
        JsonObject criterion = new JsonObject();
        criterion.addProperty("trigger", definition.criterionType().trigger());
        criterion.add("conditions", criterionConditions(definition));
        criteria.add(criterionName(definition), criterion);
        return criteria;
    }

    private static JsonObject criterionConditions(AdvancementDefinition definition) {
        JsonObject conditions = new JsonObject();
        if (definition.criterionType() == CriterionType.DIMENSION) {
            conditions.addProperty("to", CloudCraft.MOD_ID + ":" + definition.criterionValue());
            return conditions;
        }

        JsonArray items = new JsonArray();
        JsonObject item = new JsonObject();
        item.addProperty("items", CloudCraft.MOD_ID + ":" + definition.criterionValue());
        items.add(item);
        conditions.add("items", items);
        return conditions;
    }

    private static String criterionName(AdvancementDefinition definition) {
        return definition.criterionType() == CriterionType.DIMENSION ? "entered_dimension" : "has_item";
    }

    private static JsonArray requirements(String criterion) {
        JsonArray requirements = new JsonArray();
        JsonArray group = new JsonArray();
        group.add(criterion);
        requirements.add(group);
        return requirements;
    }

    private enum CriterionType {
        ITEM("minecraft:inventory_changed"),
        DIMENSION("minecraft:changed_dimension");

        private final String trigger;

        CriterionType(String trigger) {
            this.trigger = trigger;
        }

        private String trigger() {
            return trigger;
        }
    }

    private record AdvancementDefinition(
            String id,
            String parent,
            String iconItem,
            String criterionValue,
            CriterionType criterionType,
            String frame,
            boolean root,
            boolean announce) {
    }
}
