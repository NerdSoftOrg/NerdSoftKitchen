package com.panzer.mods.dice_and_delish.util;

import net.minecraft.util.RandomSource;

public final class RandomUtil {

    private RandomUtil() {
    }

    public static void shuffle(int[] array, RandomSource random) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public static float jitter(RandomSource random, float base, float range) {
        return base + random.nextFloat() * range;
    }

    public static float jitteredPitch(RandomSource random, float basePitch, float variance) {
        return jitter(random, basePitch, variance);
    }
}
