package com.tr1c.cloudcraft.cloudtech;

import net.minecraft.client.gui.GuiGraphics;

public final class BackTankSlotUi {
    public static final int SLOT_SIZE = 16;
    public static final int FRAME_SIZE = 18;

    private BackTankSlotUi() {
    }

    public static void render(GuiGraphics guiGraphics, int leftPos, int topPos, int slotX, int slotY) {
        int x = leftPos + slotX - 1;
        int y = topPos + slotY - 1;
        guiGraphics.fill(x, y, x + FRAME_SIZE, y + FRAME_SIZE, -6250336);
        guiGraphics.fill(x + 1, y + 1, x + FRAME_SIZE - 1, y + FRAME_SIZE - 1, -16777216);
        guiGraphics.fill(x + 2, y + 2, x + FRAME_SIZE - 2, y + FRAME_SIZE - 2, -4144960);
    }
}
