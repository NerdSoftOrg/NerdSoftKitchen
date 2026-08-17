package com.nerdsoft.mods.nerdsoftkitchen.lod;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class LodResolver {

    private LodResolver() {
    }

    public static boolean isDue(BlockPos pos, long gameTime) {
        return Math.floorMod(pos.hashCode() + gameTime, LodConfig.CHECK_INTERVAL_TICKS) == 0;
    }

    public static void resolveAndApply(ServerLevel level, BlockPos pos, BlockState state, LodBlock lodBlock) {
        int maxTier = lodBlock.maxLodTier();
        if (maxTier <= 0) {
            return;
        }

        int currentTier = state.getValue(lodBlock.lodProperty());

        double[] distanceSqrHolder = new double[1];
        LodPlayerCache cache = LodPlayerCache.forLevel(level);
        Player nearest = cache.nearest(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, distanceSqrHolder);

        int targetTier = resolveTier(lodBlock, maxTier, nearest, distanceSqrHolder[0]);

        if (targetTier != currentTier) {
            level.setBlock(pos, state.setValue(lodBlock.lodProperty(), targetTier), 3);
            LodDiagnostics.tierChanged(lodBlock.getClass().getSimpleName(), pos, currentTier, targetTier);
        }
    }

    private static int resolveTier(LodBlock lodBlock, int maxTier, @Nullable Player nearest, double distanceSqr) {
        if (nearest == null) {
            return 0;
        }

        int tier = 0;
        for (int t = 0; t < maxTier; t++) {
            double thresholdBlocks = lodBlock.tierDistanceChunks(t) * 16.0;
            if (distanceSqr <= thresholdBlocks * thresholdBlocks) {
                break;
            }
            tier = t + 1;
        }
        return tier;
    }
}