package com.tr1c.cloudcraft.registry;

import com.tr1c.cloudcraft.effect.CloudWalkerEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class CloudCraftEffectDefinitions {
    private static final Map<String, Supplier<MobEffect>> DEFINITIONS = definitions();

    private CloudCraftEffectDefinitions() {
    }

    public static List<String> ids() {
        return List.copyOf(DEFINITIONS.keySet());
    }

    public static MobEffect create(String id) {
        Supplier<MobEffect> factory = DEFINITIONS.get(id);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown effect definition: " + id);
        }
        return factory.get();
    }

    public static MobEffect createCloudWalkerEffect() {
        return new CloudWalkerEffect(MobEffectCategory.NEUTRAL, 0x79b5bd)
                .addAttributeModifier(
                        Attributes.MOVEMENT_SPEED,
                        ModIds.id(ModIds.CLOUD_WALKER),
                        0.25f,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    private static Map<String, Supplier<MobEffect>> definitions() {
        Map<String, Supplier<MobEffect>> definitions = new LinkedHashMap<>();
        definitions.put(ModIds.CLOUD_WALKER, CloudCraftEffectDefinitions::createCloudWalkerEffect);
        return Collections.unmodifiableMap(definitions);
    }
}
