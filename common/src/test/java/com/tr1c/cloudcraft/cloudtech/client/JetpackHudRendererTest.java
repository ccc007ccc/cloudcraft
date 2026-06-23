package com.tr1c.cloudcraft.cloudtech.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JetpackHudRendererTest {
    @Test
    void shouldScaleFillHeightAgainstConfiguredCapacity() {
        assertEquals(20, JetpackHudRenderer.fillHeight(600, 1200));
        assertEquals(20, JetpackHudRenderer.fillHeight(1200, 2400));
        assertEquals(0, JetpackHudRenderer.fillHeight(0, 2400));
    }

    @Test
    void shouldSwitchToLowFuelColorAtQuarterTank() {
        assertEquals(JetpackHudRenderer.FILL_COLOR, JetpackHudRenderer.fillColor(301, 1200));
        assertEquals(JetpackHudRenderer.LOW_FILL_COLOR, JetpackHudRenderer.fillColor(300, 1200));
    }

    @Test
    void shouldFormatPressureText() {
        assertEquals("450 / 1800", JetpackHudRenderer.pressureText(450, 1800));
    }
}
