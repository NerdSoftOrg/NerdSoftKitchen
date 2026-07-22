package com.nerdsoft.mods.nerdsoftkitchen.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public abstract class HarvestableCropBlock extends ModCropBlock {

    private final int harvestResetAge;
    private final int harvestCount;
    private final Supplier<? extends ItemLike> harvestItemSupplier;

    protected HarvestableCropBlock(BlockBehaviour.Properties properties, int maxAge, VoxelShape[] shapes,
                                   Supplier<? extends ItemLike> seedSupplier, Supplier<? extends ItemLike> harvestItemSupplier,
                                   int harvestResetAge, int harvestCount) {
        super(properties, maxAge, shapes, seedSupplier);
        this.harvestItemSupplier = harvestItemSupplier;
        this.harvestResetAge = harvestResetAge;
        this.harvestCount = harvestCount;
    }

    @Nullable
    protected abstract BlockPos resolveHarvestOriginPos(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos);

    @NotNull
    protected BlockState resolveHarvestOriginState(@NotNull Level level, @NotNull BlockPos originPos, @NotNull BlockState state, @NotNull BlockPos pos) {
        return originPos == pos ? state : level.getBlockState(originPos);
    }

    protected void afterHarvest(@NotNull ServerLevel level, @NotNull BlockPos originPos, @NotNull IntegerProperty ageProperty) {
    }

    @Override
    protected final @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level,
                                                              @NotNull BlockPos pos, @NotNull Player player,
                                                              @NotNull BlockHitResult hitResult) {
        BlockPos originPos = resolveHarvestOriginPos(state, level, pos);
        if (originPos == null) {
            return InteractionResult.PASS;
        }

        BlockState originState = resolveHarvestOriginState(level, originPos, state, pos);
        IntegerProperty ageProperty = this.getAgeProperty();

        if (originState.getValue(ageProperty) < this.getMaxAge()) {
            return InteractionResult.PASS;
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        popResource(serverLevel, originPos, new ItemStack(this.harvestItemSupplier.get().asItem(), this.harvestCount));
        serverLevel.setBlock(originPos, originState.setValue(ageProperty, this.harvestResetAge), 2);
        afterHarvest(serverLevel, originPos, ageProperty);

        serverLevel.playSound(null, originPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS,
                1.0F, 0.8F + serverLevel.getRandom().nextFloat() * 0.4F);

        return InteractionResult.SUCCESS;
    }
}