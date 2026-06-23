package com.tr1c.cloudcraft.registry;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloudCraftRegistryDefinitionsTest {
    private static final Path REGISTRY_SOURCE = Path.of("src/main/java/com/tr1c/cloudcraft/registry");
    private static final Path MAIN_RESOURCES = Path.of("src/main/resources");
    private static final Path GENERATED_RESOURCES = Path.of("src/generated/resources");
    private static final String MOD_ID = "cloudcraft";
    private static final Pattern DEFINITION_ID = Pattern.compile("definitions\\.put\\(ModIds\\.([A-Z0-9_]+),");
    private static final Pattern LANGUAGE_KEY = Pattern.compile("\\\"([^\\\"]+)\\\"\\s*:");

    @Test
    void shouldKeepItemDefinitionIdsComplete() throws IOException {
        assertEquals(List.of(
                "CLOUD",
                "CUMULUS_CLOUD_FRAGMENT",
                "STRATUS_WISP",
                "COMPRESSED_CANISTER",
                "CIRRUS_FILAMENT",
                "ALTOSTRATUS_VEIL",
                "NIMBOSTRATUS_FLEECE",
                "STORM_CORE",
                "BASIC_JETPACK_FRAME",
                "STABILIZED_NOZZLE",
                "HIGH_PRESSURE_CHAMBER",
                "CLOUD_JETPACK",
                "STABILIZED_CLOUD_JETPACK",
                "HIGH_PRESSURE_CLOUD_JETPACK"), definitionIds("CloudCraftItemDefinitions.java"));
    }

    @Test
    void shouldKeepBlockDefinitionIdsComplete() throws IOException {
        assertEquals(
                List.of(
                        "CUMULUS_CLOUD_BLOCK",
                        "CUMULUS_CLOUD_BLOCK_GAS",
                        "STRATUS_CLOUD_BLOCK",
                        "STRATUS_CLOUD_BLOCK_GAS",
                        "CIRRUS_CLOUD_BLOCK",
                        "CIRRUS_CLOUD_BLOCK_GAS",
                        "ALTOSTRATUS_CLOUD_BLOCK",
                        "ALTOSTRATUS_CLOUD_BLOCK_GAS",
                        "NIMBOSTRATUS_CLOUD_BLOCK",
                        "NIMBOSTRATUS_CLOUD_BLOCK_GAS",
                        "CUMULONIMBUS_CLOUD_BLOCK",
                        "CUMULONIMBUS_CLOUD_BLOCK_GAS",
                        "GAS_STATE_CONVERTER"),
                definitionIds("CloudCraftBlockDefinitions.java"));
    }

    @Test
    void shouldKeepBlockEntityDefinitionIdsComplete() throws IOException {
        String source = Files.readString(REGISTRY_SOURCE.resolve("CloudCraftRegistryDefinitions.java"));

        assertTrue(source.contains("private static final List<String> BLOCK_ENTITY_IDS = List.of(ModIds.GAS_STATE_CONVERTER);"));
        assertTrue(source.contains("public static List<String> blockEntityIds()"));
    }

    @Test
    void shouldCreateFunctionalGasStateConverterBlock() throws IOException {
        String source = Files.readString(REGISTRY_SOURCE.resolve("CloudCraftBlockDefinitions.java"));
        int factoryStart = source.indexOf("public static Block createGasStateConverter");
        int factoryEnd = source.indexOf("private static BlockBehaviour.Properties", factoryStart);
        String factorySource = source.substring(factoryStart, factoryEnd);

        assertTrue(factorySource.contains("new GasStateConverterBlock("));
        assertFalse(factorySource.contains("RotatableBlock"));
    }

    @Test
    void shouldKeepEffectDefinitionIdsComplete() throws IOException {
        assertEquals(List.of("CLOUD_WALKER"), definitionIds("CloudCraftEffectDefinitions.java"));
    }

    @Test
    void shouldKeepPotionDefinitionIdsComplete() throws IOException {
        assertEquals(List.of("SOLID_CLOUD_POTION"), definitionIds("CloudCraftPotionDefinitions.java"));
    }

    @Test
    void shouldKeepPotionRegistryIdStable() {
        assertEquals("solid_cloud", ModIds.SOLID_CLOUD_POTION);
    }

    @Test
    void shouldCreateBlockItemsForEveryBlock() throws IOException {
        assertEquals(definitionIds("CloudCraftBlockDefinitions.java"), registryAliasIds("BLOCK_ITEM_IDS"));
    }

    @Test
    void shouldRegisterSolidCloudBeforeGasCloud() throws IOException {
        List<String> blockIds = definitionIds("CloudCraftBlockDefinitions.java");

        assertIndexBefore(blockIds, "CUMULUS_CLOUD_BLOCK", "CUMULUS_CLOUD_BLOCK_GAS");
    }

    @Test
    void shouldKeepLanguageKeysInSync() throws IOException {
        assertEquals(languageKeys("en_us.json"), languageKeys("zh_cn.json"));
    }

    @Test
    void shouldHaveLanguageKeysForRegisteredContent() throws IOException {
        Set<String> languageKeys = languageKeys("en_us.json");

        for (String id : definitionValues("CloudCraftItemDefinitions.java")) {
            assertTrue(languageKeys.contains("item." + MOD_ID + "." + id));
        }
        for (String id : definitionValues("CloudCraftBlockDefinitions.java")) {
            assertTrue(languageKeys.contains("block." + MOD_ID + "." + id));
            assertTrue(languageKeys.contains("item." + MOD_ID + "." + id));
        }
        for (String id : definitionValues("CloudCraftEffectDefinitions.java")) {
            assertTrue(languageKeys.contains("effect." + MOD_ID + "." + id));
        }
        for (String id : definitionValues("CloudCraftPotionDefinitions.java")) {
            assertTrue(languageKeys.contains("item.minecraft.potion.effect." + id));
            assertTrue(languageKeys.contains("item.minecraft.splash_potion.effect." + id));
            assertTrue(languageKeys.contains("item.minecraft.lingering_potion.effect." + id));
        }
    }

    @Test
    void shouldKeepGeneratedResourcesComplete() {
        assertGeneratedResource("assets/cloudcraft/items/cloud.json");
        assertGeneratedResource("assets/cloudcraft/items/stratus_wisp.json");
        assertGeneratedResource("assets/cloudcraft/items/compressed_canister.json");
        assertGeneratedResource("assets/cloudcraft/items/cirrus_filament.json");
        assertGeneratedResource("assets/cloudcraft/items/altostratus_veil.json");
        assertGeneratedResource("assets/cloudcraft/items/nimbostratus_fleece.json");
        assertGeneratedResource("assets/cloudcraft/items/storm_core.json");
        assertGeneratedResource("assets/cloudcraft/items/basic_jetpack_frame.json");
        assertGeneratedResource("assets/cloudcraft/items/stabilized_nozzle.json");
        assertGeneratedResource("assets/cloudcraft/items/high_pressure_chamber.json");
        assertGeneratedResource("assets/cloudcraft/items/cloud_jetpack.json");
        assertGeneratedResource("assets/cloudcraft/items/stabilized_cloud_jetpack.json");
        assertGeneratedResource("assets/cloudcraft/items/high_pressure_cloud_jetpack.json");
        assertGeneratedResource("assets/cloudcraft/models/item/compressed_canister.json");
        assertGeneratedResource("assets/cloudcraft/models/item/stratus_wisp.json");
        assertGeneratedResource("assets/cloudcraft/models/item/cirrus_filament.json");
        assertGeneratedResource("assets/cloudcraft/models/item/altostratus_veil.json");
        assertGeneratedResource("assets/cloudcraft/models/item/nimbostratus_fleece.json");
        assertGeneratedResource("assets/cloudcraft/models/item/storm_core.json");
        assertGeneratedResource("assets/cloudcraft/models/item/basic_jetpack_frame.json");
        assertGeneratedResource("assets/cloudcraft/models/item/stabilized_nozzle.json");
        assertGeneratedResource("assets/cloudcraft/models/item/high_pressure_chamber.json");
        assertGeneratedResource("assets/cloudcraft/models/item/cloud_jetpack.json");
        assertGeneratedResource("assets/cloudcraft/models/item/stabilized_cloud_jetpack.json");
        assertGeneratedResource("assets/cloudcraft/models/item/high_pressure_cloud_jetpack.json");
        assertGeneratedResource("assets/cloudcraft/models/item/gas_state_converter.json");
        assertGeneratedResource("assets/cloudcraft/models/block/cumulus_cloud_block_gas.json");
        assertGeneratedResource("assets/cloudcraft/models/block/stratus_cloud_block.json");
        assertGeneratedResource("assets/cloudcraft/models/block/stratus_cloud_block_gas.json");
        assertGeneratedResource("assets/cloudcraft/models/block/cirrus_cloud_block.json");
        assertGeneratedResource("assets/cloudcraft/models/block/cirrus_cloud_block_gas.json");
        assertGeneratedResource("assets/cloudcraft/models/block/altostratus_cloud_block.json");
        assertGeneratedResource("assets/cloudcraft/models/block/altostratus_cloud_block_gas.json");
        assertGeneratedResource("assets/cloudcraft/models/block/nimbostratus_cloud_block.json");
        assertGeneratedResource("assets/cloudcraft/models/block/nimbostratus_cloud_block_gas.json");
        assertGeneratedResource("assets/cloudcraft/models/block/cumulonimbus_cloud_block.json");
        assertGeneratedResource("assets/cloudcraft/models/block/cumulonimbus_cloud_block_gas.json");
        assertGeneratedResource("assets/cloudcraft/blockstates/gas_state_converter.json");
        assertGeneratedResource("data/cloudcraft/recipe/compressed_canister.json");
        assertGeneratedResource("data/cloudcraft/recipe/stratus_wisp.json");
        assertGeneratedResource("data/cloudcraft/recipe/cirrus_filament.json");
        assertGeneratedResource("data/cloudcraft/recipe/altostratus_veil.json");
        assertGeneratedResource("data/cloudcraft/recipe/nimbostratus_fleece.json");
        assertGeneratedResource("data/cloudcraft/recipe/storm_core.json");
        assertGeneratedResource("data/cloudcraft/recipe/basic_jetpack_frame.json");
        assertGeneratedResource("data/cloudcraft/recipe/stabilized_nozzle.json");
        assertGeneratedResource("data/cloudcraft/recipe/high_pressure_chamber.json");
        assertGeneratedResource("data/cloudcraft/recipe/cloud_jetpack.json");
        assertGeneratedResource("data/cloudcraft/recipe/stabilized_cloud_jetpack.json");
        assertGeneratedResource("data/cloudcraft/recipe/high_pressure_cloud_jetpack.json");
        assertGeneratedResource("data/cloudcraft/recipe/stratus_cloud_block.json");
        assertGeneratedResource("data/cloudcraft/recipe/cirrus_cloud_block.json");
        assertGeneratedResource("data/cloudcraft/recipe/altostratus_cloud_block.json");
        assertGeneratedResource("data/cloudcraft/recipe/nimbostratus_cloud_block.json");
        assertGeneratedResource("data/cloudcraft/recipe/cumulonimbus_cloud_block.json");
        assertGeneratedResource("data/cloudcraft/recipe/gas_state_converter.json");
        assertGeneratedResource("data/cloudcraft/loot_table/blocks/cumulus_cloud_block_gas.json");
        assertGeneratedResource("data/cloudcraft/loot_table/blocks/stratus_cloud_block.json");
        assertGeneratedResource("data/cloudcraft/loot_table/blocks/stratus_cloud_block_gas.json");
        assertGeneratedResource("data/cloudcraft/loot_table/blocks/cirrus_cloud_block.json");
        assertGeneratedResource("data/cloudcraft/loot_table/blocks/cirrus_cloud_block_gas.json");
        assertGeneratedResource("data/cloudcraft/loot_table/blocks/altostratus_cloud_block.json");
        assertGeneratedResource("data/cloudcraft/loot_table/blocks/altostratus_cloud_block_gas.json");
        assertGeneratedResource("data/cloudcraft/loot_table/blocks/nimbostratus_cloud_block.json");
        assertGeneratedResource("data/cloudcraft/loot_table/blocks/nimbostratus_cloud_block_gas.json");
        assertGeneratedResource("data/cloudcraft/loot_table/blocks/cumulonimbus_cloud_block.json");
        assertGeneratedResource("data/cloudcraft/loot_table/blocks/cumulonimbus_cloud_block_gas.json");
        assertGeneratedResource("data/cloudcraft/loot_table/blocks/gas_state_converter.json");
        assertGeneratedResource("data/cloudcraft/advancement/root.json");
        assertGeneratedResource("data/cloudcraft/advancement/cloud_fragment.json");
        assertGeneratedResource("data/cloudcraft/advancement/compressed_canister.json");
        assertGeneratedResource("data/cloudcraft/advancement/cirrus_filament.json");
        assertGeneratedResource("data/cloudcraft/advancement/storm_core.json");
        assertGeneratedResource("data/cloudcraft/advancement/basic_jetpack_frame.json");
        assertGeneratedResource("data/cloudcraft/advancement/cloud_jetpack.json");
        assertGeneratedResource("data/cloudcraft/advancement/stabilized_cloud_jetpack.json");
        assertGeneratedResource("data/cloudcraft/advancement/high_pressure_cloud_jetpack.json");
        assertGeneratedResource("data/cloudcraft/advancement/gas_state_converter.json");
        assertGeneratedResource("data/cloudcraft/advancement/enter_cloud_dimension.json");
    }

    @Test
    void shouldHaveCloudDimensionData() {
        assertMainResource("data/cloudcraft/dimension/cloud_dimension.json");
        assertMainResource("data/cloudcraft/dimension_type/cloud_dimension.json");
        assertMainResource("data/cloudcraft/worldgen/biome/cumulus_fields.json");
        assertMainResource("data/cloudcraft/worldgen/configured_feature/stratus_cloud_patch.json");
        assertMainResource("data/cloudcraft/worldgen/configured_feature/nimbostratus_cloud_patch.json");
        assertMainResource("data/cloudcraft/worldgen/configured_feature/cumulonimbus_cloud_patch.json");
        assertMainResource("data/cloudcraft/worldgen/configured_feature/cirrus_gas_wisps.json");
        assertMainResource("data/cloudcraft/worldgen/placed_feature/stratus_cloud_patch.json");
        assertMainResource("data/cloudcraft/worldgen/placed_feature/nimbostratus_cloud_patch.json");
        assertMainResource("data/cloudcraft/worldgen/placed_feature/cumulonimbus_cloud_patch.json");
        assertMainResource("data/cloudcraft/worldgen/placed_feature/cirrus_gas_wisps.json");
    }

    @Test
    void shouldEnableNaturalCloudDimensionFeatures() throws IOException {
        String dimension = Files.readString(MAIN_RESOURCES.resolve("data/cloudcraft/dimension/cloud_dimension.json"));
        String preset = Files.readString(MAIN_RESOURCES.resolve("data/cloudcraft/worldgen/world_preset/cloud_dimension.json"));
        String biome = Files.readString(MAIN_RESOURCES.resolve("data/cloudcraft/worldgen/biome/cumulus_fields.json"));

        assertTrue(dimension.contains("\"features\": true"));
        assertTrue(preset.contains("\"features\": true"));
        assertTrue(biome.contains("\"cloudcraft:stratus_cloud_patch\""));
        assertTrue(biome.contains("\"cloudcraft:nimbostratus_cloud_patch\""));
        assertTrue(biome.contains("\"cloudcraft:cumulonimbus_cloud_patch\""));
        assertTrue(biome.contains("\"cloudcraft:cirrus_gas_wisps\""));
    }

    private static List<String> definitionIds(String sourceFile) throws IOException {
        return DEFINITION_ID.matcher(Files.readString(REGISTRY_SOURCE.resolve(sourceFile)))
                .results()
                .map(MatchResult::group)
                .map(match -> match.substring("definitions.put(ModIds.".length(), match.length() - 1))
                .toList();
    }

    private static List<String> definitionValues(String sourceFile) throws IOException {
        return definitionIds(sourceFile).stream()
                .map(CloudCraftRegistryDefinitionsTest::modIdValue)
                .toList();
    }

    private static String modIdValue(String constantName) {
        return switch (constantName) {
            case "CLOUD" -> "cloud";
            case "CUMULUS_CLOUD_FRAGMENT" -> "cumulus_cloud_fragment";
            case "STRATUS_WISP" -> "stratus_wisp";
            case "COMPRESSED_CANISTER" -> "compressed_canister";
            case "CIRRUS_FILAMENT" -> "cirrus_filament";
            case "ALTOSTRATUS_VEIL" -> "altostratus_veil";
            case "NIMBOSTRATUS_FLEECE" -> "nimbostratus_fleece";
            case "STORM_CORE" -> "storm_core";
            case "BASIC_JETPACK_FRAME" -> "basic_jetpack_frame";
            case "STABILIZED_NOZZLE" -> "stabilized_nozzle";
            case "HIGH_PRESSURE_CHAMBER" -> "high_pressure_chamber";
            case "CLOUD_JETPACK" -> "cloud_jetpack";
            case "STABILIZED_CLOUD_JETPACK" -> "stabilized_cloud_jetpack";
            case "HIGH_PRESSURE_CLOUD_JETPACK" -> "high_pressure_cloud_jetpack";
            case "CUMULUS_CLOUD_BLOCK" -> "cumulus_cloud_block";
            case "CUMULUS_CLOUD_BLOCK_GAS" -> "cumulus_cloud_block_gas";
            case "STRATUS_CLOUD_BLOCK" -> "stratus_cloud_block";
            case "STRATUS_CLOUD_BLOCK_GAS" -> "stratus_cloud_block_gas";
            case "CIRRUS_CLOUD_BLOCK" -> "cirrus_cloud_block";
            case "CIRRUS_CLOUD_BLOCK_GAS" -> "cirrus_cloud_block_gas";
            case "ALTOSTRATUS_CLOUD_BLOCK" -> "altostratus_cloud_block";
            case "ALTOSTRATUS_CLOUD_BLOCK_GAS" -> "altostratus_cloud_block_gas";
            case "NIMBOSTRATUS_CLOUD_BLOCK" -> "nimbostratus_cloud_block";
            case "NIMBOSTRATUS_CLOUD_BLOCK_GAS" -> "nimbostratus_cloud_block_gas";
            case "CUMULONIMBUS_CLOUD_BLOCK" -> "cumulonimbus_cloud_block";
            case "CUMULONIMBUS_CLOUD_BLOCK_GAS" -> "cumulonimbus_cloud_block_gas";
            case "GAS_STATE_CONVERTER" -> "gas_state_converter";
            case "CLOUD_WALKER" -> "cloud_walker";
            case "SOLID_CLOUD_POTION" -> "solid_cloud";
            default -> throw new IllegalArgumentException(constantName);
        };
    }

    private static List<String> registryAliasIds(String aliasName) throws IOException {
        String source = Files.readString(REGISTRY_SOURCE.resolve("CloudCraftRegistryDefinitions.java"));
        if (source.contains("private static final List<String> " + aliasName + " = BLOCK_IDS;")) {
            return definitionIds("CloudCraftBlockDefinitions.java");
        }
        return List.of();
    }

    private static Set<String> languageKeys(String languageFile) throws IOException {
        return LANGUAGE_KEY.matcher(Files.readString(MAIN_RESOURCES.resolve("assets/cloudcraft/lang").resolve(languageFile)))
                .results()
                .map(MatchResult::group)
                .map(match -> match.substring(1, match.indexOf("\"", 1)))
                .collect(java.util.stream.Collectors.toSet());
    }

    @Test
    void shouldHaveTexturesForGeneratedItems() throws IOException {
        for (String id : definitionValues("CloudCraftItemDefinitions.java")) {
            assertMainResource("assets/cloudcraft/textures/item/" + id + ".png");
        }
    }

    @Test
    void shouldNotKeepUnregisteredCumulusArmorTexturesInMainResources() {
        assertFalse(Files.exists(MAIN_RESOURCES.resolve("assets/cloudcraft/textures/item/cumulus_cloud_helmet.png")));
        assertFalse(Files.exists(MAIN_RESOURCES.resolve("assets/cloudcraft/textures/item/cumulus_cloud_chestplate.png")));
        assertFalse(Files.exists(MAIN_RESOURCES.resolve("assets/cloudcraft/textures/item/cumulus_cloud_leggings.png")));
        assertFalse(Files.exists(MAIN_RESOURCES.resolve("assets/cloudcraft/textures/item/cumulus_cloud_boots.png")));
    }

    private static void assertGeneratedResource(String path) {
        assertTrue(Files.isRegularFile(GENERATED_RESOURCES.resolve(path)));
    }

    private static void assertMainResource(String path) {
        assertTrue(Files.isRegularFile(MAIN_RESOURCES.resolve(path)));
    }

    private static void assertIndexBefore(List<String> ids, String first, String second) {
        int firstIndex = ids.indexOf(first);
        int secondIndex = ids.indexOf(second);

        assertEquals(true, firstIndex >= 0 && firstIndex < secondIndex);
    }
}
