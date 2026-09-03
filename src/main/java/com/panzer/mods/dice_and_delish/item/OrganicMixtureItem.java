package com.panzer.mods.dice_and_delish.item;

import com.panzer.mods.dice_and_delish.block.FertileFarmlandBlock;
import com.panzer.mods.dice_and_delish.perf.PackedPos;
import com.panzer.mods.dice_and_delish.registry.block.ModBlocks;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class OrganicMixtureItem extends Item {

    private static final int MAX_RINGS = FertileFarmlandBlock.MAX_FERTILITY;

    // 8-connectivity (matches the old fixed grid's diagonal inclusion), expressed as raw dx/dz
    // pairs rather than Direction so the gap-jump math (2x each offset) stays simple integer math.
    private static final int[] NEIGHBOR_DX = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] NEIGHBOR_DZ = {-1, 0, 1, -1, 1, -1, 0, 1};

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
            LongArrayList affected = fertilizeArea(serverLevel, centerPos);
            spawnFertilizeParticles(serverLevel, affected, centerPos.getY());
            serverLevel.playSound(null, centerPos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        Player player = context.getPlayer();
        if (player == null || !player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    private LongArrayList fertilizeArea(ServerLevel level, BlockPos center) {
        long centerPacked = PackedPos.pack(center.getX(), center.getY(), center.getZ());

        LongOpenHashSet visited = new LongOpenHashSet();
        LongArrayList currentRing = new LongArrayList();
        LongArrayList nextRing = new LongArrayList();
        LongArrayList allTouched = new LongArrayList();

        visited.add(centerPacked);
        currentRing.add(centerPacked);
        allTouched.add(centerPacked);
        applyFertility(level, center.getX(), center.getY(), center.getZ(), FertileFarmlandBlock.MAX_FERTILITY);

        for (int ring = 1; ring <= MAX_RINGS; ring++) {
            int fertility = FertileFarmlandBlock.MAX_FERTILITY - ring;
            if (fertility <= 0) {
                break;
            }

            nextRing.clear();
            long[] frontierElements = currentRing.elements();
            int frontierSize = currentRing.size();

            for (int i = 0; i < frontierSize; i++) {
                long fromPacked = frontierElements[i];
                int fromX = PackedPos.unpackX(fromPacked);
                int fromY = PackedPos.unpackY(fromPacked);
                int fromZ = PackedPos.unpackZ(fromPacked);

                for (int n = 0; n < NEIGHBOR_DX.length; n++) {
                    int dx = NEIGHBOR_DX[n];
                    int dz = NEIGHBOR_DZ[n];

                    long reached = tryReachNeighbor(level, fromX, fromY, fromZ, dx, dz, visited);
                    if (reached != Long.MIN_VALUE) {
                        visited.add(reached);
                        nextRing.add(reached);
                        allTouched.add(reached);
                        applyFertility(level, PackedPos.unpackX(reached), fromY, PackedPos.unpackZ(reached), fertility);
                    }
                }
            }

            LongArrayList swap = currentRing;
            currentRing = nextRing;
            nextRing = swap;

            if (currentRing.isEmpty()) {
                break;
            }
        }

        return allTouched;
    }

    private long tryReachNeighbor(ServerLevel level, int fromX, int fromY, int fromZ, int dx, int dz, LongOpenHashSet visited) {
        int directX = fromX + dx;
        int directZ = fromZ + dz;
        BlockPos directPos = new BlockPos(directX, fromY, directZ);
        BlockState directState = level.getBlockState(directPos);

        if (isFarmlandLike(directState)) {
            long packed = PackedPos.pack(directX, fromY, directZ);
            return visited.contains(packed) ? Long.MIN_VALUE : packed;
        }

        // Gap-jump, the immediate neighbor isn't farmland (a path, water, a fence post, etc.)
        int gappedX = fromX + dx * 2;
        int gappedZ = fromZ + dz * 2;
        BlockPos gappedPos = new BlockPos(gappedX, fromY, gappedZ);
        BlockState gappedState = level.getBlockState(gappedPos);

        if (isFarmlandLike(gappedState)) {
            long packed = PackedPos.pack(gappedX, fromY, gappedZ);
            return visited.contains(packed) ? Long.MIN_VALUE : packed;
        }

        return Long.MIN_VALUE;
    }

    private boolean isFarmlandLike(BlockState state) {
        return state.is(Blocks.FARMLAND) || state.is(ModBlocks.FERTILE_FARMLAND.get());
    }

    private void applyFertility(ServerLevel level, int x, int y, int z, int fertility) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = level.getBlockState(pos);

        int currentFertility = state.is(ModBlocks.FERTILE_FARMLAND.get())
                ? state.getValue(FertileFarmlandBlock.FERTILITY)
                : 0;
        if (fertility <= currentFertility) {
            return;
        }

        BlockState newState = ModBlocks.FERTILE_FARMLAND.get().defaultBlockState()
                .setValue(FertileFarmlandBlock.FERTILITY, fertility);
        if (state.hasProperty(BlockStateProperties.MOISTURE)) {
            newState = newState.setValue(BlockStateProperties.MOISTURE, state.getValue(BlockStateProperties.MOISTURE));
        }
        level.setBlock(pos, newState, 3);
    }

    private void spawnFertilizeParticles(ServerLevel level, LongArrayList touchedPositions, int y) {
        long[] elements = touchedPositions.elements();
        int size = touchedPositions.size();
        for (int i = 0; i < size; i++) {
            long packed = elements[i];
            int x = PackedPos.unpackX(packed);
            int z = PackedPos.unpackZ(packed);
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    x + 0.5, y + 1.0, z + 0.5,
                    4, 0.3, 0.2, 0.3, 0.0);
        }
    }
}
