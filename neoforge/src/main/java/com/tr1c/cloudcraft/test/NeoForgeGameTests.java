package com.tr1c.cloudcraft.test;

import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.block.NeoForgeModBlocks;
import com.tr1c.cloudcraft.block.custom.GasStateConverterOperation;
import com.tr1c.cloudcraft.block.entity.GasStateConverterBlockEntity;
import com.tr1c.cloudcraft.cloudtech.CloudPressureProfile;
import com.tr1c.cloudcraft.cloudtech.CompressedAirRules;
import com.tr1c.cloudcraft.cloudtech.JetpackRuntime;
import com.tr1c.cloudcraft.block.custom.cloud_block.CloudTransformationRules;
import com.tr1c.cloudcraft.block.custom.cloud_block.CloudTransformationRuntime;
import com.tr1c.cloudcraft.effect.NeoForgeModEffects;
import com.tr1c.cloudcraft.entity.CloudWispEntity;
import com.tr1c.cloudcraft.entity.NeoForgeModEntities;
import com.tr1c.cloudcraft.item.NeoForgeModItems;
import com.tr1c.cloudcraft.registry.ModIds;
import com.tr1c.cloudcraft.world.CloudDimensionKeys;
import com.tr1c.cloudcraft.world.CloudDimensionTravel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

