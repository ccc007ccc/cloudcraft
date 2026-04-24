package com.tr1c.cloudcraft.effect;

import com.tr1c.cloudcraft.registry.CloudCraftEffectDefinitions;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public final class FabricModEffects {
    private static final MobEffect CLOUD_WALKER_VALUE = Registry.register(
            BuiltInRegistries.MOB_EFFECT,
            ModIds.id(ModIds.CLOUD_WALKER),
            CloudCraftEffectDefinitions.create(ModIds.CLOUD_WALKER));

    public static final Holder<MobEffect> CLOUD_WALKER = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(CLOUD_WALKER_VALUE);

    private FabricModEffects() {
    }
}
