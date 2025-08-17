package com.tr1c.cloudcraft.item;

import com.tr1c.cloudcraft.CloudCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(CloudCraft.MOD_ID);

    public static final DeferredItem<Item> CLOUD =
            ITEMS.register("cloud", () -> new Item(new Item.Properties().useItemDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse(CloudCraft.MOD_ID + ":cloud")))));

    public static final DeferredItem<Item> CLOUD_FRAGMENT =
            ITEMS.register("cloud_fragment", () -> new Item(new Item.Properties().useItemDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse(CloudCraft.MOD_ID + ":cloud_fragment"))).stacksTo(16)));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
