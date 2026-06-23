package com.tr1c.cloudcraft.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tr1c.cloudcraft.CloudCraft;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FabricCloudCraftConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String PRESSURE_CAPACITY_MULTIPLIER = "pressureCapacityMultiplier";
    private static final String PRESSURE_COST_MULTIPLIER = "pressureCostMultiplier";
    private static final String PRESSURE_RECHARGE_MULTIPLIER = "pressureRechargeMultiplier";
    private static final String JETPACK_VERTICAL_SPEED_MULTIPLIER = "jetpackVerticalSpeedMultiplier";
    private static final String JETPACK_HORIZONTAL_ACCELERATION_MULTIPLIER = "jetpackHorizontalAccelerationMultiplier";
    private static final String JETPACK_HORIZONTAL_MAX_SPEED_MULTIPLIER = "jetpackHorizontalMaxSpeedMultiplier";
    private static final String GAS_CLOUD_HORIZONTAL_TARGET_SPEED_MULTIPLIER = "gasCloudHorizontalTargetSpeedMultiplier";
    private static final String GAS_CLOUD_HORIZONTAL_CONVERGENCE_MULTIPLIER = "gasCloudHorizontalConvergenceMultiplier";

    private FabricCloudCraftConfig() {
    }

    public static void load() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(CloudCraft.MOD_ID + ".json");
        CloudCraftConfig.Values values = CloudCraftConfig.Values.DEFAULT;
        try {
            values = read(path);
        } catch (IOException | RuntimeException ignored) {
            values = CloudCraftConfig.Values.DEFAULT;
        }
        CloudCraftConfig.apply(values);
        try {
            write(path, values);
        } catch (IOException ignored) {
            // Loading with defaults is still better than failing mod initialization over a config write.
        }
    }

    private static CloudCraftConfig.Values read(Path path) throws IOException {
        if (!Files.exists(path)) {
            return CloudCraftConfig.Values.DEFAULT;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            CloudCraftConfig.Values defaults = CloudCraftConfig.Values.DEFAULT;
            return new CloudCraftConfig.Values(
                    readDouble(root, PRESSURE_CAPACITY_MULTIPLIER, defaults.pressureCapacityMultiplier()),
                    readDouble(root, PRESSURE_COST_MULTIPLIER, defaults.pressureCostMultiplier()),
                    readDouble(root, PRESSURE_RECHARGE_MULTIPLIER, defaults.pressureRechargeMultiplier()),
                    readDouble(root, JETPACK_VERTICAL_SPEED_MULTIPLIER, defaults.jetpackVerticalSpeedMultiplier()),
                    readDouble(root, JETPACK_HORIZONTAL_ACCELERATION_MULTIPLIER, defaults.jetpackHorizontalAccelerationMultiplier()),
                    readDouble(root, JETPACK_HORIZONTAL_MAX_SPEED_MULTIPLIER, defaults.jetpackHorizontalMaxSpeedMultiplier()),
                    readDouble(root, GAS_CLOUD_HORIZONTAL_TARGET_SPEED_MULTIPLIER, defaults.gasCloudHorizontalTargetSpeedMultiplier()),
                    readDouble(root, GAS_CLOUD_HORIZONTAL_CONVERGENCE_MULTIPLIER, defaults.gasCloudHorizontalConvergenceMultiplier()));
        }
    }

    private static double readDouble(JsonObject root, String key, double defaultValue) {
        JsonElement element = root.get(key);
        if (element == null || !element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber()) {
            return defaultValue;
        }
        return element.getAsDouble();
    }

    private static void write(Path path, CloudCraftConfig.Values values) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            GSON.toJson(json(values), writer);
        }
    }

    private static JsonObject json(CloudCraftConfig.Values values) {
        JsonObject root = new JsonObject();
        root.addProperty(PRESSURE_CAPACITY_MULTIPLIER, values.pressureCapacityMultiplier());
        root.addProperty(PRESSURE_COST_MULTIPLIER, values.pressureCostMultiplier());
        root.addProperty(PRESSURE_RECHARGE_MULTIPLIER, values.pressureRechargeMultiplier());
        root.addProperty(JETPACK_VERTICAL_SPEED_MULTIPLIER, values.jetpackVerticalSpeedMultiplier());
        root.addProperty(JETPACK_HORIZONTAL_ACCELERATION_MULTIPLIER, values.jetpackHorizontalAccelerationMultiplier());
        root.addProperty(JETPACK_HORIZONTAL_MAX_SPEED_MULTIPLIER, values.jetpackHorizontalMaxSpeedMultiplier());
        root.addProperty(GAS_CLOUD_HORIZONTAL_TARGET_SPEED_MULTIPLIER, values.gasCloudHorizontalTargetSpeedMultiplier());
        root.addProperty(GAS_CLOUD_HORIZONTAL_CONVERGENCE_MULTIPLIER, values.gasCloudHorizontalConvergenceMultiplier());
        return root;
    }
}
