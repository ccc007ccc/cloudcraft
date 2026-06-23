package com.tr1c.cloudcraft.cloudtech;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class BackTankMenuSlot extends Slot {
    private boolean open;

    public BackTankMenuSlot(BackTankContainer container, int x, int y) {
        super(container, 0, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return BackTankSlot.canEquip(stack);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean isActive() {
        return open;
    }

    @Override
    public boolean mayPickup(Player player) {
        return super.mayPickup(player);
    }

    @Override
    public boolean isHighlightable() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isOpen() {
        return open;
    }
}