public final class NeoForgeGameTests {
    private static final Identifier EMPTY_STRUCTURE = Identifier.withDefaultNamespace("empty");
    private static final int MAX_TICKS = 40;
    private static final DeferredRegister<Consumer<GameTestHelper>> TEST_FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, CloudCraft.MOD_ID);
    private static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GAS_CLOUD_SOLIDIFIES = TEST_FUNCTIONS.register("gas_cloud_solidifies", () -> NeoForgeGameTests::gasCloudSolidifies);
    private static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CLOUD_WALKER_EFFECT_KEEPS_GAS_CLOUD = TEST_FUNCTIONS.register(
            "cloud_walker_effect_keeps_gas_cloud",
            () -> NeoForgeGameTests::cloudWalkerEffectKeepsGasCloud);
    private static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> SOLIDIFY_IN_RADIUS_CONVERTS_NEARBY_GAS_CLOUDS = TEST_FUNCTIONS.register(
            "solidify_in_radius_converts_nearby_gas_clouds",
            () -> NeoForgeGameTests::solidifyInRadiusConvertsNearbyGasClouds);
    private static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CLOUD_WALKER_EFFECT_KEEPS_GAS_CLOUD_CROSS = TEST_FUNCTIONS.register(
            "cloud_walker_effect_keeps_gas_cloud_cross",
            () -> NeoForgeGameTests::cloudWalkerEffectKeepsGasCloudCross);
    private static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> SOLIDIFY_IN_RADIUS_KEEPS_NON_GAS_BLOCKS = TEST_FUNCTIONS.register(
            "solidify_in_radius_keeps_non_gas_blocks",
            () -> NeoForgeGameTests::solidifyInRadiusKeepsNonGasBlocks);
    private static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CLOUD_WALKER_SHORT_CIRCUIT_KEEPS_DISTANT_GAS = TEST_FUNCTIONS.register(
            "cloud_walker_short_circuit_keeps_distant_gas",
            () -> NeoForgeGameTests::cloudWalkerShortCircuitKeepsDistantGas);
    private static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GAS_STATE_CONVERTER_SOLIDIFIES_GAS_CLOUDS = TEST_FUNCTIONS.register(
            "gas_state_converter_solidifies_gas_clouds",
            () -> NeoForgeGameTests::gasStateConverterSolidifiesGasClouds);
    private static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GAS_STATE_CONVERTER_GASIFIES_SOLID_CLOUDS = TEST_FUNCTIONS.register(
            "gas_state_converter_gasifies_solid_clouds",
            () -> NeoForgeGameTests::gasStateConverterGasifiesSolidClouds);
    private static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CLOUD_DIMENSION_LOADS = TEST_FUNCTIONS.register(
            "cloud_dimension_loads",
            () -> NeoForgeGameTests::cloudDimensionLoads);
    private static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CLOUD_DIMENSION_LANDING_CREATES_RETURN_ANCHOR = TEST_FUNCTIONS.register(
            "cloud_dimension_landing_creates_return_anchor",
            () -> NeoForgeGameTests::cloudDimensionLandingCreatesReturnAnchor);
    private static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CLOUD_DIMENSION_LANDING_SUPPORTS_JETPACK_RECHARGE = TEST_FUNCTIONS.register(
            "cloud_dimension_landing_supports_jetpack_recharge",
            () -> NeoForgeGameTests::cloudDimensionLandingSupportsJetpackRecharge);
    private static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CLOUD_DIMENSION_BIOME_HAS_NATURAL_FEATURES = TEST_FUNCTIONS.register(
            "cloud_dimension_biome_has_natural_features",
            () -> NeoForgeGameTests::cloudDimensionBiomeHasNaturalFeatures);
    private static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CLOUD_WISP_SPAWNS_AS_PASSIVE_DRIFTER = TEST_FUNCTIONS.register(
            "cloud_wisp_spawns_as_passive_drifter",
            () -> NeoForgeGameTests::cloudWispSpawnsAsPassiveDrifter);

    private NeoForgeGameTests() {
    }

    public static void register(IEventBus modEventBus) {
        TEST_FUNCTIONS.register(modEventBus);
    }

    public static void register(RegisterGameTestsEvent event) {
        Holder<TestEnvironmentDefinition> environment = event.registerEnvironment(id("default_environment"), new TestEnvironmentDefinition.AllOf());
        register(event, environment, GAS_CLOUD_SOLIDIFIES);
        register(event, environment, CLOUD_WALKER_EFFECT_KEEPS_GAS_CLOUD);
        register(event, environment, SOLIDIFY_IN_RADIUS_CONVERTS_NEARBY_GAS_CLOUDS);
        register(event, environment, CLOUD_WALKER_EFFECT_KEEPS_GAS_CLOUD_CROSS);
        register(event, environment, SOLIDIFY_IN_RADIUS_KEEPS_NON_GAS_BLOCKS);
        register(event, environment, CLOUD_WALKER_SHORT_CIRCUIT_KEEPS_DISTANT_GAS);
        register(event, environment, GAS_STATE_CONVERTER_SOLIDIFIES_GAS_CLOUDS);
        register(event, environment, GAS_STATE_CONVERTER_GASIFIES_SOLID_CLOUDS);
        register(event, environment, CLOUD_DIMENSION_LOADS);
        register(event, environment, CLOUD_DIMENSION_LANDING_CREATES_RETURN_ANCHOR);
        register(event, environment, CLOUD_DIMENSION_LANDING_SUPPORTS_JETPACK_RECHARGE);
        register(event, environment, CLOUD_DIMENSION_BIOME_HAS_NATURAL_FEATURES);
        register(event, environment, CLOUD_WISP_SPAWNS_AS_PASSIVE_DRIFTER);
    }

    private static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition> environment, DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> testFunction) {
        event.registerTest(
                testFunction.getKey().identifier(),
                new FunctionGameTestInstance(
                        testFunction.getKey(),
                        new TestData<>(environment, EMPTY_STRUCTURE, MAX_TICKS, 0, true, Rotation.NONE)));
    }

    private static void gasCloudSolidifies(GameTestHelper helper) {
        BlockPos target = new BlockPos(0, 0, 0);
        helper.setBlock(target, NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get());
        NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get().solidify(helper.getLevel(), helper.absolutePos(target));
        helper.assertBlockPresent(NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK.get(), target);
        helper.succeed();
    }

    private static void cloudWalkerEffectKeepsGasCloud(GameTestHelper helper) {
        BlockPos center = new BlockPos(0, 1, 0);
        BlockPos target = center.below();
        helper.setBlock(target, NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get());

        var pig = helper.spawnWithNoFreeWill(EntityType.PIG, center);
        pig.addEffect(new MobEffectInstance(NeoForgeModEffects.CLOUD_WALKER, 200, 0));

        helper.assertBlockPresent(NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get(), target);
        helper.succeed();
    }

    private static void solidifyInRadiusConvertsNearbyGasClouds(GameTestHelper helper) {
        BlockPos center = new BlockPos(0, 0, 0);
        BlockPos nearTarget = new BlockPos(1, 0, 0);
        BlockPos farTarget = new BlockPos(3, 0, 0);
        helper.setBlock(center, NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get());
        helper.setBlock(nearTarget, NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get());
        helper.setBlock(farTarget, NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get());

        CloudTransformationRuntime.solidifyInRadius(helper.getLevel(), helper.absolutePos(center), CloudTransformationRules.SPLASH_POTION_RADIUS);

        helper.runAfterDelay(2, () -> {
            helper.assertBlockPresent(NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK.get(), center);
            helper.assertBlockPresent(NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK.get(), nearTarget);
            helper.assertBlockPresent(NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get(), farTarget);
            helper.succeed();
        });
    }

    private static void cloudWalkerEffectKeepsGasCloudCross(GameTestHelper helper) {
        BlockPos center = new BlockPos(0, 1, 0);
        BlockPos below = new BlockPos(0, 0, 0);
        BlockPos east = new BlockPos(1, 0, 0);
        BlockPos west = new BlockPos(-1, 0, 0);
        BlockPos south = new BlockPos(0, 0, 1);
        BlockPos north = new BlockPos(0, 0, -1);
        helper.setBlock(below, NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get());
        helper.setBlock(east, NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get());
        helper.setBlock(west, NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get());
        helper.setBlock(south, NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get());
        helper.setBlock(north, NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get());

        var pig = helper.spawnWithNoFreeWill(EntityType.PIG, center);
        pig.addEffect(new MobEffectInstance(NeoForgeModEffects.CLOUD_WALKER, 200, 0));

        helper.runAfterDelay(2, () -> {
            helper.assertBlockPresent(NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get(), below);
            helper.assertBlockPresent(NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get(), east);
            helper.assertBlockPresent(NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get(), west);
            helper.assertBlockPresent(NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get(), south);
            helper.assertBlockPresent(NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get(), north);
            helper.succeed();
        });
    }

    private static void solidifyInRadiusKeepsNonGasBlocks(GameTestHelper helper) {
        BlockPos center = new BlockPos(0, 0, 0);
        BlockPos nonGasTarget = new BlockPos(1, 0, 0);
        helper.setBlock(center, NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get());
        helper.setBlock(nonGasTarget, NeoForgeModBlocks.GAS_STATE_CONVERTER.get());

        CloudTransformationRuntime.solidifyInRadius(helper.getLevel(), helper.absolutePos(center), CloudTransformationRules.SPLASH_POTION_RADIUS);

        helper.runAfterDelay(2, () -> {
            helper.assertBlockPresent(NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK.get(), center);
            helper.assertBlockPresent(NeoForgeModBlocks.GAS_STATE_CONVERTER.get(), nonGasTarget);
            helper.succeed();
        });
    }

    private static void cloudWalkerShortCircuitKeepsDistantGas(GameTestHelper helper) {
        BlockPos center = new BlockPos(0, 1, 0);
        BlockPos distantTarget = new BlockPos(0, 0, 2);
        helper.setBlock(distantTarget, NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get());

        var pig = helper.spawnWithNoFreeWill(EntityType.PIG, center);
        pig.addEffect(new MobEffectInstance(NeoForgeModEffects.CLOUD_WALKER, 200, 0));

        helper.runAfterDelay(2, () -> {
            helper.assertBlockPresent(NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get(), distantTarget);
            helper.succeed();
        });
    }

    private static void gasStateConverterSolidifiesGasClouds(GameTestHelper helper) {
        BlockPos converter = new BlockPos(0, 0, 0);
        BlockPos nearTarget = new BlockPos(2, 0, 0);
        BlockPos farTarget = new BlockPos(3, 0, 0);
        helper.setBlock(converter, NeoForgeModBlocks.GAS_STATE_CONVERTER.get());
        helper.setBlock(nearTarget, NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get());
        helper.setBlock(farTarget, NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get());

        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(NeoForgeModItems.itemById(ModIds.CUMULUS_CLOUD_FRAGMENT)));
        helper.useBlock(converter, player);

        helper.runAfterDelay(2, () -> {
            helper.assertBlockPresent(NeoForgeModBlocks.GAS_STATE_CONVERTER.get(), converter);
            helper.assertBlockPresent(NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK.get(), nearTarget);
            helper.assertBlockPresent(NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK_GAS.get(), farTarget);
            helper.assertTrue(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), "Converter should consume one cumulus fragment");
            helper.assertBlockEntityData(
                    converter,
                    GasStateConverterBlockEntity.class,
                    entity -> entity.lastOperation() == GasStateConverterOperation.SOLIDIFY && entity.lastConvertedCount() == 1,
                    () -> net.minecraft.network.chat.Component.literal("Converter should record solidify work"));
            helper.succeed();
        });
    }

    private static void gasStateConverterGasifiesSolidClouds(GameTestHelper helper) {
        BlockPos converter = new BlockPos(0, 0, 0);
        BlockPos nearTarget = new BlockPos(2, 0, 0);
        BlockPos farTarget = new BlockPos(3, 0, 0);
        helper.setBlock(converter, NeoForgeModBlocks.GAS_STATE_CONVERTER.get());
        helper.setBlock(nearTarget, NeoForgeModBlocks.CIRRUS_CLOUD_BLOCK.get());
        helper.setBlock(farTarget, NeoForgeModBlocks.CIRRUS_CLOUD_BLOCK.get());

        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(NeoForgeModItems.itemById(ModIds.CIRRUS_FILAMENT)));
        helper.useBlock(converter, player);

        helper.runAfterDelay(2, () -> {
            helper.assertBlockPresent(NeoForgeModBlocks.GAS_STATE_CONVERTER.get(), converter);
            helper.assertBlockPresent(NeoForgeModBlocks.CIRRUS_CLOUD_BLOCK_GAS.get(), nearTarget);
            helper.assertBlockPresent(NeoForgeModBlocks.CIRRUS_CLOUD_BLOCK.get(), farTarget);
            helper.assertTrue(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), "Converter should consume one cirrus filament");
            helper.assertBlockEntityData(
                    converter,
                    GasStateConverterBlockEntity.class,
                    entity -> entity.lastOperation() == GasStateConverterOperation.GASIFY && entity.lastConvertedCount() == 1,
                    () -> net.minecraft.network.chat.Component.literal("Converter should record gasify work"));
            helper.succeed();
        });
    }

    private static void cloudDimensionLoads(GameTestHelper helper) {
        var worldPresetRegistry = helper.getLevel().registryAccess().lookupOrThrow(Registries.WORLD_PRESET);
        ResourceKey<WorldPreset> cloudPreset = ResourceKey.create(Registries.WORLD_PRESET, ModIds.id(ModIds.CLOUD_DIMENSION));

        helper.assertTrue(
                worldPresetRegistry.containsKey(cloudPreset),
                "Cloud dimension world preset should be loaded");
        helper.succeed();
    }

    private static void cloudDimensionLandingCreatesReturnAnchor(GameTestHelper helper) {
        BlockPos anchor = helper.absolutePos(new BlockPos(0, 2, 0));

        helper.runAfterDelay(4, () -> {
            CloudDimensionTravel.prepareCloudLanding(
                    helper.getLevel(),
                    anchor,
                    NeoForgeModBlocks.GAS_STATE_CONVERTER.get().defaultBlockState());
            helper.assertTrue(helper.getLevel().getBlockState(anchor).is(NeoForgeModBlocks.GAS_STATE_CONVERTER.get()), "Cloud landing should contain a return converter");
            helper.assertTrue(helper.getLevel().getBlockState(anchor.below()).is(NeoForgeModBlocks.CUMULUS_CLOUD_BLOCK.get()), "Cloud landing should contain a cumulus platform");
            helper.succeed();
        });
    }

    private static void cloudDimensionLandingSupportsJetpackRecharge(GameTestHelper helper) {
        BlockPos anchor = helper.absolutePos(new BlockPos(0, 2, 0));

        helper.runAfterDelay(4, () -> {
            CloudDimensionTravel.prepareCloudLanding(
                    helper.getLevel(),
                    anchor,
                    NeoForgeModBlocks.GAS_STATE_CONVERTER.get().defaultBlockState());
            CloudPressureProfile profile = JetpackRuntime.rechargeProfileAt(helper.getLevel(), anchor.offset(2, 0, 0));
            helper.assertTrue(profile == CloudPressureProfile.CUMULUS, "Cloud landing surface should expose cumulus recharge");
            helper.assertTrue(
                    CompressedAirRules.rechargeRate(profile, ModIds.CLOUD_JETPACK) > 0,
                    "Cloud landing surface should recharge cloud jetpacks");
            helper.succeed();
        });
    }

    private static void cloudDimensionBiomeHasNaturalFeatures(GameTestHelper helper) {
        var biomeRegistry = helper.getLevel().registryAccess().lookupOrThrow(Registries.BIOME);
        Biome biome = biomeRegistry.getOrThrow(CloudDimensionKeys.CUMULUS_FIELDS).value();

        assertHasPlacedFeature(helper, biome, "stratus_cloud_patch");
        assertHasPlacedFeature(helper, biome, "nimbostratus_cloud_patch");
        assertHasPlacedFeature(helper, biome, "cumulonimbus_cloud_patch");
        assertHasPlacedFeature(helper, biome, "cirrus_gas_wisps");
        helper.succeed();
    }

    private static void cloudWispSpawnsAsPassiveDrifter(GameTestHelper helper) {
        BlockPos pos = new BlockPos(0, 2, 0);
        CloudWispEntity wisp = helper.spawn(NeoForgeModEntities.CLOUD_WISP.get(), pos);

        helper.assertEntityPresent(NeoForgeModEntities.CLOUD_WISP.get(), pos, 2.0);
        helper.assertEntityProperty(wisp, CloudWispEntity::isNoGravity, true, net.minecraft.network.chat.Component.literal("Cloud wisp should float"));
        helper.assertEntityProperty(wisp, CloudWispEntity::isCharging, false, net.minecraft.network.chat.Component.literal("Cloud wisp should not charge attacks"));
        helper.assertEntityProperty(wisp, CloudWispEntity::getExplosionPower, 0, net.minecraft.network.chat.Component.literal("Cloud wisp should not explode"));
        helper.succeed();
    }

    private static void assertHasPlacedFeature(GameTestHelper helper, Biome biome, String featureId) {
        ResourceKey<PlacedFeature> key = ResourceKey.create(Registries.PLACED_FEATURE, ModIds.id(featureId));
        boolean present = biome.getGenerationSettings().features().stream()
                .flatMap(holderSet -> holderSet.stream())
                .anyMatch(holder -> holder.unwrapKey().map(key::equals).orElse(false));

        helper.assertTrue(present, "Cumulus fields should include placed feature " + key.identifier());
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(CloudCraft.MOD_ID, path);
    }
}
