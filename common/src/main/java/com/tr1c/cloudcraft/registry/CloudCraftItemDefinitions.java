package com.tr1c.cloudcraft.registry;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CloudCraftItemDefinitions {
    private static final Map<String, ItemFactory> DEFINITIONS = definitions();

    private CloudCraftItemDefinitions() {
    }

    public static List<String> ids() {
        return List.copyOf(DEFINITIONS.keySet());
    }

    public static Item create(String id, Item.Properties properties) {
        ItemFactory factory = DEFINITIONS.get(id);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown item definition: " + id);
        }
        return factory.create(properties);
    }

    public static Item createCloud(Item.Properties properties) {
        return new Item(properties.useItemDescriptionPrefix()
                .food(new FoodProperties.Builder()
                        .nutrition(4)
                        .saturationModifier(0.5f)
                        .alwaysEdible()
                        .build())
                .component(DataComponents.CONSUMABLE, Consumable.builder()
                        .onConsume(new ApplyStatusEffectsConsumeEffect(
                                new MobEffectInstance(MobEffects.LEVITATION, 200, 0),
                                1.0F))
                        .build()));
    }

    public static Item createCumulusCloudFragment(Item.Properties properties) {
        return new Item(properties.useItemDescriptionPrefix().stacksTo(16));
    }

    private static Map<String, ItemFactory> definitions() {
        Map<String, ItemFactory> definitions = new LinkedHashMap<>();
        definitions.put(ModIds.CLOUD, CloudCraftItemDefinitions::createCloud);
        definitions.put(ModIds.CUMULUS_CLOUD_FRAGMENT, CloudCraftItemDefinitions::createCumulusCloudFragment);
        return Collections.unmodifiableMap(definitions);
    }

    @FunctionalInterface
    private interface ItemFactory {
        Item create(Item.Properties properties);
    }
}
