package com.tr1c.cloudcraft.item;

import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.registry.CloudCraftItemDefinitions;
import com.tr1c.cloudcraft.registry.CloudCraftRegistryDefinitions;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashMap;
import java.util.Map;

public final class NeoForgeModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CloudCraft.MOD_ID);
    private static final Map<String, DeferredItem<Item>> REGISTERED_ITEMS = registerItems();

    public static final DeferredItem<Item> CLOUD = REGISTERED_ITEMS.get(ModIds.CLOUD);
    public static final DeferredItem<Item> CUMULUS_CLOUD_FRAGMENT = REGISTERED_ITEMS.get(ModIds.CUMULUS_CLOUD_FRAGMENT);

    private NeoForgeModItems() {
    }

    private static Map<String, DeferredItem<Item>> registerItems() {
        Map<String, DeferredItem<Item>> items = new LinkedHashMap<>();
        for (String id : CloudCraftRegistryDefinitions.itemIds()) {
            items.put(id, register(id));
        }
        return Map.copyOf(items);
    }

    private static DeferredItem<Item> register(String name) {
        return ITEMS.register(name, () -> CloudCraftItemDefinitions.create(name, new Item.Properties().setId(ModIds.itemKey(name))));
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

    public static Item itemById(String id) {
        return REGISTERED_ITEMS.get(id).get();
    }
}
