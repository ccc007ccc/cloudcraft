package com.tr1c.cloudcraft.cloudtech;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class BackTankContainer implements Container {
    private static final int SLOT = 0;
    private static final int SIZE = 1;

    private final Player player;

    public BackTankContainer(Player player) {
        this.player = player;
    }

    @Override
    public int getContainerSize() {
        return SIZE;
    }

    @Override
    public boolean isEmpty() {
        return BackTankService.get(player).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot == SLOT ? BackTankService.get(player) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot != SLOT || amount <= 0) {
            return ItemStack.EMPTY;
        }
        ItemStack current = BackTankService.get(player);
        if (current.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack removed = current.split(amount);
        BackTankService.set(player, current);
        return removed;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot != SLOT) {
            return ItemStack.EMPTY;
        }
        ItemStack removed = BackTankService.get(player);
        if (removed.isEmpty()) {
            return ItemStack.EMPTY;
        }
        BackTankService.clear(player);
        return removed;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot != SLOT) {
            return;
        }
        BackTankService.set(player, stack);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return this.player == player;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == SLOT && BackTankSlot.canEquip(stack);
    }

    @Override
    public void clearContent() {
        BackTankService.clear(player);
    }
}
