package com.tr1c.cloudcraft.registry;

import java.util.List;

public final class CloudCraftRegistryDefinitions {
    private static final List<String> ITEM_IDS = CloudCraftItemDefinitions.ids();
    private static final List<String> BLOCK_IDS = CloudCraftBlockDefinitions.ids();
    private static final List<String> BLOCK_ITEM_IDS = BLOCK_IDS;
    private static final List<String> EFFECT_IDS = CloudCraftEffectDefinitions.ids();
    private static final List<String> POTION_IDS = CloudCraftPotionDefinitions.ids();

    private CloudCraftRegistryDefinitions() {
    }

    public static List<String> itemIds() {
        return ITEM_IDS;
    }

    public static List<String> blockIds() {
        return BLOCK_IDS;
    }

    public static List<String> blockItemIds() {
        return BLOCK_ITEM_IDS;
    }

    public static List<String> effectIds() {
        return EFFECT_IDS;
    }

    public static List<String> potionIds() {
        return POTION_IDS;
    }
}
