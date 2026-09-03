package com.panzer.mods.dice_and_delish.client.sound;

import com.panzer.mods.dice_and_delish.blockentity.GrillTableBlockEntity;
import com.panzer.mods.dice_and_delish.registry.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class GrillLoopSoundInstance extends LoopingBlockSoundInstance<GrillTableBlockEntity> {

    private static final double Y_OFFSET = 0.5D;
    private static final float VOLUME = 0.5F;

    public GrillLoopSoundInstance(Level level, BlockPos pos) {
        super(ModSounds.GRILL_GRILLING, level, pos, Y_OFFSET, VOLUME, GrillTableBlockEntity.class,
                grill -> grill.getBlockState().getValue(BlockStateProperties.LIT) && grill.isCooking());
    }
}
