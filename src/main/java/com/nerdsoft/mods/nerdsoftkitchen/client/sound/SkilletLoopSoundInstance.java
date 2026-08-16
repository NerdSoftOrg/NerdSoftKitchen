package com.nerdsoft.mods.nerdsoftkitchen.client.sound;

import com.nerdsoft.mods.nerdsoftkitchen.block.SkilletBlock;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.SkilletBlockEntity;
import com.nerdsoft.mods.nerdsoftkitchen.registry.sound.ModSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SkilletLoopSoundInstance extends AbstractTickableSoundInstance {

    private static final float LOOP_VOLUME = 0.4F;

    private final Level level;
    private final BlockPos pos;

    public SkilletLoopSoundInstance(Level level, BlockPos pos) {
        super(ModSounds.SKILLET_SIZZLE_LOOP.get(), SoundSource.BLOCKS, level.random);
        this.level = level;
        this.pos = pos;
        this.x = pos.getX() + 0.5D;
        this.y = pos.getY() + 0.25D;
        this.z = pos.getZ() + 0.5D;
        this.looping = true;
        this.delay = 0;
        this.volume = LOOP_VOLUME;
    }

    @Override
    public void tick() {
        if (!(this.level.getBlockEntity(this.pos) instanceof SkilletBlockEntity skillet)) {
            this.stop();
            return;
        }

        BlockState state = skillet.getBlockState();
        if (!state.getValue(SkilletBlock.LIT) || !skillet.isCooking()) {
            this.stop();
        }
    }
}