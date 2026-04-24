package com.tr1c.cloudcraft.item;

import com.tr1c.cloudcraft.registry.CloudCraftItemDefinitions;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class FabricModItems {
    public static final Item CLOUD = register(ModIds.CLOUD);
    public static final Item CUMULUS_CLOUD_FRAGMENT = register(ModIds.CUMULUS_CLOUD_FRAGMENT);

    private FabricModItems() {
    }

    private static Item register(String name) {
        ResourceKey<Item> itemKey = ModIds.itemKey(name);
        return Registry.register(BuiltInRegistries.ITEM, itemKey, CloudCraftItemDefinitions.create(name, new Item.Properties().setId(itemKey)));
    }

    public static <T extends Item> T register(T item, ResourceKey<Item> itemKey) {
        return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
    }
}
