package com.panzer.mods.dice_and_delish.mixin.client;

import com.panzer.mods.dice_and_delish.block.SkilletBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

    @Inject(method = "makeParticles", at = @At("HEAD"), cancellable = true)
    private static void dice_and_delish$cancelSmokeParticles(Level level, BlockPos pos, boolean isSignal, boolean spawnExtraSmoke, CallbackInfo ci) {
        if (level.getBlockState(pos.above()).getBlock() instanceof SkilletBlock) {
            ci.cancel();
        }
    }
}
