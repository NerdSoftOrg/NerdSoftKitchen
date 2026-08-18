package com.nerdsoft.mods.nerdsoftkitchen.perf;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public final class ClientLodManager {

    public static final ClientLodManager INSTANCE = new ClientLodManager();

    private final Long2IntOpenHashMap slotByPos = new Long2IntOpenHashMap();
    private final ObjectArrayList<Object> handles = new ObjectArrayList<>(256);
    private long[] posBySlot = new long[256];
    private short[] lodBySlot = new short[256];
    private int size = 0;

    static {
        INSTANCE.slotByPos.defaultReturnValue(-1);
    }

    private ClientLodManager() {
    }

    public int register(long packedPos, Object renderHandle) {
        int existing = slotByPos.get(packedPos);
        if (existing != -1) {
            return existing;
        }
        ensureCapacity(size + 1);
        int slot = size++;
        posBySlot[slot] = packedPos;
        lodBySlot[slot] = 0;
        handles.add(renderHandle);
        slotByPos.put(packedPos, slot);
        return slot;
    }

    public void unregister(long packedPos) {
        int slot = slotByPos.remove(packedPos);
        if (slot == -1) {
            return;
        }
        int lastSlot = --size;
        if (slot != lastSlot) {
            long movedPos = posBySlot[lastSlot];
            posBySlot[slot] = movedPos;
            lodBySlot[slot] = lodBySlot[lastSlot];
            handles.set(slot, handles.get(lastSlot));
            slotByPos.put(movedPos, slot);
        }
        handles.set(lastSlot, null);
    }

    public void updateTiers(double camX, double camY, double camZ) {
        long[] pos = posBySlot;
        short[] lod = lodBySlot;
        int n = size;
        for (int i = 0; i < n; i++) {
            double distSq = PackedPos.distSq(pos[i], camX, camY, camZ);
            int tier = ClientLodMask.resolveTier(distSq);
            lod[i] = ClientLodMask.setLod(lod[i], tier);
        }
    }
    
    public void forEachFullDetail(ParticleSink sink) {
        long[] pos = posBySlot;
        short[] lod = lodBySlot;
        Object[] elements = handles.elements();
        int n = size;
        for (int i = 0; i < n; i++) {
            if (ClientLodMask.getLod(lod[i]) != ClientLodMask.LOD_FULL) {
                continue;
            }
            long p = pos[i];
            sink.accept(elements[i], PackedPos.unpackX(p), PackedPos.unpackY(p), PackedPos.unpackZ(p));
        }
    }

    public int getLod(int slot) {
        return ClientLodMask.getLod(lodBySlot[slot]);
    }

    private void ensureCapacity(int required) {
        if (required <= posBySlot.length) {
            return;
        }
        int newCap = posBySlot.length + (posBySlot.length >> 1);
        long[] newPos = new long[newCap];
        short[] newLod = new short[newCap];
        System.arraycopy(posBySlot, 0, newPos, 0, size);
        System.arraycopy(lodBySlot, 0, newLod, 0, size);
        posBySlot = newPos;
        lodBySlot = newLod;
    }

    @FunctionalInterface
    public interface ParticleSink {
        void accept(Object renderHandle, int x, int y, int z);
    }
}