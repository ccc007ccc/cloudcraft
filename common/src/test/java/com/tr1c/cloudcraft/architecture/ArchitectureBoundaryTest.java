package com.tr1c.cloudcraft.architecture;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

class ArchitectureBoundaryTest {
    private static final Path ROOT = Path.of("..").toAbsolutePath().normalize();
    private static final List<String> LOADER_API_MARKERS = List.of(
            "net.fabricmc.",
            "net.neoforged.",
            "net.minecraftforge.");

    @Test
    void commonMainSourcesShouldStayLoaderAgnostic() throws IOException {
        for (Path source : javaFiles(ROOT.resolve("common/src/main/java"))) {
            String text = Files.readString(source);
            for (String marker : LOADER_API_MARKERS) {
                assertFalse(text.contains(marker), source + " must not import or reference " + marker);
            }
        }
    }

    @Test
    void platformModulesShouldNotDefineGameplayRuleClasses() throws IOException {
        for (Path sourceRoot : List.of(ROOT.resolve("fabric/src/main/java"), ROOT.resolve("neoforge/src/main/java"))) {
            for (Path source : javaFiles(sourceRoot)) {
                assertFalse(source.getFileName().toString().endsWith("Rules.java"), source + " must keep gameplay rules in common");
            }
        }
    }

    private static List<Path> javaFiles(Path root) throws IOException {
        if (Files.notExists(root)) {
            return List.of();
        }
        try (Stream<Path> files = Files.walk(root)) {
            return files
                    .filter(path -> path.toString().endsWith(".java"))
                    .toList();
        }
    }
}
