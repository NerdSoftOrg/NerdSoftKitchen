package com.nerdsoft.mods.nerdsoftkitchen.util;

import net.minecraft.util.RandomSource;

/**
 * Small stateless helpers for the seeded-randomness patterns repeated across the cooking block
 * entities (per-slot render jitter in {@code GrillTableBlockEntity}/{@code SkilletBlockEntity},
 * pitch-varied "place food" sounds, etc.). Centralized here instead of copy-pasted per class so
 * a fix or tuning change only needs to happen once.
 */
public final class RandomUtil {

    private RandomUtil() {
    }

    /**
     * In-place Fisher-Yates shuffle. Used to pick a non-repeating rotation/quadrant "slot" for
     * each occupied inventory slot so adjacent items don't cluster toward the same jittered
     * angle by chance.
     */
    public static void shuffle(int[] array, RandomSource random) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    /**
     * {@code base + random.nextFloat() * range} - the pitch/rotation-jitter formula repeated
     * throughout the cooking block entities and sound helpers. Centralizing the expression
     * doesn't change behavior, just removes the repeated arithmetic at every call site.
     */
    public static float jitter(RandomSource random, float base, float range) {
        return base + random.nextFloat() * range;
    }

    /**
     * Convenience overload for the common "play sound with a small random pitch variance around
     * a base pitch" pattern (e.g. sizzle/place-food sounds). Returns the jittered pitch value;
     * callers still own the actual {@code playSound} call since the sound event, volume, and
     * position differ per caller.
     */
    public static float jitteredPitch(RandomSource random, float basePitch, float variance) {
        return jitter(random, basePitch, variance);
    }
}