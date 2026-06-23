package com.tr1c.cloudcraft.cloudtech.client;

import net.minecraft.world.item.ItemStack;

public final class BackTankClientState {
    private static ItemStack backTank = ItemStack.EMPTY;

    private BackTankClientState() {
    }

    public static ItemStack getBackTank() {
        return backTank.copy();
    }

    public static void setBackTank(ItemStack stack) {
        backTank = stack.copy();
    }

    public static void clear() {
        backTank = ItemStack.EMPTY;
    }
}
