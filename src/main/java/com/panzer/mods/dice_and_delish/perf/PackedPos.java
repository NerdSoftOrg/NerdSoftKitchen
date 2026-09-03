package com.panzer.mods.dice_and_delish.perf;

/**
 * Bitfield layout (MSB &rarr; LSB, 64 bits total):
 *
 * <table border="1" cellpadding="5" cellspacing="0">
 *   <thead>
 *     <tr>
 *       <th align="center">Bits</th>
 *       <th align="center">Size</th>
 *       <th align="left">Axis</th>
 *       <th align="left">Encoding &amp; Range</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *     <tr>
 *       <td align="center"><b>38–63</b></td>
 *       <td align="center">26 bits</td>
 *       <td><code>X</code></td>
 *       <td>Sign-extended two's complement</td>
 *     </tr>
 *     <tr>
 *       <td align="center"><b>12–37</b></td>
 *       <td align="center">26 bits</td>
 *       <td><code>Z</code></td>
 *       <td>Sign-extended two's complement</td>
 *     </tr>
 *     <tr>
 *       <td align="center"><b>0–11</b></td>
 *       <td align="center">12 bits</td>
 *       <td><code>Y</code></td>
 *       <td>Sign-extended two's complement (<code>-2048</code> to <code>2047</code>, covers full build range)</td>
 *     </tr>
 *   </tbody>
 * </table>
 */
public final class PackedPos {

    private static final int X_BITS = 26;
    private static final int Z_BITS = 26;
    private static final int Y_BITS = 12;

    private static final int Y_SHIFT = 0;
    private static final int Z_SHIFT = Y_BITS;
    private static final int X_SHIFT = Y_BITS + Z_BITS;

    private static final long X_MASK = (1L << X_BITS) - 1L;
    private static final long Y_MASK = (1L << Y_BITS) - 1L;
    private static final long Z_MASK = (1L << Z_BITS) - 1L;

    private PackedPos() {
    }

    public static long pack(int x, int y, int z) {
        return ((x & X_MASK) << X_SHIFT) | ((z & Z_MASK) << Z_SHIFT) | (y & Y_MASK);
    }

    public static int unpackX(long packed) {
        // arithmetic shift right sign-extends the 26-bit field back to a full int
        return (int) (packed << (64 - X_SHIFT - X_BITS) >> (64 - X_BITS));
    }

    public static int unpackY(long packed) {
        return (int) (packed << (64 - Y_BITS) >> (64 - Y_BITS));
    }

    public static int unpackZ(long packed) {
        return (int) (packed << (64 - Z_SHIFT - Z_BITS) >> (64 - Z_BITS));
    }

    // squared distance from a raw camera-space double triple
    public static double distSq(long packed, double camX, double camY, double camZ) {
        double dx = unpackX(packed) + 0.5 - camX;
        double dy = unpackY(packed) + 0.5 - camY;
        double dz = unpackZ(packed) + 0.5 - camZ;
        return dx * dx + dy * dy + dz * dz;
    }
}
