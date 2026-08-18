package com.nerdsoft.mods.nerdsoftkitchen.perf;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record TileStateSyncPayload(long packedPos, short state) implements CustomPacketPayload {

    public static final Type<TileStateSyncPayload> TYPE =
            CustomPacketPayload.createType("nerdsoftkitchen:tile_state_sync");

    public static final StreamCodec<RegistryFriendlyByteBuf, TileStateSyncPayload> STREAM_CODEC =
            StreamCodec.ofMember(TileStateSyncPayload::write, TileStateSyncPayload::read);

    private static void write(TileStateSyncPayload payload, RegistryFriendlyByteBuf buf) {
        buf.writeLong(payload.packedPos);
        buf.writeShort(payload.state);
    }

    private static TileStateSyncPayload read(RegistryFriendlyByteBuf buf) {
        long pos = buf.readLong();
        short state = (short) buf.readShort();
        return new TileStateSyncPayload(pos, state);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
