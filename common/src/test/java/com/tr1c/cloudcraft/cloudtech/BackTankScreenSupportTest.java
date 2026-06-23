package com.tr1c.cloudcraft.cloudtech;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BackTankScreenSupportTest {
    @Test
    void shouldTreatInventoryHoverUsingScreenCoordinates() {
        int leftPos = 100;
        int topPos = 50;

        assertTrue(BackTankScreenSupport.isInventoryAreaHovered(leftPos, topPos, 108, 58));
        assertFalse(BackTankScreenSupport.isInventoryAreaHovered(leftPos, topPos, 8, 8));
    }

    @Test
    void shouldTreatBackTankHoverUsingScreenCoordinates() {
        int leftPos = 100;
        int topPos = 50;

        assertTrue(BackTankScreenSupport.isBackTankHovered(leftPos, topPos, BackTankScreenSupport.INVENTORY_SLOT_X, BackTankScreenSupport.INVENTORY_SLOT_Y, 89, 74));
        assertFalse(BackTankScreenSupport.isBackTankHovered(leftPos, topPos, BackTankScreenSupport.INVENTORY_SLOT_X, BackTankScreenSupport.INVENTORY_SLOT_Y, -11, 25));
    }
}
