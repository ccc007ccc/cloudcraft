package com.tr1c.cloudcraft.compat;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompatibilityBoundaryTest {
    private static final Path ROOT = Path.of("..").toAbsolutePath().normalize();
    private static final Path COMPATIBILITY_STRATEGY = ROOT.resolve("docs/COMPATIBILITY_STRATEGY.md");
    private static final List<String> THIRD_PARTY_API_MARKERS = List.of(
            "mezz.jei.",
            "me.shedaniel.rei.",
            "dev.emi.",
            "teamreborn.energy.",
            "net.fabricmc.fabric.api.transfer.v1",
            "net.neoforged.neoforge.energy.",
            "ForgeCapabilities.ENERGY");
    private static final List<String> THIRD_PARTY_GRADLE_DEPENDENCIES = List.of(
            "mezz.jei:",
            "me.shedaniel.rei:",
            "dev.emi:",
            "teamreborn:energy",
            "curse.maven:jade",
            "mcjty.theoneprobe");

    @Test
    void shouldDocumentRecipeViewerAndTechModCompatibilityStrategy() throws IOException {
        String strategy = Files.readString(COMPATIBILITY_STRATEGY);

        assertTrue(strategy.contains("JEI"));
        assertTrue(strategy.contains("REI"));
        assertTrue(strategy.contains("EMI"));
        assertTrue(strategy.contains("gas_state_converter"));
        assertTrue(strategy.contains("compressed air"));
        assertTrue(strategy.contains("optional integration"));
    }

    @Test
    void shouldKeepThirdPartyIntegrationsOutOfCoreSourcesUntilImplemented() throws IOException {
        for (Path sourceRoot : sourceRoots()) {
            if (Files.notExists(sourceRoot)) {
                continue;
            }
            try (Stream<Path> files = Files.walk(sourceRoot)) {
                for (Path source : files.filter(path -> path.toString().endsWith(".java")).toList()) {
                    String text = Files.readString(source);
                    for (String marker : THIRD_PARTY_API_MARKERS) {
                        assertFalse(text.contains(marker), source + " must not directly depend on " + marker);
                    }
                }
            }
        }
    }

    @Test
    void shouldNotDeclareRecipeViewerOrEnergyApiAsRuntimeDependenciesYet() throws IOException {
        for (Path buildFile : buildFiles()) {
            String text = Files.readString(buildFile);
            for (String dependency : THIRD_PARTY_GRADLE_DEPENDENCIES) {
                assertFalse(text.contains(dependency), buildFile + " must keep " + dependency + " optional for now");
            }
        }
    }

    private static List<Path> sourceRoots() {
        return List.of(
                ROOT.resolve("common/src/main/java"),
                ROOT.resolve("fabric/src/main/java"),
                ROOT.resolve("neoforge/src/main/java"));
    }

    private static List<Path> buildFiles() {
        return List.of(
                ROOT.resolve("build.gradle"),
                ROOT.resolve("common/build.gradle"),
                ROOT.resolve("fabric/build.gradle"),
                ROOT.resolve("neoforge/build.gradle"));
    }
}
