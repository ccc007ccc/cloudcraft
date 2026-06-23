package com.tr1c.cloudcraft.cloudtech;

import com.tr1c.cloudcraft.cloudtech.client.BackTankClientState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public final class BackTankService {
    private static Synchronizer synchronizer = player -> {
    };

    private BackTankService() {
    }

    public static ItemStack get(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return CloudTechPlayerState.getBackTank(serverPlayer);
        }
        return BackTankClientState.getBackTank();
    }

    public static void set(Player player, ItemStack stack) {
        ItemStack normalized = normalize(stack);
        if (player instanceof ServerPlayer serverPlayer) {
            if (ItemStack.matches(CloudTechPlayerState.getBackTank(serverPlayer), normalized)) {
                return;
            }
            CloudTechPlayerState.setBackTank(serverPlayer, normalized);
            synchronizer.sync(serverPlayer);
            return;
        }
        BackTankClientState.setBackTank(normalized);
    }

    public static void clear(Player player) {
        set(player, ItemStack.EMPTY);
    }

    public static void sync(ServerPlayer player) {
        synchronizer.sync(player);
    }

    public static void registerSynchronizer(Synchronizer synchronizer) {
        BackTankService.synchronizer = Objects.requireNonNull(synchronizer);
    }

    private static ItemStack normalize(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack normalized = stack.copy();
        normalized.setCount(1);
        return normalized;
    }

    @FunctionalInterface
    public interface Synchronizer {
        void sync(ServerPlayer player);
    }
}
