package com.tr1c.cloudcraft.effect;

import com.tr1c.cloudcraft.CloudCraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.print.attribute.Attribute;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, CloudCraft.MOD_ID);

    public static final Holder<MobEffect> CLOUD_WALKER =
            MOB_EFFECTS.register("cloud_walker",
            () -> new CloudWalkerEffect(MobEffectCategory.NEUTRAL, 0x79b5bd)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED,
            ResourceLocation.fromNamespaceAndPath(CloudCraft.MOD_ID, "cloud_walker"),
            0.25f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            );

    public  static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
