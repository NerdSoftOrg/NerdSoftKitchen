package com.nerdsoft.mods.nerdsoftkitchen.crop;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
//? if <1.21.2 {
import net.minecraft.world.ItemInteractionResult;
//?}
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class TomatoCropBlock extends ModCropBlock {

    private static final int BASE_MAX_AGE = 4;

    private final MapCodec<TomatoCropBlock> codec;
    private final Supplier<? extends TomatoCropPoleBlock> poleBlockSupplier;

    public TomatoCropBlock(BlockBehaviour.Properties properties, VoxelShape[] shapes,
                           Supplier<? extends ItemLike> seedSupplier, Supplier<? extends TomatoCropPoleBlock> poleBlockSupplier) {
        super(properties, BASE_MAX_AGE, shapes, seedSupplier);
        this.poleBlockSupplier = poleBlockSupplier;
        this.codec = simpleCodec(props -> new TomatoCropBlock(props, shapes, seedSupplier, poleBlockSupplier));
    }

    @Override
    public @NotNull MapCodec<TomatoCropBlock> codec() {
        return this.codec;
    }

    @Override
    protected @NotNull IntegerProperty getAgeProperty() {
        return BlockStateProperties.AGE_4;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.AGE_4);
    }

    @Override
    //? if <1.21.2 {
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state,
                                                       //?} else
            //protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state,
                                                       @NotNull Level level, @NotNull BlockPos pos,
                                                       @NotNull Player player, @NotNull InteractionHand hand,
                                                       @NotNull BlockHitResult hitResult) {
        if (!stack.is(Items.STICK)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
        if (level.isClientSide()) {
            //? if <1.21.2 {
            return ItemInteractionResult.SUCCESS;
            //?} else
            //return InteractionResult.SUCCESS;
        }

        int age = state.getValue(this.getAgeProperty());
        TomatoCropPoleBlock poleBlock = this.poleBlockSupplier.get();

        level.setBlock(pos, TomatoCropPoleBlock.createLower(poleBlock, age), 3);
        TomatoCropPoleBlock.tryGrowOrSyncUpper(poleBlock, level, pos, BlockStateProperties.AGE_5, age);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        level.playSound(null, pos, SoundType.WOOD.getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
        ((ServerLevel) level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.STICK)),
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8, 0.2, 0.3, 0.2, 0.05);

        //? if <1.21.2 {
        return ItemInteractionResult.SUCCESS;
        //?} else
        //return InteractionResult.SUCCESS;
    }
}