package com.nerdsoft.mods.nerdsoftkitchen.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * Data component attached to a Skillet {@link net.minecraft.world.item.ItemStack} that was
 * picked up while lit/cooking ("Hot Skillet"). {@code expiresAtGameTime} is an absolute
 * {@link net.minecraft.world.level.Level#getGameTime()} value; the item is considered hot for
 * as long as the current game time is before this value.
 * <p>
 * Storing an absolute tick (rather than a remaining-duration counter that would need
 * decrementing every tick) keeps this component free of any server-side ticking cost - it is
 * pure data, checked only when it matters (on attack, on tooltip render).
 */
public record SkilletHotState(long expiresAtGameTime) {

    public static final Codec<SkilletHotState> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.LONG.fieldOf("expires_at").forGetter(SkilletHotState::expiresAtGameTime)
            ).apply(instance, SkilletHotState::new));

    public static final StreamCodec<ByteBuf, SkilletHotState> STREAM_CODEC =
            ByteBufCodecs.VAR_LONG.map(SkilletHotState::new, SkilletHotState::expiresAtGameTime);

    public boolean isActiveAt(long gameTime) {
        return gameTime < expiresAtGameTime;
    }
}