package com.tr1c.cloudcraft.cloudtech;

import com.tr1c.cloudcraft.progression.CloudProgressionRules;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class CloudTechItems {
    private static final Identifier CLOUD_JETPACK = ModIds.id(ModIds.CLOUD_JETPACK);

    private CloudTechItems() {
    }

    public static Identifier jetpackId() {
        return CLOUD_JETPACK;
    }

    public static boolean isJetpack(Item item) {
        return CloudProgressionRules.isJetpackId(item.builtInRegistryHolder().key().identifier().getPath());
    }

    public static boolean isJetpack(ItemStack stack) {
        return !stack.isEmpty() && isJetpack(stack.getItem());
    }

    public static String jetpackId(ItemStack stack) {
        return stack.getItem().builtInRegistryHolder().key().identifier().getPath();
    }

    public static Item jetpackItem() {
        return BuiltInRegistries.ITEM.getValue(CLOUD_JETPACK);
    }
}
