package com.nerdsoft.mods.nerdsoftkitchen.block;

import com.mojang.serialization.MapCodec;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.OrganicSoilBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//? if <1.21.2 {
/*import net.minecraft.world.ItemInteractionResult;
*///?}

public class OrganicSoilBlock extends BaseEntityBlock {

    public static final MapCodec<OrganicSoilBlock> CODEC = simpleCodec(OrganicSoilBlock::new);
    private static final int MIN_BONUS_MUSHROOMS = 2;
    private static final int MAX_BONUS_MUSHROOMS = 3;

    public OrganicSoilBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull MapCodec<OrganicSoilBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new OrganicSoilBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return null;
    }

    @Override
    //? if <1.21.2 {
    /*protected @NotNull ItemInteractionResult useItemOn(
            *///?} else
            protected @NotNull InteractionResult useItemOn(
            @NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
            @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult
    ) {
        if (!stack.is(Items.ROTTEN_FLESH)) {
            //? if <1.21.2 {
            /*return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            *///?} else
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (!(level.getBlockState(pos.above()).getBlock() instanceof MushroomBlock)) {
            //? if <1.21.2 {
            /*return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            *///?} else
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (level.getBlockEntity(pos) instanceof OrganicSoilBlockEntity soilEntity && !soilEntity.isNourished()) {
            boolean nourished = soilEntity.addNutrient(level.getRandom());
            if (!level.isClientSide) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 0.8F);
                if (nourished) {
                    level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.4F, 1.4F);
                }
            }
            //? if <1.21.2 {
            /*return ItemInteractionResult.sidedSuccess(level.isClientSide);
            *///?} else
            return InteractionResult.SUCCESS;
        }

        //? if <1.21.2 {
        /*return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        *///?} else
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    //? if <1.21.2 {
    /*@Override
    protected void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock, @NotNull BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        handleMushroomHarvest(level, pos, neighborPos);
    }
    *///?} else {
    @Override
    protected void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock, @Nullable net.minecraft.world.level.redstone.Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
        handleMushroomHarvest(level, pos, pos.above());
    }
    //?}

    private void handleMushroomHarvest(Level level, BlockPos pos, BlockPos neighborPos) {
        if (level.isClientSide || !neighborPos.equals(pos.above())) {
            return;
        }
        if (!(level.getBlockEntity(pos) instanceof OrganicSoilBlockEntity soilEntity) || !soilEntity.isNourished()) {
            return;
        }
        // The mushroom above was just removed (broken/harvested) while nourished: grant the bonus and reset.
        if (level.getBlockState(neighborPos).getBlock() instanceof MushroomBlock) {
            return;
        }
        if (level instanceof ServerLevel serverLevel) {
            spawnBonusMushrooms(serverLevel, pos);
        }
        soilEntity.reset();
    }

    private void spawnBonusMushrooms(ServerLevel level, BlockPos pos) {
        // The specific mushroom type isn't tracked once broken, so award brown mushrooms as the common case.
        int count = MIN_BONUS_MUSHROOMS + level.getRandom().nextInt(MAX_BONUS_MUSHROOMS - MIN_BONUS_MUSHROOMS + 1);
        popResource(level, pos, new ItemStack(Items.BROWN_MUSHROOM, count));
    }
}