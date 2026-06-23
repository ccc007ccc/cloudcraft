package com.tr1c.cloudcraft.visual;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloudTextureAssetTest {
    private static final Path ROOT = Path.of("..").toAbsolutePath().normalize();
    private static final Path MAIN_TEXTURES = Path.of("src/main/resources/assets/cloudcraft/textures");
    private static final Path TEXTURE_PROJECT = ROOT.resolve("design/texture_project");
    private static final List<String> CLOUD_TYPES = List.of(
            "cumulus",
            "stratus",
            "cirrus",
            "altostratus",
            "nimbostratus",
            "cumulonimbus");
    private static final List<String> GAS_STATE_CONVERTER_PARTS = List.of(
            "baffle",
            "glass",
            "inside",
            "left",
            "pedestal",
            "right",
            "top");

    @Test
    void shouldKeepCloudBlockSourcesPreviewsAndGameTexturesTraceable() {
        for (String cloudType : CLOUD_TYPES) {
            assertCloudBlockTrace(cloudType + "_cloud_block");
            assertCloudBlockTrace(cloudType + "_cloud_block_gas");
        }
    }

    @Test
    void shouldKeepGasStateConverterTexturesBasedOnProjectSourcesAndOriginalReferences() {
        for (String part : GAS_STATE_CONVERTER_PARTS) {
            assertTrue(Files.isRegularFile(TEXTURE_PROJECT.resolve("source/block/gas_state_converter/" + part + ".apx")));
            assertTrue(Files.isRegularFile(TEXTURE_PROJECT.resolve("preview/block/gas_state_converter/" + part + "-preview.png")));
            assertTrue(Files.isRegularFile(TEXTURE_PROJECT.resolve("reference/original/common/src/main/resources/assets/cloudcraft/textures/block/gas_state_converter/" + part + ".png")));
            assertTrue(Files.isRegularFile(MAIN_TEXTURES.resolve("block/gas_state_converter/" + part + ".png")));
        }
    }

    @Test
    void shouldMakeSolidAndGasCloudBlocksReadableByOpacity() throws IOException {
        for (String cloudType : CLOUD_TYPES) {
            ImageMetrics solid = metrics(cloudType + "_cloud_block");
            ImageMetrics gas = metrics(cloudType + "_cloud_block_gas");

            assertEquals(16, solid.width());
            assertEquals(16, solid.height());
            assertEquals(16, gas.width());
            assertEquals(16, gas.height());
            assertEquals(0, solid.transparentPixels(), cloudType + " solid cloud must be fully opaque");
            assertTrue(gas.transparentPixels() >= 12, cloudType + " gas cloud needs transparent air pockets");
            assertTrue(gas.averageAlpha() >= 40.0D, cloudType + " gas cloud must remain visible");
            assertTrue(gas.averageAlpha() <= 150.0D, cloudType + " gas cloud must read as translucent");
            assertTrue(gas.averageAlpha() < solid.averageAlpha() * 0.65D, cloudType + " gas cloud must be lighter in opacity than its solid block");
        }
    }

    @Test
    void shouldKeepCloudBlockTexturesTileableAtTheEdges() throws IOException {
        for (String cloudType : CLOUD_TYPES) {
            assertTileable(cloudType + "_cloud_block");
            assertTileable(cloudType + "_cloud_block_gas");
        }
    }

    @Test
    void shouldKeepCloudGeneraVisuallySeparatedByPaletteAndDensity() throws IOException {
        ImageMetrics cumulus = metrics("cumulus_cloud_block");
        ImageMetrics stratus = metrics("stratus_cloud_block");
        ImageMetrics cirrus = metrics("cirrus_cloud_block");
        ImageMetrics altostratus = metrics("altostratus_cloud_block");
        ImageMetrics nimbostratus = metrics("nimbostratus_cloud_block");
        ImageMetrics cumulonimbus = metrics("cumulonimbus_cloud_block");

        assertTrue(cumulus.luma() > stratus.luma() + 15.0D);
        assertTrue(cirrus.luma() > stratus.luma() + 10.0D);
        assertTrue(stratus.luma() > altostratus.luma() + 5.0D);
        assertTrue(altostratus.luma() > cumulonimbus.luma() + 45.0D);
        assertTrue(cumulonimbus.luma() > nimbostratus.luma() + 10.0D);
        assertTrue(cirrus.blueMinusRed() > 20.0D);
        assertTrue(altostratus.blueMinusRed() > stratus.blueMinusRed() + 10.0D);

        ImageMetrics cumulusGas = metrics("cumulus_cloud_block_gas");
        ImageMetrics cumulonimbusGas = metrics("cumulonimbus_cloud_block_gas");

        assertTrue(cumulusGas.averageAlpha() < 60.0D);
        assertTrue(cumulonimbusGas.averageAlpha() > 110.0D);
    }

    private static void assertCloudBlockTrace(String textureId) {
        assertTrue(Files.isRegularFile(TEXTURE_PROJECT.resolve("source/block/cloud_block/" + textureId + ".apx")));
        assertTrue(Files.isRegularFile(TEXTURE_PROJECT.resolve("preview/block/cloud_block/" + textureId + "-preview.png")));
        assertTrue(Files.isRegularFile(MAIN_TEXTURES.resolve("block/cloud_block/" + textureId + ".png")));
    }

    private static void assertTileable(String textureId) throws IOException {
        ImageMetrics image = metrics(textureId);

        assertTrue(image.leftRightEdgeDelta() <= 16.0D, textureId + " has a visible left/right tile break");
        assertTrue(image.topBottomEdgeDelta() <= 16.0D, textureId + " has a visible top/bottom tile break");
    }

    private static ImageMetrics metrics(String textureId) throws IOException {
        BufferedImage image = ImageIO.read(MAIN_TEXTURES.resolve("block/cloud_block/" + textureId + ".png").toFile());
        int width = image.getWidth();
        int height = image.getHeight();
        int transparentPixels = 0;
        double alphaSum = 0.0D;
        double redSum = 0.0D;
        double greenSum = 0.0D;
        double blueSum = 0.0D;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = image.getRGB(x, y);
                int alpha = alpha(argb);
                if (alpha == 0) {
                    transparentPixels++;
                }
                alphaSum += alpha;
                redSum += red(argb) * alpha;
                greenSum += green(argb) * alpha;
                blueSum += blue(argb) * alpha;
            }
        }

        double weightedAlpha = Math.max(1.0D, alphaSum);
        return new ImageMetrics(
                width,
                height,
                transparentPixels,
                alphaSum / (width * height),
                redSum / weightedAlpha,
                greenSum / weightedAlpha,
                blueSum / weightedAlpha,
                edgeDelta(image, true),
                edgeDelta(image, false));
    }

    private static double edgeDelta(BufferedImage image, boolean horizontal) {
        int samples = horizontal ? image.getHeight() : image.getWidth();
        double total = 0.0D;
        for (int index = 0; index < samples; index++) {
            int first = horizontal ? image.getRGB(0, index) : image.getRGB(index, 0);
            int second = horizontal ? image.getRGB(image.getWidth() - 1, index) : image.getRGB(index, image.getHeight() - 1);
            total += rgbaDelta(first, second);
        }
        return total / samples;
    }

    private static double rgbaDelta(int first, int second) {
        return (Math.abs(red(first) - red(second))
                + Math.abs(green(first) - green(second))
                + Math.abs(blue(first) - blue(second))
                + Math.abs(alpha(first) - alpha(second))) / 4.0D;
    }

    private static int alpha(int argb) {
        return (argb >>> 24) & 0xFF;
    }

    private static int red(int argb) {
        return (argb >>> 16) & 0xFF;
    }

    private static int green(int argb) {
        return (argb >>> 8) & 0xFF;
    }

    private static int blue(int argb) {
        return argb & 0xFF;
    }

    private record ImageMetrics(
            int width,
            int height,
            int transparentPixels,
            double averageAlpha,
            double red,
            double green,
            double blue,
            double leftRightEdgeDelta,
            double topBottomEdgeDelta) {
        private double luma() {
            return 0.2126D * red + 0.7152D * green + 0.0722D * blue;
        }

        private double blueMinusRed() {
            return blue - red;
        }
    }
}
