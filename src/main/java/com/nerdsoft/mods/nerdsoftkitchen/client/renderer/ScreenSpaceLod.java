package com.nerdsoft.mods.nerdsoftkitchen.client.renderer;

import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
import net.minecraft.client.Minecraft;

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

    private static final Long2BooleanOpenHashMap HYSTERESIS_STATE = new Long2BooleanOpenHashMap();

    static boolean isPastThreshold(long packedPos, double radiusSqr, double distanceSqr, double coverageThreshold) {
        boolean wasPast = HYSTERESIS_STATE.get(packedPos);
        boolean isPast = isPastThreshold(radiusSqr, distanceSqr, coverageThreshold, wasPast);
        if (isPast != wasPast) {
            HYSTERESIS_STATE.put(packedPos, isPast);
        }
        return isPast;
    }
}
