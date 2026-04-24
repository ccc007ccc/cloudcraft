package com.tr1c.cloudcraft.registry;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

public final class CloudCraftBrewingDefinitions {
    private CloudCraftBrewingDefinitions() {
    }

    public static BrewingMix solidCloud(Item ingredient, Holder<Potion> result) {
        return new BrewingMix(Potions.AWKWARD, ingredient, result);
    }

    public record BrewingMix(Holder<Potion> input, Item ingredient, Holder<Potion> result) {
    }
}
