package com.nerdsoft.mods.nerdsoftkitchen.client.sound;

import com.nerdsoft.mods.nerdsoftkitchen.blockentity.GrillTableBlockEntity;
import com.nerdsoft.mods.nerdsoftkitchen.registry.ModSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class GrillLoopSoundInstance extends AbstractTickableSoundInstance {
    private final Level level;
    private final BlockPos pos;

    public GrillLoopSoundInstance(Level level, BlockPos pos) {
        super(ModSounds.GRILL_GRILLING.get(), SoundSource.BLOCKS, level.random);
        this.level = level;
        this.pos = pos;
        this.x = pos.getX() + 0.5D;
        this.y = pos.getY() + 0.5D;
        this.z = pos.getZ() + 0.5D;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.5F;
    }

    @Override
    public void tick() {
        if (!(this.level.getBlockEntity(this.pos) instanceof GrillTableBlockEntity grill)) {
            this.stop();
            return;
        }

        BlockState state = grill.getBlockState();
        if (!state.getValue(BlockStateProperties.LIT) || !grill.isCooking()) {
            this.stop();
        }
    }
}