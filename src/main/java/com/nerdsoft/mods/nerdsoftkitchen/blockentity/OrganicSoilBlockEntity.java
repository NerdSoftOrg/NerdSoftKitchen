package com.nerdsoft.mods.nerdsoftkitchen.blockentity;

import com.nerdsoft.mods.nerdsoftkitchen.registry.blockentity.ModBlockEntities;
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

    private int nutrients;
    private int threshold;

    public OrganicSoilBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ORGANIC_SOIL.get(), pos, state);
    }

    public boolean isNourished() {
        return threshold > 0 && nutrients >= threshold;
    }

    /**
     * Registers one use of rotten flesh on the mushroom growing above this soil.
     * The threshold (3-9 uses) is rolled the first time nutrients are added.
     *
     * @return true if this use pushed the nutrient count past the threshold (i.e. the mushroom is now ready).
     */
    public boolean addNutrient(RandomSource random) {
        if (threshold <= 0) {
            threshold = MIN_NUTRIENT_THRESHOLD + random.nextInt(MAX_NUTRIENT_THRESHOLD - MIN_NUTRIENT_THRESHOLD + 1);
        }
        if (nutrients >= threshold) {
            return true;
        }
        nutrients++;
        setChanged();
        requestSync();
        return nutrients >= threshold;
    }

    public void reset() {
        nutrients = 0;
        threshold = 0;
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
        nutrients = tag.getInt(NUTRIENTS_KEY);
        threshold = tag.getInt(THRESHOLD_KEY);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt(NUTRIENTS_KEY, nutrients);
        tag.putInt(THRESHOLD_KEY, threshold);
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