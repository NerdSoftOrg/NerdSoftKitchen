package com.nerdsoft.mods.nerdsoftkitchen.blockentity;

import com.nerdsoft.mods.nerdsoftkitchen.lod.LodBlock;
import com.nerdsoft.mods.nerdsoftkitchen.lod.LodResolver;
import com.nerdsoft.mods.nerdsoftkitchen.registry.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CuttingBoardBlockEntity extends BlockEntity {

    private static final String ITEM_KEY = "Item";
    private ItemStack storedItem = ItemStack.EMPTY;

    public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CUTTING_BOARD.get(), pos, state);
    }

    public boolean isEmpty() {
        return storedItem.isEmpty();
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, CuttingBoardBlockEntity board) {
        if (!LodResolver.isDue(pos, level.getGameTime())) {
            return;
        }
        if (state.getBlock() instanceof LodBlock lodBlock) {
            LodResolver.resolveAndApply(level, pos, state, lodBlock);
        }
    }

    public @NotNull ItemStack getStoredItem() {
        return storedItem;
    }

    public void setStoredItem(ItemStack stack) {
        this.storedItem = stack;
        setChanged();
        requestSync();
    }

    public ItemStack clearStoredItem() {
        ItemStack removed = storedItem;
        storedItem = ItemStack.EMPTY;
        setChanged();
        requestSync();
        return removed;
    }

    private void requestSync() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        NonNullList<ItemStack> holder = NonNullList.withSize(1, ItemStack.EMPTY);
        if (tag.contains(ITEM_KEY, 10)) {
            ContainerHelper.loadAllItems(tag.getCompound(ITEM_KEY), holder, registries);
        }
        storedItem = holder.getFirst();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        NonNullList<ItemStack> holder = NonNullList.withSize(1, ItemStack.EMPTY);
        holder.set(0, storedItem);
        tag.put(ITEM_KEY, ContainerHelper.saveAllItems(new CompoundTag(), holder, registries));
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