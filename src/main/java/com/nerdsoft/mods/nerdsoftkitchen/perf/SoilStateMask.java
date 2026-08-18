package com.nerdsoft.mods.nerdsoftkitchen.perf;

/**
 * Bitfield layout (LSB &rarr; MSB, 16 bits total):
 *
 * <table border="1" cellpadding="5" cellspacing="0">
 *   <thead>
 *     <tr>
 *       <th align="center">Bits</th>
 *       <th align="left">Function</th>
 *       <th align="left">Description</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *     <tr>
 *       <td align="center"><b>0</b></td>
 *       <td><code>NOURISHED</code></td>
 *       <td><code>1</code> = nutrients &ge; threshold, mirrors <code>isNourished()</code> for <i>O(1)</i> reads.</td>
 *     </tr>
 *     <tr>
 *       <td align="center"><b>1–4</b></td>
 *       <td><code>NUTRIENTS</code></td>
 *       <td>4 bits (0–15). Valid range: 0–9 (per <code>MAX_NUTRIENT_THRESHOLD</code>).</td>
 *     </tr>
 *     <tr>
 *       <td align="center"><b>5–8</b></td>
 *       <td><code>THRESHOLD</code></td>
 *       <td>4 bits (0–15). Valid range: 0 or 3–9 (per <code>MIN/MAX_NUTRIENT_THRESHOLD</code>).</td>
 *     </tr>
 *     <tr>
 *       <td align="center"><b>9–15</b></td>
 *       <td><code>UNUSED</code></td>
 *       <td>Reserved for future soil-tile state (moisture tier, etc.).</td>
 *     </tr>
 *   </tbody>
 * </table>
 */
public final class SoilStateMask {

    public static final int NOURISHED_BIT = 0;
    public static final int NUTRIENTS_SHIFT = 1;
    public static final int THRESHOLD_SHIFT = 5;

    public static final int NUTRIENTS_BITS = 4;
    public static final int THRESHOLD_BITS = 4;

    public static final int NOURISHED_MASK = 1 << NOURISHED_BIT;
    public static final int NUTRIENTS_MASK = ((1 << NUTRIENTS_BITS) - 1) << NUTRIENTS_SHIFT;
    public static final int THRESHOLD_MASK = ((1 << THRESHOLD_BITS) - 1) << THRESHOLD_SHIFT;

    public static final int NUTRIENTS_MAX = (1 << NUTRIENTS_BITS) - 1;
    public static final int THRESHOLD_MAX = (1 << THRESHOLD_BITS) - 1;

    private SoilStateMask() {
    }

    public static boolean isNourished(short packed) {
        return (packed & NOURISHED_MASK) != 0;
    }

    public static short setNourished(short packed, boolean value) {
        int v = (value ? 1 : 0) << NOURISHED_BIT;
        return (short) ((packed & ~NOURISHED_MASK) | v);
    }

    public static int getNutrients(short packed) {
        return (packed & NUTRIENTS_MASK) >>> NUTRIENTS_SHIFT;
    }

    public static short setNutrients(short packed, int nutrients) {
        int clamped = nutrients & NUTRIENTS_MAX;
        return (short) ((packed & ~NUTRIENTS_MASK) | (clamped << NUTRIENTS_SHIFT));
    }

    public static int getThreshold(short packed) {
        return (packed & THRESHOLD_MASK) >>> THRESHOLD_SHIFT;
    }

    public static short setThreshold(short packed, int threshold) {
        int clamped = threshold & THRESHOLD_MAX;
        return (short) ((packed & ~THRESHOLD_MASK) | (clamped << THRESHOLD_SHIFT));
    }

    public static short incrementNutrients(short packed) {
        int nextNutrients = (getNutrients(packed) + 1) & NUTRIENTS_MAX;
        int threshold = getThreshold(packed);
        short withNutrients = (short) ((packed & ~NUTRIENTS_MASK) | (nextNutrients << NUTRIENTS_SHIFT));
        boolean nourished = threshold > 0 && nextNutrients >= threshold;
        return setNourished(withNutrients, nourished);
    }
}