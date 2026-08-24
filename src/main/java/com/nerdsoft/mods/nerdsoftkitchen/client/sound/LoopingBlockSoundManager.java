package com.nerdsoft.mods.nerdsoftkitchen.client.sound;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Predicate;

abstract class LoopingBlockSoundManager<T extends BlockEntity, S extends AbstractTickableSoundInstance> {

    private final Long2ObjectMap<S> activeSounds = new Long2ObjectOpenHashMap<>();
    private final Predicate<T> shouldPlay;
    private final BiFunction<Level, BlockPos, S> soundFactory;

    protected LoopingBlockSoundManager(Predicate<T> shouldPlay, BiFunction<Level, BlockPos, S> soundFactory) {
        this.shouldPlay = shouldPlay;
        this.soundFactory = soundFactory;
    }

    private static SoundManager soundManager() {
        return Minecraft.getInstance().getSoundManager();
    }

    public final void update(Level level, BlockPos pos, BlockState state, T entity) {
        boolean wants = shouldPlay.test(entity);
        long key = pos.asLong();
        S existing = activeSounds.get(key);

        if (wants) {
            if (existing == null || !soundManager().isActive(existing)) {
                S newSound = soundFactory.apply(level, pos);
                activeSounds.put(key, newSound);
                soundManager().play(newSound);
            }
        } else if (existing != null) {
            stop(pos);
        }
    }

    public final void stop(BlockPos pos) {
        S existing = activeSounds.remove(pos.asLong());
        if (existing != null) {
            soundManager().stop(existing);
        }
    }

    public final void clear() {
        activeSounds.clear();
    }

    public final void stopSoundsInChunk(ChunkPos chunkPos) {
        if (activeSounds.isEmpty()) {
            return;
        }
        SoundManager sounds = soundManager();
        Iterator<Long2ObjectMap.Entry<S>> iterator = activeSounds.long2ObjectEntrySet().iterator();
        while (iterator.hasNext()) {
            Long2ObjectMap.Entry<S> entry = iterator.next();
            long packedPos = entry.getLongKey();
            if ((BlockPos.getX(packedPos) >> 4) == chunkPos.x && (BlockPos.getZ(packedPos) >> 4) == chunkPos.z) {
                sounds.stop(entry.getValue());
                iterator.remove();
            }
        }
    }
}
