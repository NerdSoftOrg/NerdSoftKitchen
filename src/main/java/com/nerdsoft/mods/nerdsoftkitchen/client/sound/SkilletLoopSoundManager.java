package com.nerdsoft.mods.nerdsoftkitchen.client.sound;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.block.SkilletBlock;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.SkilletBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = NerdSoftKitchen.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class SkilletLoopSoundManager {

    private static final Map<BlockPos, SkilletLoopSoundInstance> ACTIVE_SOUNDS = new HashMap<>();

    private SkilletLoopSoundManager() {
    }

    private static SoundManager soundManager() {
        return Minecraft.getInstance().getSoundManager();
    }

    public static void update(Level level, BlockPos pos, BlockState state, SkilletBlockEntity entity) {
        boolean shouldPlay = state.getValue(SkilletBlock.LIT) && entity.isCooking();
        SkilletLoopSoundInstance existing = ACTIVE_SOUNDS.get(pos);

        if (shouldPlay) {
            if (existing == null || !soundManager().isActive(existing)) {
                SkilletLoopSoundInstance newSound = new SkilletLoopSoundInstance(level, pos);
                ACTIVE_SOUNDS.put(pos, newSound);
                soundManager().play(newSound);
            }
        } else if (existing != null) {
            stop(pos);
        }
    }

    public static void stop(BlockPos pos) {
        SkilletLoopSoundInstance existing = ACTIVE_SOUNDS.remove(pos);
        if (existing != null) {
            soundManager().stop(existing);
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
        SoundManager sounds = soundManager();
        Iterator<Map.Entry<BlockPos, SkilletLoopSoundInstance>> iterator = ACTIVE_SOUNDS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, SkilletLoopSoundInstance> entry = iterator.next();
            BlockPos pos = entry.getKey();
            if (pos.getX() >> 4 == chunkPos.x && pos.getZ() >> 4 == chunkPos.z) {
                sounds.stop(entry.getValue());
                iterator.remove();
            }
        }
    }
}