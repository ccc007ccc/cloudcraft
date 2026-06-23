package com.tr1c.cloudcraft.cloudtech;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.tr1c.cloudcraft.CloudCraft.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public final class NeoForgeCloudTechBootstrap {
    private NeoForgeCloudTechBootstrap() {
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(JetpackInputPacket.TYPE, JetpackInputPacket.STREAM_CODEC, NeoForgeCloudTechBootstrap::handleJetpackInput);
        registrar.playToClient(BackTankSyncPacket.TYPE, BackTankSyncPacket.STREAM_CODEC);
        BackTankService.registerSynchronizer(NeoForgeCloudTechBootstrap::syncBackTank);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            JetpackRuntime.tick(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer newPlayer && event.getOriginal() instanceof ServerPlayer oldPlayer) {
            BackTankService.set(newPlayer, BackTankService.get(oldPlayer));
            CloudTechPlayerState.setThrusting(newPlayer, false);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CloudTechPlayerState.setThrusting(player, false);
            syncBackTank(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CloudTechPlayerState.setThrusting(player, false);
        }
    }

    private static void handleJetpackInput(JetpackInputPacket payload, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) {
            CloudTechPlayerState.setThrusting(player, payload.thrusting());
        }
    }

    private static void syncBackTank(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new BackTankSyncPacket(BackTankService.get(player)));
    }
}
