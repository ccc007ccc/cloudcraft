package com.tr1c.cloudcraft.datagen;

import com.mojang.serialization.JsonOps;
import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.registry.CloudCraftRegistryDefinitions;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public final class CloudCraftLootTableProvider implements DataProvider {
    private final PackOutput.PathProvider lootTables;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public CloudCraftLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.lootTables = output.createPathProvider(PackOutput.Target.DATA_PACK, "loot_table");
        this.registries = registries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return registries.thenCompose(provider -> {
            List<CompletableFuture<?>> futures = new java.util.ArrayList<>();
            new CloudCraftBlockLootSubProvider(provider).generate(save(output, futures, provider));
            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return CloudCraft.MOD_ID + " loot table provider";
    }

    private BiConsumer<ResourceKey<LootTable>, LootTable.Builder> save(CachedOutput output, List<CompletableFuture<?>> futures, HolderLookup.Provider provider) {
        return (key, builder) -> futures.add(saveLootTable(output, provider, key, builder));
    }

    private CompletableFuture<?> saveLootTable(CachedOutput output, HolderLookup.Provider provider, ResourceKey<LootTable> key, LootTable.Builder builder) {
        return DataProvider.saveStable(output, LootTable.DIRECT_CODEC.encodeStart(provider.createSerializationContext(JsonOps.INSTANCE), builder.build()).getOrThrow(), lootTables.json(key.identifier()));
    }

    private static Block block(String id) {
        return BuiltInRegistries.BLOCK.getValue(ModIds.id(id));
    }

    private static ItemLike item(String id) {
        return BuiltInRegistries.ITEM.getValue(ModIds.id(id));
    }

    private static final class CloudCraftBlockLootSubProvider extends BlockLootSubProvider {
        private final HolderLookup.Provider registries;

        private CloudCraftBlockLootSubProvider(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
            this.registries = registries;
        }

        @Override
        public void generate() {
            HolderLookup.RegistryLookup<net.minecraft.world.item.enchantment.Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
            addCloudBlockLoot(enchantments, ModIds.CUMULUS_CLOUD_BLOCK);
            add(block(ModIds.CUMULUS_CLOUD_BLOCK_GAS), noDrop());
            addCloudBlockLoot(enchantments, ModIds.STRATUS_CLOUD_BLOCK);
            add(block(ModIds.STRATUS_CLOUD_BLOCK_GAS), noDrop());
            addCloudBlockLoot(enchantments, ModIds.CIRRUS_CLOUD_BLOCK);
            add(block(ModIds.CIRRUS_CLOUD_BLOCK_GAS), noDrop());
            addCloudBlockLoot(enchantments, ModIds.ALTOSTRATUS_CLOUD_BLOCK);
            add(block(ModIds.ALTOSTRATUS_CLOUD_BLOCK_GAS), noDrop());
            addCloudBlockLoot(enchantments, ModIds.NIMBOSTRATUS_CLOUD_BLOCK);
            add(block(ModIds.NIMBOSTRATUS_CLOUD_BLOCK_GAS), noDrop());
            addCloudBlockLoot(enchantments, ModIds.CUMULONIMBUS_CLOUD_BLOCK);
            add(block(ModIds.CUMULONIMBUS_CLOUD_BLOCK_GAS), noDrop());
            dropSelf(block(ModIds.GAS_STATE_CONVERTER));
        }

        private void addCloudBlockLoot(HolderLookup.RegistryLookup<net.minecraft.world.item.enchantment.Enchantment> enchantments, String id) {
            add(block(id), createSilkTouchDispatchTable(
                    block(id),
                    LootItem.lootTableItem(item(ModIds.CUMULUS_CLOUD_FRAGMENT))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                            .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE), 1))
                            .apply(LimitCount.limitCount(IntRange.range(1, 4)))
                            .apply(ApplyExplosionDecay.explosionDecay())));
        }

        protected Iterable<Block> getKnownBlocks() {
            return CloudCraftRegistryDefinitions.blockIds().stream()
                    .map(CloudCraftLootTableProvider::block)
                    .toList();
        }
    }
}
