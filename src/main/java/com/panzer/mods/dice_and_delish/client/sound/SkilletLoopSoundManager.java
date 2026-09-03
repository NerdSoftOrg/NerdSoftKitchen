package com.panzer.mods.dice_and_delish.client.sound;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.block.SkilletBlock;
import com.panzer.mods.dice_and_delish.blockentity.SkilletBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = DiceAndDelish.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class SkilletLoopSoundManager {

    private static final LoopingBlockSoundManager<SkilletBlockEntity, SkilletLoopSoundInstance> DELEGATE =
            new LoopingBlockSoundManager<>(
                    skillet -> skillet.getBlockState().getValue(SkilletBlock.LIT) && skillet.isCooking(),
                    SkilletLoopSoundInstance::new
            ) {};

    private SkilletLoopSoundManager() {
    }

    public static void update(Level level, BlockPos pos, BlockState state, SkilletBlockEntity entity) {
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
