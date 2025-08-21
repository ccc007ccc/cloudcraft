package com.tr1c.cloudcraft.item;

import com.tr1c.cloudcraft.CloudCraft;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

// 这个类用于定义和注册所有 Mod 中的盔甲材质
public class ModArmorMaterials {

    private static final Map<String, Holder<ArmorMaterial>> MATERIALS = new HashMap<>();

    public static Holder<ArmorMaterial> register(
            String name,
            EnumMap<ArmorType, Integer> typeProtection,
            int durability,
            int enchantability,
            Holder<SoundEvent> equipSoundEvent,
            TagKey<Item> ingredientTag,
            float toughness,
            float knockbackResistance
    ) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(CloudCraft.MOD_ID, name);
        ResourceKey<EquipmentAsset> assetId = ResourceKey.create(EquipmentAssets.ROOT_ID, location);

        ArmorMaterial material = new ArmorMaterial(
                durability,
                typeProtection,
                enchantability,
                equipSoundEvent,
                toughness,
                knockbackResistance,
                ingredientTag,
                assetId
        );

        // 直接保存到 Map 里，后续物品初始化使用
        Holder<ArmorMaterial> holder = Holder.direct(material);
        MATERIALS.put(name, holder);
        return holder;
    }

    public static Holder<ArmorMaterial> get(String name) {
        return MATERIALS.get(name);
    }
}

