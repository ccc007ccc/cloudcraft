package com.tr1c.cloudcraft.cloudtech.client;

import com.tr1c.cloudcraft.cloudtech.CloudTechItems;
import com.tr1c.cloudcraft.cloudtech.JetpackStackState;
import com.tr1c.cloudcraft.progression.CloudProgressionRules;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public final class JetpackHudRenderer {
    private static final int WIDTH = 8;
    static final int HEIGHT = 40;
    private static final int PADDING = 8;
    private static final int BORDER_COLOR = 0xFF8C8C8C;
    private static final int BACKGROUND_COLOR = 0xC0101010;
    static final int FILL_COLOR = 0xFF8FE7FF;
    static final int LOW_FILL_COLOR = 0xFFFFB347;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    private JetpackHudRenderer() {
    }

    public static void render(GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.hideGui || minecraft.player == null || minecraft.gameMode == null) {
            return;
        }

        ItemStack jetpack = BackTankClientState.getBackTank();
        if (!CloudTechItems.isJetpack(jetpack)) {
            return;
        }

        String jetpackId = CloudTechItems.jetpackId(jetpack);
        int pressure = JetpackStackState.getPressure(jetpack);
        int maxPressure = CloudProgressionRules.maxPressure(jetpackId);
        int filled = fillHeight(pressure, maxPressure);
        int x = guiGraphics.guiWidth() - WIDTH - PADDING;
        int y = guiGraphics.guiHeight() - HEIGHT - PADDING - minecraft.font.lineHeight;
        int fillColor = fillColor(pressure, maxPressure);
        String text = pressureText(pressure, maxPressure);
        Font font = minecraft.font;

        guiGraphics.fill(x - 1, y - 1, x + WIDTH + 1, y + HEIGHT + 1, BORDER_COLOR);
        guiGraphics.fill(x, y, x + WIDTH, y + HEIGHT, BACKGROUND_COLOR);
        if (filled > 0) {
            guiGraphics.fill(x, y + HEIGHT - filled, x + WIDTH, y + HEIGHT, fillColor);
        }
        guiGraphics.drawString(font, text, x + WIDTH / 2 - font.width(text) / 2, y - font.lineHeight - 2, TEXT_COLOR, true);
    }

    static int fillHeight(int pressure, int maxPressure) {
        if (maxPressure <= 0) {
            return 0;
        }
        return HEIGHT * Math.max(0, pressure) / maxPressure;
    }

    static int fillColor(int pressure, int maxPressure) {
        return pressure * 4 <= maxPressure ? LOW_FILL_COLOR : FILL_COLOR;
    }

    static String pressureText(int pressure, int maxPressure) {
        return pressure + " / " + maxPressure;
    }
}
