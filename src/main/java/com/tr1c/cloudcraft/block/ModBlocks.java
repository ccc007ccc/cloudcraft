package com.tr1c.cloudcraft.block;

import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.block.custom.cloud_block.CloudBlock;
import com.tr1c.cloudcraft.block.custom.cloud_block.CloudBlockGas;
import com.tr1c.cloudcraft.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    // 创建一个延迟注册器，用于注册方块
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(CloudCraft.MOD_ID);

    // 注册云方块
    public static final DeferredBlock<Block> CLOUD_BLOCK =
            registerBlocks("cloud_block", () -> new CloudBlock(
                    BlockBehaviour.Properties.of()
                    .destroyTime(0.2F) // 设置破坏时间
                    .explosionResistance(0.1F) // 设置爆炸抗性
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.parse(CloudCraft.MOD_ID + ":cloud_block")))
                    .sound(SoundType.SNOW)
            ));

    // 注册气态云方块
    public static final DeferredBlock<CloudBlockGas> CLOUD_BLOCK_GAS =
            registerBlocks("cloud_block_gas", () -> new CloudBlockGas(
                    BlockBehaviour.Properties.of()
                            .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.parse(CloudCraft.MOD_ID + ":cloud_block_gas"))),
                    CLOUD_BLOCK.get().defaultBlockState() // 传入对应的固态云方块状态
            ));

    // 注册气态转换器
    public static final DeferredBlock<Block> GAS_STATE_CONVERTER =
            registerBlocks("gas_state_converter", () -> new RotatableBlock(
                    BlockBehaviour.Properties.of()
                    .destroyTime(1.0F)
                    .explosionResistance(6.0F)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.parse(CloudCraft.MOD_ID + ":gas_state_converter"))),
                    false // false = 只水平旋转
            ));

    // 为方块注册对应的物品
    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse(CloudCraft.MOD_ID + ":" + name)))));
    }

    // 注册方块及其对应的物品
    private static <T extends Block> DeferredBlock<T> registerBlocks(String name, Supplier<T> block) {
        DeferredBlock<T> blocks = BLOCKS.register(name, block);
        registerBlockItem(name, blocks);
        return blocks;
    }

    // 在事件总线上注册方块
    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}