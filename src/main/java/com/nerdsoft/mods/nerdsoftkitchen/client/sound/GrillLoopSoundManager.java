package com.nerdsoft.mods.nerdsoftkitchen.client.sound;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.GrillTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// Compatibility for 1.21.1
@SuppressWarnings("removal")
@EventBusSubscriber(modid = NerdSoftKitchen.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
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
        } else if (existing != null) {
            stop(pos);
        }
    }

    public static void stop(BlockPos pos) {
        GrillLoopSoundInstance existing = ACTIVE_SOUNDS.remove(pos);
        if (existing != null) {
            Minecraft.getInstance().getSoundManager().stop(existing);
        }
    }

    public static void clear() {
        ACTIVE_SOUNDS.clear();
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            clear();
        }
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        clear();
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        if (ACTIVE_SOUNDS.isEmpty() || !event.getLevel().isClientSide()) {
            return;
        }
        ChunkPos chunkPos = event.getChunk().getPos();
        Iterator<Map.Entry<BlockPos, GrillLoopSoundInstance>> iterator = ACTIVE_SOUNDS.entrySet().iterator();
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next().getKey();
            if (pos.getX() >> 4 == chunkPos.x && pos.getZ() >> 4 == chunkPos.z) {
                Minecraft.getInstance().getSoundManager().stop(ACTIVE_SOUNDS.get(pos));
                iterator.remove();
            }
        }
    }
}