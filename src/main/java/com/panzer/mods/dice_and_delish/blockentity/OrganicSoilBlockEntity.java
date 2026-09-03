package com.panzer.mods.dice_and_delish.blockentity;

import com.panzer.mods.dice_and_delish.perf.GlobalTickManager;
import com.panzer.mods.dice_and_delish.perf.PackedPos;
import com.panzer.mods.dice_and_delish.perf.SoilStateMask;
import com.panzer.mods.dice_and_delish.registry.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OrganicSoilBlockEntity extends BlockEntity {

    public static final int MIN_NUTRIENT_THRESHOLD = 3;
    public static final int MAX_NUTRIENT_THRESHOLD = 9;

    private static final String NUTRIENTS_KEY = "Nutrients";
    private static final String THRESHOLD_KEY = "NutrientThreshold";

    private final long packedPos;
    private int tickSlot = -1;

    public OrganicSoilBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ORGANIC_SOIL.get(), pos, state);
        this.packedPos = PackedPos.pack(pos.getX(), pos.getY(), pos.getZ());
    }

    private void ensureRegistered() {
        if (tickSlot == -1) {
            tickSlot = GlobalTickManager.INSTANCE.register(packedPos, this, (short) 0);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        ensureRegistered();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (tickSlot != -1) {
            GlobalTickManager.INSTANCE.unregister(packedPos);
            tickSlot = -1;
        }
    }

    private short state() {
        ensureRegistered();
        return GlobalTickManager.INSTANCE.getState(tickSlot);
    }

    private void setState(short value) {
        ensureRegistered();
        GlobalTickManager.INSTANCE.setState(tickSlot, value);
    }

    private int nutrients() {
        return SoilStateMask.getNutrients(state());
    }

    private int threshold() {
        return SoilStateMask.getThreshold(state());
    }

    public boolean isNourished() {
        return SoilStateMask.isNourished(state());
    }

    public boolean addNutrient(RandomSource random) {
        short current = state();
        int currentThreshold = SoilStateMask.getThreshold(current);
        if (currentThreshold <= 0) {
            currentThreshold = MIN_NUTRIENT_THRESHOLD + random.nextInt(MAX_NUTRIENT_THRESHOLD - MIN_NUTRIENT_THRESHOLD + 1);
            current = SoilStateMask.setThreshold(current, currentThreshold);
            setState(current);
        }
        int currentNutrients = SoilStateMask.getNutrients(current);
        if (currentNutrients >= currentThreshold) {
            return true;
        }
        setState(SoilStateMask.incrementNutrients(current));
        setChanged();
        requestSync();
        return nutrients() >= currentThreshold;
    }

    public void reset() {
        setState((short) 0);
        setChanged();
        requestSync();
    }

    private void requestSync() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        int loadedNutrients = tag.getInt(NUTRIENTS_KEY);
        int loadedThreshold = tag.getInt(THRESHOLD_KEY);
        short packed = SoilStateMask.setNutrients((short) 0, loadedNutrients);
        packed = SoilStateMask.setThreshold(packed, loadedThreshold);
        packed = SoilStateMask.setNourished(packed, loadedThreshold > 0 && loadedNutrients >= loadedThreshold);
        setState(packed);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt(NUTRIENTS_KEY, nutrients());
        tag.putInt(THRESHOLD_KEY, threshold());
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        loadAdditional(tag, registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
