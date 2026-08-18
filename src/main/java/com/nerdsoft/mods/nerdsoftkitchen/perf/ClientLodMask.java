package com.nerdsoft.mods.nerdsoftkitchen.perf;

/**
 * Level of Detail (LOD) state definitions:
 *
 * <table border="1" cellpadding="5" cellspacing="0">
 *   <thead>
 *     <tr>
 *       <th align="center">Value</th>
 *       <th align="left">Level</th>
 *       <th align="left">Distance Threshold</th>
 *       <th align="left">Rendering Behavior</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *     <tr>
 *       <td align="center"><b>0</b></td>
 *       <td><code>FULL</code></td>
 *       <td><code>dist &lt; 16m</code></td>
 *       <td>Full geometry + particles.</td>
 *     </tr>
 *     <tr>
 *       <td align="center"><b>1</b></td>
 *       <td><code>SIMPLIFIED</code></td>
 *       <td><code>16m &le; dist &lt; 32m</code></td>
 *       <td>Simplified mesh, no particles.</td>
 *     </tr>
 *     <tr>
 *       <td align="center"><b>2</b></td>
 *       <td><code>BILLBOARD</code></td>
 *       <td><code>32m &le; dist &lt; 64m</code></td>
 *       <td>Low-poly / billboard.</td>
 *     </tr>
 *     <tr>
 *       <td align="center"><b>3</b></td>
 *       <td><code>CULLED</code></td>
 *       <td><code>dist &ge; 64m</code></td>
 *       <td>Not rendered.</td>
 *     </tr>
 *   </tbody>
 * </table>
 */
public final class ClientLodMask {

    public static final int LOD_SHIFT = 13;
    public static final int LOD_BITS = 2;
    public static final int LOD_MASK = ((1 << LOD_BITS) - 1) << LOD_SHIFT;

    public static final int LOD_FULL = 0;
    public static final int LOD_SIMPLIFIED = 1;
    public static final int LOD_BILLBOARD = 2;
    public static final int LOD_CULLED = 3;

    private static final double T0_SQ = 16.0 * 16.0;
    private static final double T1_SQ = 32.0 * 32.0;
    private static final double T2_SQ = 64.0 * 64.0;

    private ClientLodMask() {
    }

    public static int getLod(short packed) {
        return (packed & LOD_MASK) >>> LOD_SHIFT;
    }

    public static short setLod(short packed, int lod) {
        return (short) ((packed & ~LOD_MASK) | ((lod & 0b11) << LOD_SHIFT));
    }

    /**
     * Branchless-leaning tier resolve: three compares collapse to an additive rank instead of
     * an if/else chain, letting the JIT emit compare+set-flag sequences without deep branching.
     */
    public static int resolveTier(double distSq) {
        int tier = 0;
        tier += distSq >= T0_SQ ? 1 : 0;
        tier += distSq >= T1_SQ ? 1 : 0;
        tier += distSq >= T2_SQ ? 1 : 0;
        return tier; // 0..3, matches FULL..CULLED
    }

    public static boolean allowsParticles(int lod) {
        return lod == LOD_FULL;
    }

    public static boolean isVisible(int lod) {
        return lod != LOD_CULLED;
    }
}