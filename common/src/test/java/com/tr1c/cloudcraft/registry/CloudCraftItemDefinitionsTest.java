package com.tr1c.cloudcraft.registry;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloudCraftItemDefinitionsTest {
    private static final Path SOURCE = Path.of("src/main/java/com/tr1c/cloudcraft/registry/CloudCraftItemDefinitions.java");

    @Test
    void shouldKeepCloudAsGentleAlwaysEdibleFood() throws IOException {
        String source = Files.readString(SOURCE);

        assertTrue(source.contains(".nutrition(4)"));
        assertTrue(source.contains(".saturationModifier(0.5f)"));
        assertTrue(source.contains(".alwaysEdible()"));
        assertTrue(source.contains("new MobEffectInstance(MobEffects.SLOW_FALLING, 160, 0)"));
        assertTrue(source.contains("1.0F"));
        assertFalse(source.contains("MobEffects.LEVITATION"));
    }
}
