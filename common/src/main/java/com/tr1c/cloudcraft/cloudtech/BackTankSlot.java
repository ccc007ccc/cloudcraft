package com.tr1c.cloudcraft.cloudtech;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class BackTankSlot {
    private BackTankSlot() {
    }

    public static boolean canEquip(ItemStack stack) {
        return CloudTechItems.isJetpack(stack);
    }

    public static ItemStack equip(Player player, ItemStack stack) {
        if (!canEquip(stack)) {
            return stack;
        }
        ItemStack current = BackTankService.get(player);
        if (!current.isEmpty()) {
            return stack;
        }
        ItemStack equipped = stack.copyWithCount(1);
        BackTankService.set(player, equipped);
        stack.shrink(1);
        return stack;
    }

}
