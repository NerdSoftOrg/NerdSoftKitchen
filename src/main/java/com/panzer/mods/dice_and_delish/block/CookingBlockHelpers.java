package com.panzer.mods.dice_and_delish.block;

import com.panzer.mods.dice_and_delish.blockentity.AbstractCookingBlockEntity;
import com.panzer.mods.dice_and_delish.registry.data.ModDamageTypes;
import com.panzer.mods.dice_and_delish.registry.world.damagesource.BlockDamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
//? if >=1.21.2 {
/*import net.minecraft.server.level.ServerLevel;
*///?}

final class CookingBlockHelpers {

    private static final float STEP_DAMAGE = 1.0F;

    private CookingBlockHelpers() {
    }

    static void applyStepBurn(Level level, BlockPos pos, BlockState state, Entity entity,
                              BooleanProperty litProperty, Block source) {
        if (level.isClientSide
                || !state.getValue(litProperty)
                || !(entity instanceof LivingEntity living)
                || living.fireImmune()
                || !entity.getOnPos().equals(pos)) {
            return;
        }

        //? if <1.21.2 {
        Holder<DamageType> cookwareBurnHolder = level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(ModDamageTypes.COOKWARE_BURN);
        //?} else {
        /*Holder<DamageType> cookwareBurnHolder = level.registryAccess()
                .lookupOrThrow(Registries.DAMAGE_TYPE)
                .getOrThrow(ModDamageTypes.COOKWARE_BURN);
        *///?}

        //? if <1.21.2 {
        living.hurt(new BlockDamageSource(cookwareBurnHolder, source), STEP_DAMAGE);
        //?} else
        //living.hurtServer((ServerLevel) level, new BlockDamageSource(cookwareBurnHolder, source), STEP_DAMAGE);
    }

    static void dropContentsOnRemoval(BlockState state, Level level, BlockPos pos, BlockState newState) {
        if (state.is(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AbstractCookingBlockEntity cookingEntity) {
            cookingEntity.dropContents(level, pos);
        }
    }
}
