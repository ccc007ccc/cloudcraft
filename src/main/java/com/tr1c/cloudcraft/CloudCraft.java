package com.tr1c.cloudcraft;

import com.tr1c.cloudcraft.block.ModBlocks;
import com.tr1c.cloudcraft.item.ModCreativeModeTabs;
import com.tr1c.cloudcraft.item.ModItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// 这里的值应该与 META-INF/neoforge.mods.toml 文件中的条目匹配
@Mod(CloudCraft.MOD_ID)
public class CloudCraft {
    // 在一个公共的地方定义 mod ID，以便所有地方引用
    public static final String MOD_ID = "cloudcraft";
    // 直接引用 slf4j 日志记录器
    public static final Logger LOGGER = LogUtils.getLogger();

    // mod 类的构造函数是加载 mod 时运行的第一个代码。
    // FML 会自动识别一些参数类型，如 IEventBus 或 ModContainer，并自动传入它们。
    public CloudCraft(IEventBus modEventBus, ModContainer modContainer) {
        // 注册 commonSetup 方法以进行 mod 加载
        modEventBus.addListener(this::commonSetup);

        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModBlocks.register(modEventBus);

        // 为我们感兴趣的服务器和其他游戏事件注册我们自己。
        // 请注意，仅当我们希望 *这个* 类 (CloudCraft) 直接响应事件时，这才是必需的。
        // 如果此类中没有 @SubscribeEvent 注解的函数（如下面的 onServerStarting()），请不要添加此行。
        NeoForge.EVENT_BUS.register(this);

        // 将物品注册到创造模式物品栏
        modEventBus.addListener(this::addCreative);

        // 注册我们 mod 的 ModConfigSpec，以便 FML 可以为我们创建和加载配置文件
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // 一些通用的设置代码
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // 将示例方块物品添加到建筑方块物品栏
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    // 你可以使用 SubscribeEvent 让事件总线发现要调用的方法
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // 服务器启动时执行某些操作
        LOGGER.info("HELLO from server starting");
    }
}
