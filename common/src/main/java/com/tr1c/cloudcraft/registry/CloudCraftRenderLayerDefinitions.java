package com.tr1c.cloudcraft.registry;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class CloudCraftRenderLayerDefinitions {
    private static final List<String> TRANSLUCENT_BLOCK_IDS = List.of(
            ModIds.CUMULUS_CLOUD_BLOCK_GAS,
            ModIds.GAS_STATE_CONVERTER
    );

    private CloudCraftRenderLayerDefinitions() {
    }

    public static List<String> translucentBlockIds() {
        return TRANSLUCENT_BLOCK_IDS;
    }

    public static <T> void forEachTranslucentBlock(Function<String, T> resolver, Consumer<T> consumer) {
        TRANSLUCENT_BLOCK_IDS.forEach(id -> consumer.accept(resolver.apply(id)));
    }
}
