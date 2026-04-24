package com.tr1c.cloudcraft.potion;

import com.tr1c.cloudcraft.effect.FabricModEffects;
import com.tr1c.cloudcraft.registry.CloudCraftPotionDefinitions;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.alchemy.Potion;

public final class FabricModPotions {
    private static final Potion SOLID_CLOUD_VALUE = Registry.register(
            BuiltInRegistries.POTION,
            ModIds.id(ModIds.SOLID_CLOUD_POTION),
            CloudCraftPotionDefinitions.create(ModIds.SOLID_CLOUD_POTION, FabricModEffects.CLOUD_WALKER));

    public static final Holder<Potion> SOLID_CLOUD = BuiltInRegistries.POTION.wrapAsHolder(SOLID_CLOUD_VALUE);

    private FabricModPotions() {
    }
}
