package com.nerdsoft.mods.nerdsoftkitchen.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class LoopingBlockSoundInstance<T extends BlockEntity> extends AbstractTickableSoundInstance {

    private final Level level;
    private final BlockPos pos;
    private final Class<T> blockEntityType;
    private final Predicate<T> shouldKeepPlaying;

    public LoopingBlockSoundInstance(Supplier<SoundEvent> sound, Level level, BlockPos pos, double yOffset,
                                     float volume, Class<T> blockEntityType, Predicate<T> shouldKeepPlaying) {
        super(sound.get(), SoundSource.BLOCKS, level.random);
        this.level = level;
        this.pos = pos;
        this.blockEntityType = blockEntityType;
        this.shouldKeepPlaying = shouldKeepPlaying;
        this.x = pos.getX() + 0.5D;
        this.y = pos.getY() + yOffset;
        this.z = pos.getZ() + 0.5D;
        this.looping = true;
        this.delay = 0;
        this.volume = volume;
    }

    @Override
    public void tick() {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!blockEntityType.isInstance(blockEntity) || !shouldKeepPlaying.test(blockEntityType.cast(blockEntity))) {
            this.stop();
        }
    }
}
