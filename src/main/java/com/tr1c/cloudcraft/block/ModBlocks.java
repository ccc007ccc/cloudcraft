package com.tr1c.cloudcraft.block;

import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(CloudCraft.MOD_ID);

    public static final DeferredBlock<Block> CLOUD_BLOCK =
            registerBlocks("cloud_block", () -> new Block(BlockBehaviour.Properties.of()
                    .destroyTime(0.2F)
                    .explosionResistance(0.1F)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.parse(CloudCraft.MOD_ID + ":cloud_block")))));


    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse(CloudCraft.MOD_ID + ":" + name)))));
    }

    private static <T extends Block> DeferredBlock<T> registerBlocks(String name, Supplier<T> block) {
        DeferredBlock<T> blocks = BLOCKS.register(name, block);
        registerBlockItem(name, blocks);
        return blocks;
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
