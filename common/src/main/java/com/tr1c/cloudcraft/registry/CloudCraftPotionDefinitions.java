package com.tr1c.cloudcraft.registry;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CloudCraftPotionDefinitions {
    private static final Map<String, PotionFactory> DEFINITIONS = definitions();

    private CloudCraftPotionDefinitions() {
    }

    public static List<String> ids() {
        return List.copyOf(DEFINITIONS.keySet());
    }

    public static Potion create(String id, Holder<MobEffect> effect) {
        PotionFactory factory = DEFINITIONS.get(id);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown potion definition: " + id);
        }
        return factory.create(effect);
    }

    public static Potion createSolidCloudPotion(Holder<MobEffect> effect) {
        return new Potion(ModIds.SOLID_CLOUD_POTION, new MobEffectInstance(effect, 3600, 0));
    }

    private static Map<String, PotionFactory> definitions() {
        Map<String, PotionFactory> definitions = new LinkedHashMap<>();
        definitions.put(ModIds.SOLID_CLOUD_POTION, CloudCraftPotionDefinitions::createSolidCloudPotion);
        return Collections.unmodifiableMap(definitions);
    }

    @FunctionalInterface
    private interface PotionFactory {
        Potion create(Holder<MobEffect> effect);
    }
}
