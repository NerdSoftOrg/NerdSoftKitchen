package com.nerdsoft.mods.nerdsoftkitchen.client.renderer;

import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;

final class ScreenSpaceLod {

    private static int cachedFovDegrees = Integer.MIN_VALUE;
    private static double cachedInverseTanHalfFovSqr;

    private ScreenSpaceLod() {
    }

    private static double inverseTanHalfFovSqr() {
        int fovDegrees = Minecraft.getInstance().options.fov().get();
        if (fovDegrees != cachedFovDegrees) {
            double halfFovRad = Math.toRadians(fovDegrees) * 0.5;
            double tanHalfFov = Math.tan(halfFovRad);
            cachedInverseTanHalfFovSqr = 1.0 / (tanHalfFov * tanHalfFov);
            cachedFovDegrees = fovDegrees;
        }
        return cachedInverseTanHalfFovSqr;
    }

    static double thresholdForDistance(double radiusSqr, double referenceDistanceBlocks) {
        double referenceDistanceSqr = referenceDistanceBlocks * referenceDistanceBlocks;
        double defaultFovTanHalfSqr = defaultFovInverseTanHalfSqr();
        return (radiusSqr * defaultFovTanHalfSqr) / referenceDistanceSqr;
    }

    private static double defaultFovInverseTanHalfSqr() {
        double halfFovRad = Math.toRadians(70.0) * 0.5;
        double tanHalfFov = Math.tan(halfFovRad);
        return 1.0 / (tanHalfFov * tanHalfFov);
    }

    static double coverage(double radiusSqr, double distanceSqr) {
        if (distanceSqr <= 0.0) {
            // Camera is at (or inside) the object, treat as maximum coverage rather than
            // dividing by zero or returning a nonsensical huge value.
            return Double.MAX_VALUE;
        }
        return (radiusSqr * inverseTanHalfFovSqr()) / distanceSqr;
    }

    private static boolean isPastThreshold(double radiusSqr, double distanceSqr, double coverageThreshold,
                                           boolean currentlyPast) {
        double effectiveThreshold = currentlyPast
                ? coverageThreshold * HYSTERESIS_UP_FACTOR   // already simplified: require *more* coverage to go back to full detail
                : coverageThreshold * HYSTERESIS_DOWN_FACTOR; // already full detail: require *less* coverage to simplify
        return coverage(radiusSqr, distanceSqr) < effectiveThreshold;
    }

    // 15% hysteresis band on either side of the nominal threshold.
    private static final double HYSTERESIS_UP_FACTOR = 1.15;
    private static final double HYSTERESIS_DOWN_FACTOR = 0.85;

    // Two independent maps rather than one shared map with a salted key: a single block position
    // can carry both an item LOD state and a block-body LOD state at once (see
    // GrillTableBlockEntityRenderer), and keeping the raw BlockPos#asLong() as the key (instead
    // of XOR-mangling it) lets onChunkUnload/onPositionRemoved below derive chunk/xz coordinates
    // straight from the key with BlockPos.getX/Y/Z(long), exactly like vanilla does.
    private static final Long2BooleanOpenHashMap ITEM_HYSTERESIS_STATE = new Long2BooleanOpenHashMap();
    private static final Long2BooleanOpenHashMap BODY_HYSTERESIS_STATE = new Long2BooleanOpenHashMap();

    static boolean isPastThreshold(long packedPos, double radiusSqr, double distanceSqr, double coverageThreshold) {
        return isPastThreshold(ITEM_HYSTERESIS_STATE, packedPos, radiusSqr, distanceSqr, coverageThreshold);
    }

    static boolean isBodyPastThreshold(long packedPos, double radiusSqr, double distanceSqr, double coverageThreshold) {
        return isPastThreshold(BODY_HYSTERESIS_STATE, packedPos, radiusSqr, distanceSqr, coverageThreshold);
    }

    private static boolean isPastThreshold(Long2BooleanOpenHashMap state, long packedPos, double radiusSqr,
                                            double distanceSqr, double coverageThreshold) {
        boolean wasPast = state.get(packedPos);
        boolean isPast = isPastThreshold(radiusSqr, distanceSqr, coverageThreshold, wasPast);
        if (isPast != wasPast) {
            state.put(packedPos, isPast);
        }
        return isPast;
    }

    static void forget(long packedPos) {
        ITEM_HYSTERESIS_STATE.remove(packedPos);
        BODY_HYSTERESIS_STATE.remove(packedPos);
    }

    static void forgetChunk(int chunkX, int chunkZ) {
        forgetChunk(ITEM_HYSTERESIS_STATE, chunkX, chunkZ);
        forgetChunk(BODY_HYSTERESIS_STATE, chunkX, chunkZ);
    }

    private static void forgetChunk(Long2BooleanOpenHashMap state, int chunkX, int chunkZ) {
        if (state.isEmpty()) {
            return;
        }
        LongIterator iterator = state.keySet().iterator();
        while (iterator.hasNext()) {
            long packedPos = iterator.nextLong();
            if ((BlockPos.getX(packedPos) >> 4) == chunkX && (BlockPos.getZ(packedPos) >> 4) == chunkZ) {
                iterator.remove();
            }
        }
    }

    static void clear() {
        ITEM_HYSTERESIS_STATE.clear();
        BODY_HYSTERESIS_STATE.clear();
    }

    static void forgetChunkPos(ChunkPos pos) {
        forgetChunk(pos.x, pos.z);
    }
}
