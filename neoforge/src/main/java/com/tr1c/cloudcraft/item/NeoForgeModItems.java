package com.tr1c.cloudcraft.item;

import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.registry.CloudCraftItemDefinitions;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class NeoForgeModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CloudCraft.MOD_ID);

    public static final DeferredItem<Item> CLOUD = register(ModIds.CLOUD);
    public static final DeferredItem<Item> CUMULUS_CLOUD_FRAGMENT = register(ModIds.CUMULUS_CLOUD_FRAGMENT);

    private NeoForgeModItems() {
    }

    private static DeferredItem<Item> register(String name) {
        return ITEMS.register(name, () -> CloudCraftItemDefinitions.create(name, new Item.Properties().setId(ModIds.itemKey(name))));
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
