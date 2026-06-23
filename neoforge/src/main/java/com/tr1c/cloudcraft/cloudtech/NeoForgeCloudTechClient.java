package com.tr1c.cloudcraft.cloudtech;

import com.mojang.blaze3d.platform.InputConstants;
import com.tr1c.cloudcraft.cloudtech.client.BackTankClientState;
import com.tr1c.cloudcraft.cloudtech.client.JetpackHudRenderer;
import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.lwjgl.glfw.GLFW;

import static com.tr1c.cloudcraft.CloudCraft.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public final class NeoForgeCloudTechClient {
    private static final KeyMapping.Category KEY_CATEGORY = KeyMapping.Category.register(CloudTechItems.jetpackId());
    private static final KeyMapping THRUST_KEY = new KeyMapping(
            "key.cloudcraft.jetpack_thrust",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_SPACE,
            KEY_CATEGORY);
    private static boolean thrusting;

    private NeoForgeCloudTechClient() {
    }

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(THRUST_KEY);
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CHAT, ModIds.id("jetpack_fuel"), (guiGraphics, deltaTracker) ->
                JetpackHudRenderer.render(guiGraphics));
    }

    @SubscribeEvent
    public static void registerClientPayloads(RegisterClientPayloadHandlersEvent event) {
        event.register(BackTankSyncPacket.TYPE, NeoForgeCloudTechClient::handleBackTankSync);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        boolean current = THRUST_KEY.isDown();
        if (current != thrusting) {
            thrusting = current;
            ClientPacketDistributor.sendToServer(new JetpackInputPacket(current));
        }
    }

    @SubscribeEvent
    public static void onDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        thrusting = false;
        BackTankClientState.clear();
    }

    private static void handleBackTankSync(BackTankSyncPacket payload, IPayloadContext context) {
        BackTankClientState.setBackTank(payload.stack());
    }
}
