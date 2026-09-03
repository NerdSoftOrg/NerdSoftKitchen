package com.panzer.mods.dice_and_delish.client.sound;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.block.GrillTableBlock;
import com.panzer.mods.dice_and_delish.blockentity.GrillTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

// Compatibility for 1.21.1
@SuppressWarnings("removal")
@EventBusSubscriber(modid = DiceAndDelish.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class GrillLoopSoundManager {

    private static final LoopingBlockSoundManager<GrillTableBlockEntity, GrillLoopSoundInstance> DELEGATE =
            new LoopingBlockSoundManager<>(
                    grill -> grill.getBlockState().getValue(GrillTableBlock.LIT) && grill.isCooking(),
                    GrillLoopSoundInstance::new
            ) {};

    private GrillLoopSoundManager() {
    }

    public static void update(Level level, BlockPos pos, BlockState state, GrillTableBlockEntity entity) {
        DELEGATE.update(level, pos, state, entity);
    }

    @SuppressWarnings("unused")
    public static void stop(BlockPos pos) {
        DELEGATE.stop(pos);
    }

    public static void clear() {
        DELEGATE.clear();
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
        if (event.getLevel().isClientSide()) {
            DELEGATE.stopSoundsInChunk(event.getChunk().getPos());
        }
    }
}
