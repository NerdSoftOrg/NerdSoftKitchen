package com.nerdsoft.mods.nerdsoftkitchen.client.sound;

import com.nerdsoft.mods.nerdsoftkitchen.block.SkilletBlock;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.SkilletBlockEntity;
import com.nerdsoft.mods.nerdsoftkitchen.registry.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class SkilletLoopSoundInstance extends LoopingBlockSoundInstance<SkilletBlockEntity> {

    private static final double Y_OFFSET = 0.25D;
    private static final float VOLUME = 0.4F;

    public SkilletLoopSoundInstance(Level level, BlockPos pos) {
        super(ModSounds.SKILLET_SIZZLE_LOOP, level, pos, Y_OFFSET, VOLUME, SkilletBlockEntity.class,
                skillet -> skillet.getBlockState().getValue(SkilletBlock.LIT) && skillet.isCooking());
    }
}
