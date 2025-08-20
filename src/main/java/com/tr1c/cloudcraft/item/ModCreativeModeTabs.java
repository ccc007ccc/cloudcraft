package com.tr1c.cloudcraft.item;

import com.tr1c.cloudcraft.CloudCraft;
import com.tr1c.cloudcraft.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    // 创建一个延迟注册器，用于注册创造模式物品栏
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CloudCraft.MOD_ID);

    // 注册云物品栏
    public static final Supplier<CreativeModeTab> CLOUD_TAB =
            CREATIVE_MODE_TABS.register("cloud_tab",() -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.CLOUD.get())) // 设置物品栏图标
                    .title(Component.translatable("itemGroup.cloudcraft")) // 设置物品栏标题
                    .displayItems((parameters, output) -> { // 设置物品栏中显示的物品
                        output.accept(ModItems.CLOUD); // 添加云物品
                        output.accept(ModItems.CUMULUS_CLOUD_FRAGMENT); // 添加云碎片
                        output.accept(ModBlocks.CUMULUS_CLOUD_BLOCK); // 添加云方块
                        output.accept(ModBlocks.CLOUD_BLOCK_GAS); // 添加云气体方块
                        output.accept(ModBlocks.GAS_STATE_CONVERTER); // 添加气态转换器
                    })
                    .build());


    // 在事件总线上注册创造模式物品栏
    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}