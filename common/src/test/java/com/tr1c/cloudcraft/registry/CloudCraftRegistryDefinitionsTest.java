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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
        assertEquals(List.of("CLOUD", "CUMULUS_CLOUD_FRAGMENT"), definitionIds("CloudCraftItemDefinitions.java"));
    }

    @Test
    void shouldKeepBlockDefinitionIdsComplete() throws IOException {
        assertEquals(
                List.of("CUMULUS_CLOUD_BLOCK", "CUMULUS_CLOUD_BLOCK_GAS", "GAS_STATE_CONVERTER"),
                definitionIds("CloudCraftBlockDefinitions.java"));
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
    void shouldKeepPotionRegistryIdSeparateFromBottleModelId() {
        assertEquals("solid_cloud", ModIds.SOLID_CLOUD_POTION);
        assertEquals("potion_bottle_solid_cloud", ModIds.SOLID_CLOUD_POTION_BOTTLE_MODEL);
        assertNotEquals(ModIds.SOLID_CLOUD_POTION, ModIds.SOLID_CLOUD_POTION_BOTTLE_MODEL);
    }

    @Test
    void shouldExposePotionBottleItemModelIds() throws IOException {
        String source = Files.readString(REGISTRY_SOURCE.resolve("CloudCraftRegistryDefinitions.java"));

        assertEquals(true, source.contains("List.of(ModIds.SOLID_CLOUD_POTION_BOTTLE_MODEL)"));
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
        assertGeneratedResource("assets/cloudcraft/items/potion_bottle_solid_cloud.json");
        assertGeneratedResource("assets/cloudcraft/models/item/gas_state_converter.json");
        assertGeneratedResource("assets/cloudcraft/models/block/cumulus_cloud_block_gas.json");
        assertGeneratedResource("assets/cloudcraft/blockstates/gas_state_converter.json");
        assertGeneratedResource("data/cloudcraft/recipe/gas_state_converter.json");
        assertGeneratedResource("data/cloudcraft/loot_table/blocks/cumulus_cloud_block_gas.json");
        assertGeneratedResource("data/cloudcraft/loot_table/blocks/gas_state_converter.json");
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
            case "CUMULUS_CLOUD_BLOCK" -> "cumulus_cloud_block";
            case "CUMULUS_CLOUD_BLOCK_GAS" -> "cumulus_cloud_block_gas";
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

    private static void assertGeneratedResource(String path) {
        assertTrue(Files.isRegularFile(GENERATED_RESOURCES.resolve(path)));
    }

    private static void assertIndexBefore(List<String> ids, String first, String second) {
        int firstIndex = ids.indexOf(first);
        int secondIndex = ids.indexOf(second);

        assertEquals(true, firstIndex >= 0 && firstIndex < secondIndex);
    }
}
