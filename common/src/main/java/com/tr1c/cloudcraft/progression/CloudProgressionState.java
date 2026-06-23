package com.tr1c.cloudcraft.progression;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class CloudProgressionState extends SavedData {
    private static final String FILE_ID = "cloud_progression_state";
    private static final String PLAYERS_KEY = "players";
    private static final String DISCOVERED_TIERS_KEY = "discovered_tiers";
    private static final String UNLOCKED_UPGRADES_KEY = "unlocked_upgrades";
    private static final Codec<UUID> UUID_CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);
    private static final Codec<PlayerProgress> PLAYER_PROGRESS_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().optionalFieldOf(DISCOVERED_TIERS_KEY, List.of()).forGetter(PlayerProgress::discoveredTiers),
            Codec.STRING.listOf().optionalFieldOf(UNLOCKED_UPGRADES_KEY, List.of()).forGetter(PlayerProgress::unlockedUpgrades)
    ).apply(instance, PlayerProgress::new));
    private static final Codec<CloudProgressionState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(UUID_CODEC, PLAYER_PROGRESS_CODEC)
                    .optionalFieldOf(PLAYERS_KEY, Map.of())
                    .forGetter(state -> state.progressByPlayer)
    ).apply(instance, CloudProgressionState::new));
    private static final SavedDataType<CloudProgressionState> TYPE = new SavedDataType<>(
            FILE_ID,
            CloudProgressionState::new,
            CODEC,
            DataFixTypes.PLAYER);

    private final Map<UUID, PlayerProgress> progressByPlayer;

    private CloudProgressionState() {
        this(Map.of());
    }

    private CloudProgressionState(Map<UUID, PlayerProgress> progressByPlayer) {
        this.progressByPlayer = new HashMap<>(progressByPlayer);
    }

    public static CloudProgressionState get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(TYPE);
    }

    public static void discoverTier(Player player, CloudTier tier) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        CloudProgressionState state = get(serverPlayer.level());
        PlayerProgress current = state.progress(serverPlayer);
        if (current.discoveredTiers().contains(tier.id())) {
            return;
        }
        List<String> discovered = new ArrayList<>(new LinkedHashSet<>(current.discoveredTiers()));
        discovered.add(tier.id());
        state.progressByPlayer.put(serverPlayer.getUUID(), new PlayerProgress(List.copyOf(discovered), current.unlockedUpgrades()));
        state.setDirty();
    }

    public static void unlockUpgrade(Player player, String upgradeId) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        CloudProgressionState state = get(serverPlayer.level());
        PlayerProgress current = state.progress(serverPlayer);
        if (current.unlockedUpgrades().contains(upgradeId)) {
            return;
        }
        List<String> unlocked = new ArrayList<>(new LinkedHashSet<>(current.unlockedUpgrades()));
        unlocked.add(upgradeId);
        state.progressByPlayer.put(serverPlayer.getUUID(), new PlayerProgress(current.discoveredTiers(), List.copyOf(unlocked)));
        state.setDirty();
    }

    public static boolean hasDiscoveredTier(Player player, CloudTier tier) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return false;
        }
        return get(serverPlayer.level()).progress(serverPlayer).discoveredTiers().contains(tier.id());
    }

    public static boolean hasUnlockedUpgrade(Player player, String upgradeId) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return false;
        }
        return get(serverPlayer.level()).progress(serverPlayer).unlockedUpgrades().contains(upgradeId);
    }

    private PlayerProgress progress(ServerPlayer player) {
        return progressByPlayer.computeIfAbsent(player.getUUID(), ignored -> new PlayerProgress(List.of(), List.of()));
    }

    private record PlayerProgress(List<String> discoveredTiers, List<String> unlockedUpgrades) {
    }
}
