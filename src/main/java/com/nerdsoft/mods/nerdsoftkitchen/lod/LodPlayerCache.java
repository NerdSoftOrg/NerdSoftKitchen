package com.nerdsoft.mods.nerdsoftkitchen.lod;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.WeakHashMap;

final class LodPlayerCache {

    private static final WeakHashMap<ServerLevel, LodPlayerCache> CACHES = new WeakHashMap<>();

    private long cachedTick = Long.MIN_VALUE;
    private Player[] players = new Player[0];
    private double[] x = new double[0];
    private double[] y = new double[0];
    private double[] z = new double[0];

    static LodPlayerCache forLevel(ServerLevel level) {
        return CACHES.computeIfAbsent(level, l -> new LodPlayerCache());
    }

    private void refreshIfStale(ServerLevel level) {
        long tick = level.getGameTime();
        if (tick == cachedTick) {
            return;
        }
        cachedTick = tick;

        List<ServerPlayer> levelPlayers = level.players();
        int count = levelPlayers.size();
        if (players.length != count) {
            players = new Player[count];
            x = new double[count];
            y = new double[count];
            z = new double[count];
        }
        for (int i = 0; i < count; i++) {
            ServerPlayer player = levelPlayers.get(i);
            players[i] = player;
            Vec3 pos = player.position();
            x[i] = pos.x;
            y[i] = pos.y;
            z[i] = pos.z;
        }
    }

    Player nearest(ServerLevel level, double px, double py, double pz, double[] outDistanceSqr) {
        refreshIfStale(level);

        Player best = null;
        double bestDistSqr = Double.MAX_VALUE;
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            if (player.isSpectator()) {
                continue;
            }
            double dx = x[i] - px;
            double dy = y[i] - py;
            double dz = z[i] - pz;
            double distSqr = dx * dx + dy * dy + dz * dz;
            if (distSqr < bestDistSqr) {
                bestDistSqr = distSqr;
                best = player;
            }
        }
        outDistanceSqr[0] = bestDistSqr;
        return best;
    }
}