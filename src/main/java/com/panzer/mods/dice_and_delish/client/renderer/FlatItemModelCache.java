package com.panzer.mods.dice_and_delish.client.renderer;

//? if <1.21.2 {
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//?} else
//import net.minecraft.client.resources.model.BakedModel;

public final class FlatItemModelCache {

    //? if <1.21.2 {
    private static final Map<BakedModel, BakedModel> CACHE = new ConcurrentHashMap<>();
    //?}

    private FlatItemModelCache() {}

    public static BakedModel flatten(BakedModel original) {
        //? if <1.21.2 {
        if (!BillboardConfig.isEnabled()) {
            return original;
        }
        if (original.isGui3d()) {
            return original;
        }
        return CACHE.computeIfAbsent(original, FlatBillboardModel::new);
        //?} else {
        /*return original;
         *///?}
    }

    public static void clear() {
        //? if <1.21.2 {
        CACHE.clear();
        //?}
    }

    //? if <1.21.2 {
    private static final class FlatBillboardModel extends BakedModelWrapper<BakedModel> {

        private volatile List<BakedQuad> cachedFlatQuads;

        FlatBillboardModel(BakedModel originalModel) {
            super(originalModel);
        }

        private List<BakedQuad> buildFlatQuads() {
            RandomSource rand = RandomSource.create(42L);
            List<BakedQuad> allQuads = new ArrayList<>();

            allQuads.addAll(originalModel.getQuads(null, null, rand, ModelData.EMPTY, null));
            for (Direction d : Direction.values()) {
                allQuads.addAll(originalModel.getQuads(null, d, rand, ModelData.EMPTY, null));
            }

            List<BakedQuad> flatQuads = new ArrayList<>();
            for (BakedQuad quad : allQuads) {
                Direction dir = quad.getDirection();
                if (dir != Direction.UP && dir != Direction.DOWN && dir != Direction.WEST && dir != Direction.EAST) {
                    flatQuads.add(quad);
                }
            }

            return flatQuads.isEmpty() ? allQuads : flatQuads;
        }

        private List<BakedQuad> getFlatQuads() {
            List<BakedQuad> cached = cachedFlatQuads;
            if (cached != null) {
                return cached;
            }
            List<BakedQuad> built = buildFlatQuads();
            cachedFlatQuads = built;
            return built;
        }

        @Override
        public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                                 @NotNull RandomSource rand, @NotNull ModelData data,
                                                 @Nullable RenderType renderType) {
            if (side == null) {
                return getFlatQuads();
            }
            return List.of();
        }

        @Override
        @Deprecated
        public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
            return getQuads(state, side, rand, ModelData.EMPTY, null);
        }

        @Override
        public List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous) {
            return List.of(this);
        }

        @Override
        public boolean usesBlockLight() {
            return originalModel.usesBlockLight();
        }

        @Override
        public boolean isGui3d() {
            return false;
        }

        @Override
        public boolean isCustomRenderer() {
            return false;
        }

        @Override
        public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
            return originalModel.getRenderTypes(state, rand, data);
        }

        @Override
        public @NotNull List<RenderType> getRenderTypes(@NotNull ItemStack itemStack, boolean fabulous) {
            return originalModel.getRenderTypes(itemStack, fabulous);
        }
    }
    //?}
}
