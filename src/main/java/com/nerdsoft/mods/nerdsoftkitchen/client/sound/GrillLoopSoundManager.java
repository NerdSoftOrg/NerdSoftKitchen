package com.nerdsoft.mods.nerdsoftkitchen.client.sound;

import com.nerdsoft.mods.nerdsoftkitchen.blockentity.GrillTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public final class GrillLoopSoundManager {

    private static final Map<BlockPos, GrillLoopSoundInstance> ACTIVE_SOUNDS = new HashMap<>();

    private GrillLoopSoundManager() {
    }

    public static void update(Level level, BlockPos pos, BlockState state, GrillTableBlockEntity entity) {
        boolean shouldPlay = state.getValue(com.nerdsoft.mods.nerdsoftkitchen.block.GrillTableBlock.LIT) && entity.isCooking();
        GrillLoopSoundInstance existing = ACTIVE_SOUNDS.get(pos);

        if (shouldPlay) {
            if (existing == null || !Minecraft.getInstance().getSoundManager().isActive(existing)) {
                GrillLoopSoundInstance newSound = new GrillLoopSoundInstance(level, pos);
                ACTIVE_SOUNDS.put(pos, newSound);
                Minecraft.getInstance().getSoundManager().play(newSound);
            }
        } else {
            stop(pos);
        }
    }

    public static void stop(BlockPos pos) {
        GrillLoopSoundInstance existing = ACTIVE_SOUNDS.remove(pos);
        if (existing != null) {
            Minecraft.getInstance().getSoundManager().stop(existing);
        }
    }
}