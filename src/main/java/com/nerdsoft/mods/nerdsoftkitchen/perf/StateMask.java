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
 *       <td><code>HEATED</code></td>
 *       <td><code>1</code> = actively heated this tick.</td>
 *     </tr>
 *     <tr>
 *       <td align="center"><b>1</b></td>
 *       <td><code>ACTIVE</code></td>
 *       <td><code>1</code> = block is running its process (e.g. cooking/mixing).</td>
 *     </tr>
 *     <tr>
 *       <td align="center"><b>2</b></td>
 *       <td><code>HOT_RESIDUAL</code></td>
 *       <td><code>1</code> = post-use residual heat window (decoupled from <code>HEATED</code>).</td>
 *     </tr>
 *     <tr>
 *       <td align="center"><b>3–6</b></td>
 *       <td><code>STAGE</code></td>
 *       <td>4 bits (0–15). Recipe or animation stage index.</td>
 *     </tr>
 *     <tr>
 *       <td align="center"><b>7–15</b></td>
 *       <td><code>PROGRESS</code></td>
 *       <td>9 bits (0–511). Progress ticks (recipes capped under 511 ticks or externally scaled, e.g. <code>progress &gt;&gt; 1</code> for 0–1022 range).</td>
 *     </tr>
 *   </tbody>
 * </table>
 */
public final class StateMask {

    // Bit offsets
    public static final int HEATED_BIT = 0;
    public static final int ACTIVE_BIT = 1;
    public static final int HOT_RESIDUAL_BIT = 2;
    public static final int STAGE_SHIFT = 3;
    public static final int PROGRESS_SHIFT = 7;

    // Field widths
    public static final int STAGE_BITS = 4;
    public static final int PROGRESS_BITS = 9;

    // Masks
    public static final int HEATED_MASK = 1 << HEATED_BIT;
    public static final int ACTIVE_MASK = 1 << ACTIVE_BIT;
    public static final int HOT_RESIDUAL_MASK = 1 << HOT_RESIDUAL_BIT;
    public static final int STAGE_MASK = ((1 << STAGE_BITS) - 1) << STAGE_SHIFT;
    public static final int PROGRESS_MASK = ((1 << PROGRESS_BITS) - 1) << PROGRESS_SHIFT;

    public static final int STAGE_MAX = (1 << STAGE_BITS) - 1;
    public static final int PROGRESS_MAX = (1 << PROGRESS_BITS) - 1;

    private StateMask() {
    }

    public static boolean isHeated(short packed) {
        return (packed & HEATED_MASK) != 0;
    }

    public static short setHeated(short packed, boolean value) {
        int v = (value ? 1 : 0) << HEATED_BIT;
        return (short) ((packed & ~HEATED_MASK) | v);
    }

    public static boolean isActive(short packed) {
        return (packed & ACTIVE_MASK) != 0;
    }

    public static short setActive(short packed, boolean value) {
        int v = (value ? 1 : 0) << ACTIVE_BIT;
        return (short) ((packed & ~ACTIVE_MASK) | v);
    }

    public static boolean isHotResidual(short packed) {
        return (packed & HOT_RESIDUAL_MASK) != 0;
    }

    public static short setHotResidual(short packed, boolean value) {
        int v = (value ? 1 : 0) << HOT_RESIDUAL_BIT;
        return (short) ((packed & ~HOT_RESIDUAL_MASK) | v);
    }

    public static int getStage(short packed) {
        return (packed & STAGE_MASK) >>> STAGE_SHIFT;
    }

    public static short setStage(short packed, int stage) {
        int clamped = stage & STAGE_MAX;
        return (short) ((packed & ~STAGE_MASK) | (clamped << STAGE_SHIFT));
    }

    public static int getProgress(short packed) {
        return (packed & PROGRESS_MASK) >>> PROGRESS_SHIFT;
    }

    public static short setProgress(short packed, int progress) {
        int clamped = progress & PROGRESS_MAX;
        return (short) ((packed & ~PROGRESS_MASK) | (clamped << PROGRESS_SHIFT));
    }

    public static short incrementProgress(short packed) {
        int next = (getProgress(packed) + 1) & PROGRESS_MAX;
        return (short) ((packed & ~PROGRESS_MASK) | (next << PROGRESS_SHIFT));
    }
}