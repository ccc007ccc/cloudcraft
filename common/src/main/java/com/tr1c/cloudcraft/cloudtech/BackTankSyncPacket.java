package com.tr1c.cloudcraft.cloudtech;

import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

public record BackTankSyncPacket(ItemStack stack) implements CustomPacketPayload {
    public static final Type<BackTankSyncPacket> TYPE = new Type<>(ModIds.id("back_tank_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BackTankSyncPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC,
            BackTankSyncPacket::stack,
            BackTankSyncPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
