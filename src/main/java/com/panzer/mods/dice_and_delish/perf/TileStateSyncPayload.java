package com.panzer.mods.dice_and_delish.perf;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record TileStateSyncPayload(long packedPos, short state) implements CustomPacketPayload {

    public static final Type<TileStateSyncPayload> TYPE =
            CustomPacketPayload.createType(DiceAndDelish.MOD_ID + ":tile_state_sync");

    public static final StreamCodec<RegistryFriendlyByteBuf, TileStateSyncPayload> STREAM_CODEC =
            StreamCodec.ofMember(TileStateSyncPayload::write, TileStateSyncPayload::read);

    private static void write(TileStateSyncPayload payload, RegistryFriendlyByteBuf buf) {
        buf.writeLong(payload.packedPos);
        buf.writeShort(payload.state);
    }

    private static TileStateSyncPayload read(RegistryFriendlyByteBuf buf) {
        long pos = buf.readLong();
        short state = buf.readShort();
        return new TileStateSyncPayload(pos, state);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
