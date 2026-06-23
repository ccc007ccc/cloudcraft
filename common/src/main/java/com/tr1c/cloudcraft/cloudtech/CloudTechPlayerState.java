package com.tr1c.cloudcraft.cloudtech;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tr1c.cloudcraft.cloudtech.client.BackTankClientState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CloudTechPlayerState extends SavedData {
    private static final String FILE_ID = "cloudtech_player_state";
    private static final String PLAYERS_KEY = "players";
    private static final String BACK_TANK_KEY = "back_tank";
    private static final Set<UUID> THRUSTING_PLAYERS = ConcurrentHashMap.newKeySet();
    private static final Codec<UUID> UUID_CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);
    private static final Codec<PlayerState> PLAYER_STATE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.OPTIONAL_CODEC.optionalFieldOf(BACK_TANK_KEY, ItemStack.EMPTY).forGetter(PlayerState::backTank)
    ).apply(instance, PlayerState::new));
    private static final Codec<CloudTechPlayerState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(UUID_CODEC, PLAYER_STATE_CODEC)
                    .optionalFieldOf(PLAYERS_KEY, Map.of())
                    .forGetter(state -> state.states)
    ).apply(instance, CloudTechPlayerState::new));
    private static final SavedDataType<CloudTechPlayerState> TYPE = new SavedDataType<>(
            FILE_ID,
            CloudTechPlayerState::new,
            CODEC,
            DataFixTypes.PLAYER);

    private final Map<UUID, PlayerState> states;

    private CloudTechPlayerState() {
        this(Map.of());
    }

    private CloudTechPlayerState(Map<UUID, PlayerState> states) {
        this.states = new HashMap<>(states);
    }

    public static CloudTechPlayerState get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(TYPE);
    }

    public static ItemStack getBackTank(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return get(serverPlayer.level()).state(serverPlayer).backTank().copy();
        }
        return BackTankClientState.getBackTank();
    }

    public static void setBackTank(Player player, ItemStack stack) {
        if (player instanceof ServerPlayer serverPlayer) {
            CloudTechPlayerState data = get(serverPlayer.level());
            data.states.put(serverPlayer.getUUID(), new PlayerState(stack.copy()));
            data.setDirty();
            return;
        }
        BackTankClientState.setBackTank(stack);
    }

    public static boolean isThrusting(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return false;
        }
        return THRUSTING_PLAYERS.contains(serverPlayer.getUUID());
    }

    public static void setThrusting(Player player, boolean thrusting) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        if (thrusting) {
            THRUSTING_PLAYERS.add(serverPlayer.getUUID());
        } else {
            THRUSTING_PLAYERS.remove(serverPlayer.getUUID());
        }
    }

    private PlayerState state(ServerPlayer player) {
        return states.computeIfAbsent(player.getUUID(), ignored -> new PlayerState(ItemStack.EMPTY));
    }

    private record PlayerState(ItemStack backTank) {
    }
}
