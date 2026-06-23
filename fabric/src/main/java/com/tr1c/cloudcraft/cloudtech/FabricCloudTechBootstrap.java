package com.tr1c.cloudcraft.cloudtech;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public final class FabricCloudTechBootstrap {
    private FabricCloudTechBootstrap() {
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(JetpackInputPacket.TYPE, JetpackInputPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(BackTankSyncPacket.TYPE, BackTankSyncPacket.STREAM_CODEC);
        BackTankService.registerSynchronizer(FabricCloudTechBootstrap::syncBackTank);
        ServerPlayNetworking.registerGlobalReceiver(JetpackInputPacket.TYPE, (payload, context) ->
                CloudTechPlayerState.setThrusting(context.player(), payload.thrusting()));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            CloudTechPlayerState.setThrusting(handler.player, false);
            syncBackTank(handler.player);
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                CloudTechPlayerState.setThrusting(handler.player, false));
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            BackTankService.set(newPlayer, BackTankService.get(oldPlayer));
            CloudTechPlayerState.setThrusting(newPlayer, false);
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                JetpackRuntime.tick(player);
            }
        });
    }

    private static void syncBackTank(ServerPlayer player) {
        ServerPlayNetworking.send(player, new BackTankSyncPacket(BackTankService.get(player)));
    }
}
