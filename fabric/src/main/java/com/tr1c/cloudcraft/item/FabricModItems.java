package com.tr1c.cloudcraft.item;

import com.tr1c.cloudcraft.registry.CloudCraftItemDefinitions;
import com.tr1c.cloudcraft.registry.CloudCraftRegistryDefinitions;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.LinkedHashMap;
import java.util.Map;

public final class FabricModItems {
    private static final Map<String, Item> ITEMS = registerItems();

    public static final Item CLOUD = ITEMS.get(ModIds.CLOUD);
    public static final Item CUMULUS_CLOUD_FRAGMENT = ITEMS.get(ModIds.CUMULUS_CLOUD_FRAGMENT);

    private FabricModItems() {
    }

    private static Map<String, Item> registerItems() {
        Map<String, Item> items = new LinkedHashMap<>();
        for (String id : CloudCraftRegistryDefinitions.itemIds()) {
            items.put(id, register(id));
        }
        return Map.copyOf(items);
    }

    private static Item register(String name) {
        ResourceKey<Item> itemKey = ModIds.itemKey(name);
        return Registry.register(BuiltInRegistries.ITEM, itemKey, CloudCraftItemDefinitions.create(name, new Item.Properties().setId(itemKey)));
    }

    public static <T extends Item> T register(T item, ResourceKey<Item> itemKey) {
        return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
    }

    public static Item itemById(String id) {
        return ITEMS.get(id);
    }
}
