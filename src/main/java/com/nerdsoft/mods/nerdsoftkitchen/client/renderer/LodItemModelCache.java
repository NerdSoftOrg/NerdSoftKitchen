package com.nerdsoft.mods.nerdsoftkitchen.client.renderer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class LodItemModelCache {

    private static final Direction FRONT_FACE = Direction.SOUTH;

    private static final Map<BakedModel, BakedModel> CACHE = new ConcurrentHashMap<>();

    private LodItemModelCache() {
    }

    static BakedModel singleQuad(BakedModel original) {
        return CACHE.computeIfAbsent(original, SingleFaceModel::new);
    }

    private static final class SingleFaceModel extends BakedModelWrapper<BakedModel> {

        SingleFaceModel(BakedModel originalModel) {
            super(originalModel);
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand,
                                         ModelData data, @Nullable RenderType renderType) {
            if (side != null) {
                return side == FRONT_FACE
                        ? originalModel.getQuads(state, side, rand, data, renderType)
                        : List.of();
            }
            List<BakedQuad> all = originalModel.getQuads(state, null, rand, data, renderType);
            List<BakedQuad> kept = new ArrayList<>(all.size());
            for (BakedQuad quad : all) {
                if (quad.getDirection() == FRONT_FACE) {
                    kept.add(quad);
                }
            }
            return kept;
        }
    }
}