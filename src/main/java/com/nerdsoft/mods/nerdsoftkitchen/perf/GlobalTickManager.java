package com.nerdsoft.mods.nerdsoftkitchen.perf;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class GlobalTickManager {

    public static final GlobalTickManager INSTANCE = new GlobalTickManager();

    private final Long2IntOpenHashMap slotByPos = new Long2IntOpenHashMap();
    private final ObjectArrayList<BlockEntity> tickables = new ObjectArrayList<>(256);
    private long[] posBySlot = new long[256];
    private short[] stateBySlot = new short[256];
    private int size = 0;

    static {
        // default fastutil load factor already avoids frequent resizes
        INSTANCE.slotByPos.defaultReturnValue(-1);
    }

    private GlobalTickManager() {
    }

    public int register(long packedPos, BlockEntity entity, short initialState) {
        int existing = slotByPos.get(packedPos);
        if (existing != -1) {
            return existing;
        }
        ensureCapacity(size + 1);
        int slot = size++;
        posBySlot[slot] = packedPos;
        stateBySlot[slot] = initialState;
        tickables.add(entity);
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
            stateBySlot[slot] = stateBySlot[lastSlot];
            tickables.set(slot, tickables.get(lastSlot));
            slotByPos.put(movedPos, slot);
        }
        tickables.set(lastSlot, null);
    }

    public short getState(int slot) {
        return stateBySlot[slot];
    }

    public void setState(int slot, short state) {
        stateBySlot[slot] = state;
    }

    public void tickAll(TickSink sink) {
        BlockEntity[] elements = tickables.elements();
        for (int i = 0; i < size; i++) {
            short current = stateBySlot[i];
            if (!StateMask.isActive(current)) {
                continue;
            }
            stateBySlot[i] = sink.tick(elements[i], posBySlot[i], current);
        }
    }

    private void ensureCapacity(int required) {
        if (required <= posBySlot.length) {
            return;
        }
        int newCap = posBySlot.length + (posBySlot.length >> 1); // grow by 1.5x
        long[] newPos = new long[newCap];
        short[] newState = new short[newCap];
        System.arraycopy(posBySlot, 0, newPos, 0, size);
        System.arraycopy(stateBySlot, 0, newState, 0, size);
        posBySlot = newPos;
        stateBySlot = newState;
    }

    @FunctionalInterface
    public interface TickSink {
        short tick(BlockEntity entity, long packedPos, short currentState);
    }
}