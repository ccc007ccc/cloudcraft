package com.tr1c.cloudcraft.cloudtech;

import com.tr1c.cloudcraft.progression.CloudProgressionRules;
import com.tr1c.cloudcraft.progression.CloudProgressionState;
import com.tr1c.cloudcraft.visual.CloudFeedbackRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class JetpackRuntime {
    private static final double HOVER_FALL_SPEED = -0.08;
    private static final double HOVER_HORIZONTAL_DRAG = 0.96;

    private JetpackRuntime() {
    }

    public static void tick(ServerPlayer player) {
        ItemStack jetpack = BackTankService.get(player);
        if (!CloudTechItems.isJetpack(jetpack)) {
            CloudTechPlayerState.setThrusting(player, false);
            return;
        }

        boolean freeFlight = player.isCreative() || player.isSpectator();
        unlockProgressionItems(player, jetpack);
        rechargeFromCloud(player, jetpack);
        if (CloudTechPlayerState.isThrusting(player)) {
            thrust(player, jetpack, freeFlight);
            return;
        }
        if (!freeFlight) {
            hover(player, jetpack);
        }
    }

    private static void unlockProgressionItems(ServerPlayer player, ItemStack jetpack) {
        unlockIfPresent(player, CloudTechItems.jetpackId(jetpack));
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                unlockIfPresent(player, stack.getItem().builtInRegistryHolder().key().identifier().getPath());
            }
        }
    }

    private static void unlockIfPresent(ServerPlayer player, String itemId) {
        if (CloudProgressionRules.unlockIds().contains(itemId)) {
            CloudProgressionState.unlockUpgrade(player, itemId);
        }
    }

    private static void rechargeFromCloud(ServerPlayer player, ItemStack jetpack) {
        CloudPressureProfile profile = rechargeProfileAt(player.level(), player.blockPosition());
        if (profile == null) {
            return;
        }
        String jetpackId = CloudTechItems.jetpackId(jetpack);
        int recharge = CompressedAirRules.rechargeRate(profile, jetpackId);
        if (recharge <= 0) {
            return;
        }
        CloudProgressionState.discoverTier(player, profile.tier());
        JetpackStackState.setPressure(jetpack, CompressedAirRules.add(
                JetpackStackState.getPressure(jetpack),
                recharge,
                CloudProgressionRules.maxPressure(jetpackId)));
        BackTankService.set(player, jetpack);
    }

    private static void thrust(ServerPlayer player, ItemStack jetpack, boolean freeFlight) {
        String jetpackId = CloudTechItems.jetpackId(jetpack);
        int pressure = JetpackStackState.getPressure(jetpack);
        if (!freeFlight && !CompressedAirRules.canThrust(pressure, jetpackId)) {
            return;
        }
        if (!freeFlight) {
            JetpackStackState.setPressure(jetpack, CompressedAirRules.consumeThrust(pressure, jetpackId));
            BackTankService.set(player, jetpack);
        }

        Vec3 movement = applyThrustMovement(player.getDeltaMovement(), player.getLastClientMoveIntent(), jetpackId);
        player.setDeltaMovement(movement);
        player.hurtMarked = true;
        player.fallDistance = 0.0F;
        playThrustFeedback(player, jetpackId);
    }

    private static void playThrustFeedback(ServerPlayer player, String jetpackId) {
        if (!CloudFeedbackRules.shouldPlayJetpackThrustSound(player.tickCount)) {
            return;
        }
        player.level().playSound(
                null,
                player.blockPosition(),
                SoundEvents.WIND_CHARGE_THROW,
                SoundSource.PLAYERS,
                0.28F,
                CloudFeedbackRules.jetpackThrustPitch(jetpackId));
    }

    static Vec3 applyThrustMovement(Vec3 movement, Vec3 moveIntent, String jetpackId) {
        JetpackFlightRules.Movement applied = JetpackFlightRules.applyThrustMovement(
                movement.x,
                movement.y,
                movement.z,
                moveIntent.x,
                moveIntent.z,
                CloudProgressionRules.thrustVerticalSpeed(jetpackId),
                CloudProgressionRules.thrustHorizontalAcceleration(jetpackId),
                CloudProgressionRules.thrustHorizontalMaxSpeed(jetpackId));
        return new Vec3(applied.x(), applied.y(), applied.z());
    }

    private static void hover(ServerPlayer player, ItemStack jetpack) {
        if (!shouldHover(player)) {
            return;
        }
        String jetpackId = CloudTechItems.jetpackId(jetpack);
        int pressure = JetpackStackState.getPressure(jetpack);
        if (pressure < CloudProgressionRules.hoverCost(jetpackId)) {
            return;
        }

        JetpackStackState.setPressure(jetpack, CompressedAirRules.consumeHover(pressure, jetpackId));
        BackTankService.set(player, jetpack);

        var movement = player.getDeltaMovement();
        player.setDeltaMovement(movement.x * HOVER_HORIZONTAL_DRAG, Math.max(movement.y, HOVER_FALL_SPEED), movement.z * HOVER_HORIZONTAL_DRAG);
        player.hurtMarked = true;
        player.fallDistance = 0.0F;
    }

    private static boolean shouldHover(ServerPlayer player) {
        if (player.onGround() || player.onClimbable() || player.isInWater() || player.isInLava()) {
            return false;
        }
        return player.getDeltaMovement().y < 0.0D;
    }

    public static CloudPressureProfile rechargeProfileAt(Level level, BlockPos center) {
        BlockPos[] positions = new BlockPos[]{center, center.above(), center.below()};
        for (BlockPos pos : positions) {
            BlockState state = level.getBlockState(pos);
            String path = state.getBlockHolder().unwrapKey().map(key -> key.identifier().getPath()).orElse("");
            CloudPressureProfile profile = CloudPressureProfile.find(path);
            if (profile != null) {
                return profile;
            }
        }
        return null;
    }
}
