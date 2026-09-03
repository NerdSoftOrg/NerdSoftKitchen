package com.panzer.mods.dice_and_delish.client.sound;

import com.panzer.mods.dice_and_delish.block.SkilletBlock;
import com.panzer.mods.dice_and_delish.blockentity.SkilletBlockEntity;
import com.panzer.mods.dice_and_delish.registry.sound.ModSounds;
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
