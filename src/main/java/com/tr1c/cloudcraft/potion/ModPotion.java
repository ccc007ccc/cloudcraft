package com.tr1c.cloudcraft.potion;

import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.effect.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPotion {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, CloudCraft.MOD_ID);

    // 注册凝云药水物品
    public static final Holder<Potion> POTION_BOTTLE_SPLASH_SOLID_CLOUD =
            POTIONS.register("potion_bottle_splash_solid_cloud",
            () -> new Potion("potion_bottle_splash_solid_cloud",
            new MobEffectInstance(ModEffects.CLOUD_WALKER, 3600, 0)));
    public static void register(IEventBus eventBus)
    {
        POTIONS.register(eventBus);
    }
}
