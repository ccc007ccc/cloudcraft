package com.tr1c.cloudcraft.potion;

import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.effect.NeoForgeModEffects;
import com.tr1c.cloudcraft.registry.CloudCraftPotionDefinitions;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class NeoForgeModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, CloudCraft.MOD_ID);

    public static final Holder<Potion> SOLID_CLOUD = POTIONS.register(ModIds.SOLID_CLOUD_POTION, () ->
            CloudCraftPotionDefinitions.create(ModIds.SOLID_CLOUD_POTION, NeoForgeModEffects.CLOUD_WALKER));

    private NeoForgeModPotions() {
    }

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
