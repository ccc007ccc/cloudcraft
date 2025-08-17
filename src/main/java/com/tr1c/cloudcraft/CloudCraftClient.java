package com.tr1c.cloudcraft;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// 此类不会在专用服务器上加载。从这里访问客户端代码是安全的。
@Mod(value = CloudCraft.MOD_ID, dist = Dist.CLIENT)
// 您可以使用 EventBusSubscriber 自动注册类中使用 @SubscribeEvent 注解的所有静态方法
@EventBusSubscriber(modid = CloudCraft.MOD_ID, value = Dist.CLIENT)
public class CloudCraftClient {
    public CloudCraftClient(ModContainer container) {
        // 允许 NeoForge 为此模组的配置创建一个配置屏幕。
        // 配置屏幕通过进入“Mods”屏幕 > 点击您的模组 > 点击“配置”来访问。
        // 不要忘记将您的配置选项的翻译添加到 en_us.json 文件中。
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // 一些客户端设置代码
        CloudCraft.LOGGER.info("HELLO FROM CLIENT SETUP");
        CloudCraft.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
