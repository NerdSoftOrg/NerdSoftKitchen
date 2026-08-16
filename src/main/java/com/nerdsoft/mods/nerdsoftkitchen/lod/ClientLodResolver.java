package com.nerdsoft.mods.nerdsoftkitchen.lod;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public final class ClientLodResolver {

    private static final double BLOCKS_PER_CHUNK = 16.0;

    private ClientLodResolver() {
    }

    public static int resolveTier(LodBlock lodBlock, BlockPos pos) {
        int maxTier = lodBlock.maxLodTier();
        if (maxTier <= 0) {
            return 0;
        }

        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return 0;
        }

        double distanceSqr = player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

        int tier = 0;
        for (int t = 0; t < maxTier; t++) {
            double thresholdBlocks = lodBlock.tierDistanceChunks(t) * BLOCKS_PER_CHUNK;
            if (distanceSqr > thresholdBlocks * thresholdBlocks) {
                tier = t + 1;
            }
        }
        return tier;
    }
}