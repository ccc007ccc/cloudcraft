package com.tr1c.cloudcraft.cloudtech;

import com.tr1c.cloudcraft.registry.ModIds;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record JetpackInputPacket(boolean thrusting) implements CustomPacketPayload {
    public static final Type<JetpackInputPacket> TYPE = new Type<>(ModIds.id("jetpack_input"));
    public static final StreamCodec<RegistryFriendlyByteBuf, JetpackInputPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            JetpackInputPacket::thrusting,
            JetpackInputPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
