package com.tr1c.cloudcraft.item;

import com.tr1c.cloudcraft.CloudCraft;
import net.minecraft.client.color.item.Potion;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    // 创建一个延迟注册器，用于注册物品
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(CloudCraft.MOD_ID);

    // 注册云物品
    public static final DeferredItem<Item> CLOUD =
            ITEMS.register("cloud", () -> new Item(
                    new Item.Properties()
                            .useItemDescriptionPrefix()
                            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse(CloudCraft.MOD_ID + ":cloud")))
                            .food(new FoodProperties.Builder()
                                    .nutrition(4)
                                    .saturationModifier(0.5f)
                                    .alwaysEdible()
                                    .build()
                            )
                            .component( // 重点：添加Consumable组件
                                    DataComponents.CONSUMABLE,
                                    Consumable.builder()
                                            .onConsume(new ApplyStatusEffectsConsumeEffect(
                                                    new MobEffectInstance(MobEffects.LEVITATION, 200, 0), // 10秒漂浮I
                                                    1.0F // 概率100%
                                            ))
                                            .build()
                            )
            ));

    // 注册云碎片物品
    public static final DeferredItem<Item> CLOUD_FRAGMENT =
            ITEMS.register("cloud_fragment", () -> new Item(new Item.Properties().useItemDescriptionPrefix()
                    .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse(CloudCraft.MOD_ID + ":cloud_fragment")))
                    .stacksTo(16))); // 设置最大堆叠数量为16

    // 在事件总线上注册物品
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}