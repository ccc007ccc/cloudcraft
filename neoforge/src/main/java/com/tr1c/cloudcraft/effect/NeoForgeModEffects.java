package com.tr1c.cloudcraft.effect;

import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.registry.CloudCraftEffectDefinitions;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class NeoForgeModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, CloudCraft.MOD_ID);
    public static final Holder<MobEffect> CLOUD_WALKER = MOB_EFFECTS.register(ModIds.CLOUD_WALKER, () -> CloudCraftEffectDefinitions.create(ModIds.CLOUD_WALKER));

    private NeoForgeModEffects() {
    }

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
