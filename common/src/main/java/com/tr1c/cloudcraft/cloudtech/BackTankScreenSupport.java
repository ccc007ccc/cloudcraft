package com.tr1c.cloudcraft.cloudtech;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

public final class BackTankScreenSupport {
    public static final int INVENTORY_ARMOR_AREA_X = 7;
    public static final int INVENTORY_ARMOR_AREA_Y = 7;
    public static final int INVENTORY_ARMOR_AREA_WIDTH = 18;
    public static final int INVENTORY_ARMOR_AREA_HEIGHT = 72;
    public static final int SLOT_OFFSET = 18;
    public static final int INVENTORY_SLOT_X = INVENTORY_ARMOR_AREA_X - SLOT_OFFSET;
    public static final int INVENTORY_SLOT_Y = 25;
    public static final int HOVER_SIZE = BackTankSlotUi.FRAME_SIZE;

    private BackTankScreenSupport() {
    }

    public static Slot findVisibleBackTankSlot(AbstractContainerMenu menu) {
        for (Slot slot : menu.slots) {
            if (slot.container instanceof BackTankContainer) {
                return slot;
            }
        }
        return null;
    }

    public static Slot findBackTankAnchorSlot(AbstractContainerMenu menu) {
        Slot anchor = null;
        for (Slot slot : menu.slots) {
            if (!isArmorSlot(slot)) {
                continue;
            }
            if (anchor == null || slot.x < anchor.x || slot.x == anchor.x && slot.y > anchor.y) {
                anchor = slot;
            }
        }
        return anchor;
    }

    public static boolean isArmorSlot(Slot slot) {
        int containerSlot = slot.getContainerSlot();
        return !(slot.container instanceof BackTankContainer) && containerSlot >= 5 && containerSlot < 9;
    }

    public static boolean isInventoryAreaHovered(int leftPos, int topPos, double mouseX, double mouseY) {
        double relativeMouseX = mouseX - leftPos;
        double relativeMouseY = mouseY - topPos;
        return isHovered(
                INVENTORY_ARMOR_AREA_X,
                INVENTORY_ARMOR_AREA_Y,
                INVENTORY_ARMOR_AREA_WIDTH,
                INVENTORY_ARMOR_AREA_HEIGHT,
                relativeMouseX,
                relativeMouseY)
                || isBackTankHovered(leftPos, topPos, INVENTORY_SLOT_X, INVENTORY_SLOT_Y, mouseX, mouseY);
    }

    public static boolean isCreativeAreaHovered(AbstractContainerMenu menu, int leftPos, int topPos, Slot visibleBackTankSlot, double mouseX, double mouseY) {
        double relativeMouseX = mouseX - leftPos;
        double relativeMouseY = mouseY - topPos;
        for (Slot slot : menu.slots) {
            if (isArmorSlot(slot) && isHovered(slot.x, slot.y, BackTankSlotUi.SLOT_SIZE, BackTankSlotUi.SLOT_SIZE, relativeMouseX, relativeMouseY)) {
                return true;
            }
        }
        return visibleBackTankSlot != null && isBackTankHovered(leftPos, topPos, visibleBackTankSlot.x, visibleBackTankSlot.y, mouseX, mouseY);
    }

    public static boolean isBackTankHovered(int leftPos, int topPos, int slotX, int slotY, double mouseX, double mouseY) {
        return isHovered(leftPos + slotX - 1, topPos + slotY - 1, HOVER_SIZE, HOVER_SIZE, mouseX, mouseY);
    }

    public static int expandedX(Slot anchorSlot) {
        return anchorSlot.x - SLOT_OFFSET;
    }

    public static int expandedY(Slot anchorSlot) {
        return anchorSlot.y;
    }

    private static boolean isHovered(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
