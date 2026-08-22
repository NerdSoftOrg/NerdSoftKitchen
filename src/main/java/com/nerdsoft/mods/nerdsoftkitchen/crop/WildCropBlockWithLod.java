package com.nerdsoft.mods.nerdsoftkitchen.crop;

import com.mojang.serialization.MapCodec;
import com.nerdsoft.mods.nerdsoftkitchen.lod.LodBlock;
import com.nerdsoft.mods.nerdsoftkitchen.lod.LodConfig;
import com.nerdsoft.mods.nerdsoftkitchen.lod.LodProperty;
import com.nerdsoft.mods.nerdsoftkitchen.lod.LodRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class WildCropBlockWithLod extends WildCropBlock implements LodBlock {

    public static final int DEFAULT_LOD_DISTANCE_CHUNKS = 1; // Cambiado de 3 a 1
    private static final ThreadLocal<IntegerProperty> PENDING_LOD_PROPERTY = new ThreadLocal<>();
    private final LodConfig.Entry lodEntry;
    private final IntegerProperty lod;

    private final MapCodec<? extends WildCropBlockWithLod> codec;

    public WildCropBlockWithLod(BlockBehaviour.Properties properties, VoxelShape shape, String lodKey) {
        this(properties, shape, lodKey, DEFAULT_LOD_DISTANCE_CHUNKS);
    }

    public WildCropBlockWithLod(BlockBehaviour.Properties properties, VoxelShape shape, String lodKey, int lodDistanceChunks) {
        this(properties, shape, lodKey, lodDistanceChunks, LodRegistry.register(lodKey, lodDistanceChunks));
    }

    private WildCropBlockWithLod(BlockBehaviour.Properties properties, VoxelShape shape, String lodKey,
                                 int lodDistanceChunks, LodConfig.Entry lodEntry) {
        this(properties, shape, lodKey, lodDistanceChunks, lodEntry, setPendingLodProperty(lodEntry));
    }

    private WildCropBlockWithLod(BlockBehaviour.Properties properties, VoxelShape shape, String lodKey,
                                 int lodDistanceChunks, LodConfig.Entry lodEntry, IntegerProperty lod) {
        super(properties, shape);
        this.lodEntry = lodEntry;
        this.lod = lod;
        this.codec = simpleCodec(props -> new WildCropBlockWithLod(props, shape, lodKey, lodDistanceChunks));
        PENDING_LOD_PROPERTY.remove();
        this.registerDefaultState(this.stateDefinition.any().setValue(lod, 0));
    }

    private static IntegerProperty setPendingLodProperty(LodConfig.Entry entry) {
        IntegerProperty property = LodProperty.create(entry.maxTier());
        PENDING_LOD_PROPERTY.set(property);
        return property;
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PENDING_LOD_PROPERTY.get());
    }

    @Override
    public @NotNull MapCodec<? extends WildCropBlockWithLod> codec() {
        return this.codec;
    }

    @Override
    public IntegerProperty lodProperty() {
        return lod;
    }

    @Override
    public LodConfig.Entry lodEntry() {
        return lodEntry;
    }
}
