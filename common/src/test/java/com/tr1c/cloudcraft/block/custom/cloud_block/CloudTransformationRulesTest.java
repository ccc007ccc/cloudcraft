package com.tr1c.cloudcraft.block.custom.cloud_block;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloudTransformationRulesTest {
    @Test
    void shouldMatchExpectedEffectOffsets() {
        List<CloudTransformationRules.Offset> offsets = CloudTransformationRules.effectOffsets();

        assertTrue(offsets.contains(new CloudTransformationRules.Offset(0, -1, 0)));
        assertTrue(offsets.contains(new CloudTransformationRules.Offset(1, -1, 0)));
        assertTrue(offsets.contains(new CloudTransformationRules.Offset(-1, -1, 0)));
        assertTrue(offsets.contains(new CloudTransformationRules.Offset(0, -1, 1)));
        assertTrue(offsets.contains(new CloudTransformationRules.Offset(0, -1, -1)));
        assertFalse(offsets.contains(new CloudTransformationRules.Offset(1, -1, 1)));
    }

    @Test
    void shouldRespectRadiusBoundaryForOffsets() {
        List<CloudTransformationRules.Offset> offsets = CloudTransformationRules.radiusOffsets(CloudTransformationRules.SPLASH_POTION_RADIUS);

        assertTrue(offsets.contains(new CloudTransformationRules.Offset(1, 0, 0)));
        assertFalse(offsets.contains(new CloudTransformationRules.Offset(2, 0, 0)));
        assertFalse(offsets.contains(new CloudTransformationRules.Offset(1, 1, 1)));
    }

    @Test
    void shouldExposeConverterRadiusForMachineConversion() {
        List<CloudTransformationRules.Offset> offsets = CloudTransformationRules.radiusOffsets(CloudTransformationRules.CONVERTER_RADIUS);

        assertTrue(offsets.contains(new CloudTransformationRules.Offset(2, 0, 0)));
        assertTrue(offsets.contains(new CloudTransformationRules.Offset(0, 2, 0)));
        assertFalse(offsets.contains(new CloudTransformationRules.Offset(3, 0, 0)));
        assertFalse(offsets.contains(new CloudTransformationRules.Offset(2, 2, 1)));
    }

    @Test
    void shouldReuseCachedOffsetsForKnownPotionRadii() {
        List<CloudTransformationRules.Offset> splashOffsets = CloudTransformationRules.radiusOffsets(CloudTransformationRules.SPLASH_POTION_RADIUS);
        List<CloudTransformationRules.Offset> splashOffsetsAgain = CloudTransformationRules.radiusOffsets(CloudTransformationRules.SPLASH_POTION_RADIUS);
        List<CloudTransformationRules.Offset> lingeringOffsets = CloudTransformationRules.radiusOffsets(CloudTransformationRules.LINGERING_POTION_RADIUS);
        List<CloudTransformationRules.Offset> lingeringOffsetsAgain = CloudTransformationRules.radiusOffsets(CloudTransformationRules.LINGERING_POTION_RADIUS);
        List<CloudTransformationRules.Offset> converterOffsets = CloudTransformationRules.radiusOffsets(CloudTransformationRules.CONVERTER_RADIUS);
        List<CloudTransformationRules.Offset> converterOffsetsAgain = CloudTransformationRules.radiusOffsets(CloudTransformationRules.CONVERTER_RADIUS);

        assertSame(splashOffsets, splashOffsetsAgain);
        assertSame(lingeringOffsets, lingeringOffsetsAgain);
        assertSame(converterOffsets, converterOffsetsAgain);
    }

    @Test
    void shouldExposeCrossOffsetsForCloudWalkerCheck() {
        List<CloudTransformationRules.Offset> offsets = CloudTransformationRules.crossOffsets();

        assertTrue(offsets.contains(new CloudTransformationRules.Offset(0, -1, 0)));
        assertTrue(offsets.contains(new CloudTransformationRules.Offset(1, 0, 0)));
        assertTrue(offsets.contains(new CloudTransformationRules.Offset(-1, 0, 0)));
        assertTrue(offsets.contains(new CloudTransformationRules.Offset(0, 0, 1)));
        assertTrue(offsets.contains(new CloudTransformationRules.Offset(0, 0, -1)));
        assertFalse(offsets.contains(new CloudTransformationRules.Offset(0, 1, 0)));
    }
}
