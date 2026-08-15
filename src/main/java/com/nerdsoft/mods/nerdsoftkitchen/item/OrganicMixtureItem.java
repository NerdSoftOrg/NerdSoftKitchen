package com.nerdsoft.mods.nerdsoftkitchen.item;

import com.nerdsoft.mods.nerdsoftkitchen.block.FertileFarmlandBlock;
import com.nerdsoft.mods.nerdsoftkitchen.registry.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class OrganicMixtureItem extends Item {

    private static final int CENTER_FERTILITY = FertileFarmlandBlock.MAX_FERTILITY;
    private static final int EDGE_FERTILITY = FertileFarmlandBlock.MAX_FERTILITY - 1;
    private static final int CORNER_FERTILITY = FertileFarmlandBlock.MAX_FERTILITY - 2;

    public OrganicMixtureItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos centerPos = context.getClickedPos();
        BlockState centerState = level.getBlockState(centerPos);

        if (!centerState.is(Blocks.FARMLAND)) {
            return InteractionResult.PASS;
        }

        if (level instanceof ServerLevel serverLevel) {
            fertilizeArea(serverLevel, centerPos);
            spawnFertilizeParticles(serverLevel, centerPos);
            serverLevel.playSound(null, centerPos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        Player player = context.getPlayer();
        if (player == null || !player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    private void fertilizeArea(ServerLevel level, BlockPos center) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos pos = center.offset(dx, 0, dz);
                BlockState state = level.getBlockState(pos);
                if (!state.is(Blocks.FARMLAND) && !state.is(ModBlocks.FERTILE_FARMLAND.get())) {
                    continue;
                }

                int fertility = fertilityFor(dx, dz);
                int currentFertility = state.is(ModBlocks.FERTILE_FARMLAND.get())
                        ? state.getValue(FertileFarmlandBlock.FERTILITY)
                        : 0;
                if (fertility <= currentFertility) {
                    continue;
                }

                BlockState newState = ModBlocks.FERTILE_FARMLAND.get().defaultBlockState()
                        .setValue(FertileFarmlandBlock.FERTILITY, fertility);
                if (state.hasProperty(BlockStateProperties.MOISTURE)) {
                    newState = newState.setValue(BlockStateProperties.MOISTURE, state.getValue(BlockStateProperties.MOISTURE));
                }
                level.setBlock(pos, newState, 3);
            }
        }
    }

    private int fertilityFor(int dx, int dz) {
        if (dx == 0 && dz == 0) {
            return CENTER_FERTILITY;
        }
        if (dx == 0 || dz == 0) {
            return EDGE_FERTILITY;
        }
        return CORNER_FERTILITY;
    }

    private void spawnFertilizeParticles(ServerLevel level, BlockPos center) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos pos = center.offset(dx, 0, dz);
                if (!level.getBlockState(pos).is(ModBlocks.FERTILE_FARMLAND.get())) {
                    continue;
                }
                level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                        pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                        4, 0.3, 0.2, 0.3, 0.0);
            }
        }
    }
}