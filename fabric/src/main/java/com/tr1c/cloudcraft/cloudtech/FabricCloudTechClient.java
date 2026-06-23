package com.tr1c.cloudcraft.cloudtech;

import com.tr1c.cloudcraft.cloudtech.client.BackTankClientState;
import com.tr1c.cloudcraft.cloudtech.client.JetpackHudRenderer;
import com.tr1c.cloudcraft.registry.ModIds;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public final class FabricCloudTechClient {
    private static final KeyMapping.Category KEY_CATEGORY = KeyMapping.Category.register(CloudTechItems.jetpackId());
    private static final KeyMapping THRUST_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.cloudcraft.jetpack_thrust",
            GLFW.GLFW_KEY_SPACE,
            KEY_CATEGORY));
    private static boolean thrusting;

    private FabricCloudTechClient() {
    }

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(BackTankSyncPacket.TYPE, (payload, context) ->
                BackTankClientState.setBackTank(payload.stack()));
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, ModIds.id("jetpack_fuel"), (guiGraphics, deltaTracker) ->
                JetpackHudRenderer.render(guiGraphics));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            thrusting = false;
            BackTankClientState.clear();
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean current = THRUST_KEY.isDown();
            if (current != thrusting && client.player != null) {
                thrusting = current;
                ClientPlayNetworking.send(new JetpackInputPacket(current));
            }
        });
    }
}
