package com.nerdsoft.mods.nerdsoftkitchen.client.sound;

import com.nerdsoft.mods.nerdsoftkitchen.blockentity.GrillTableBlockEntity;
import com.nerdsoft.mods.nerdsoftkitchen.registry.sound.ModSounds;
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
