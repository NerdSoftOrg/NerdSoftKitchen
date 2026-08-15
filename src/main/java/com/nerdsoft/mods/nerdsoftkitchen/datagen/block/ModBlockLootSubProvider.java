package com.nerdsoft.mods.nerdsoftkitchen.datagen.block;

import com.nerdsoft.mods.nerdsoftkitchen.crop.TomatoCropPoleBlock;
import com.nerdsoft.mods.nerdsoftkitchen.registry.block.ModBlocks;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ModBlockLootSubProvider extends BlockLootSubProvider {

    private static final float SEED_YIELD_MIN = 1.0F;
    private static final float SEED_YIELD_MAX = 2.0F;

    private static final float TOMATO_YIELD_MIN = 1.0F;
    private static final float TOMATO_YIELD_MAX = 2.0F;
    private static final float POLE_YIELD_MULTIPLIER = 1.75F;
    private static final float POLE_YIELD_MIN = (float) Math.floor(TOMATO_YIELD_MIN * POLE_YIELD_MULTIPLIER);
    private static final float POLE_YIELD_MAX = (float) Math.floor(TOMATO_YIELD_MAX * POLE_YIELD_MULTIPLIER);

    private static final float STRAWBERRY_YIELD_MIN = 1.0F;
    private static final float STRAWBERRY_YIELD_MAX = 3.0F;
    private static final float LETTUCE_YIELD_MIN = 1.0F;
    private static final float LETTUCE_YIELD_MAX = 2.0F;
    private static final float PURPLE_ONION_YIELD_MIN = 1.0F;
    private static final float PURPLE_ONION_YIELD_MAX = 2.0F;

    private static final float WILD_YIELD_MIN = 1.0F;
    private static final float WILD_YIELD_MAX = 3.0F;

    public ModBlockLootSubProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.GRILL_TABLE.get());
        dropSelf(ModBlocks.GRILL_TABLE_SOUL.get());
        dropSelf(ModBlocks.CUTTING_BOARD.get());
        dropSelf(ModBlocks.ORGANIC_SOIL.get());

        add(ModBlocks.FERTILE_FARMLAND.get(), block -> LootTable.lootTable()
                .withPool(LootPool.lootPool().add(LootItem.lootTableItem(Blocks.DIRT))));

        add(ModBlocks.WILD_STRAWBERRY.get(), block -> createWildCropDrops(ModItems.STRAWBERRY.get(), ModItems.STRAWBERRY_SEEDS.get()));
        add(ModBlocks.WILD_TOMATO.get(), block -> createWildCropDrops(ModItems.TOMATO.get(), ModItems.TOMATO_SEEDS.get()));
        add(ModBlocks.WILD_LETTUCE.get(), block -> createWildCropDrops(ModItems.LETTUCE.get(), ModItems.LETTUCE_SEEDS.get()));
        add(ModBlocks.WILD_PURPLE_ONION.get(), block -> createWildCropDrops(ModItems.PURPLE_ONION.get(), ModItems.PURPLE_ONION_SEEDS.get()));

        add(ModBlocks.STRAWBERRY_CROP.get(), block -> createMatureCropDrops(block, BlockStateProperties.AGE_3,
                ModItems.STRAWBERRY.get(), STRAWBERRY_YIELD_MIN, STRAWBERRY_YIELD_MAX, ModItems.STRAWBERRY_SEEDS.get()));
        add(ModBlocks.LETTUCE_CROP.get(), block -> createMatureCropDrops(block, BlockStateProperties.AGE_3,
                ModItems.LETTUCE.get(), LETTUCE_YIELD_MIN, LETTUCE_YIELD_MAX, ModItems.LETTUCE_SEEDS.get()));
        add(ModBlocks.PURPLE_ONION_CROP.get(), block -> createMatureCropDrops(block, BlockStateProperties.AGE_3,
                ModItems.PURPLE_ONION.get(), PURPLE_ONION_YIELD_MIN, PURPLE_ONION_YIELD_MAX, ModItems.PURPLE_ONION_SEEDS.get()));

        add(ModBlocks.TOMATO_CROP.get(), block -> createMatureCropDrops(block, BlockStateProperties.AGE_4,
                ModItems.TOMATO.get(), TOMATO_YIELD_MIN, TOMATO_YIELD_MAX, ModItems.TOMATO_SEEDS.get()));

        add(ModBlocks.TOMATO_CROP_POLE.get(), block -> {
            LootItemCondition.Builder isLower = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TomatoCropPoleBlock.HALF, DoubleBlockHalf.LOWER));
            LootItemCondition.Builder mature = isMaxAge(block, BlockStateProperties.AGE_5);
            LootItemCondition.Builder isLowerAndMature = isLower.and(mature);

            return LootTable.lootTable()
                    .withPool(LootPool.lootPool().when(isLowerAndMature)
                            .add(LootItem.lootTableItem(ModItems.TOMATO.get())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(POLE_YIELD_MIN, POLE_YIELD_MAX)))))
                    .withPool(LootPool.lootPool().when(isLower)
                            .add(LootItem.lootTableItem(ModItems.TOMATO_SEEDS.get())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(SEED_YIELD_MIN, SEED_YIELD_MAX)))));
        });
    }

    private LootTable.Builder createMatureCropDrops(Block block, IntegerProperty ageProperty,
                                                    ItemLike cropItem, float cropMin, float cropMax, ItemLike seedItem) {
        LootItemCondition.Builder mature = isMaxAge(block, ageProperty);

        return LootTable.lootTable()
                .withPool(LootPool.lootPool().when(mature)
                        .add(LootItem.lootTableItem(cropItem)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(cropMin, cropMax)))))
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(seedItem)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(SEED_YIELD_MIN, SEED_YIELD_MAX)))));
    }

    private LootItemCondition.Builder isMaxAge(Block block, IntegerProperty ageProperty) {
        int maxAge = ageProperty.getPossibleValues().stream().mapToInt(Integer::intValue).max().orElseThrow();
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ageProperty, maxAge));
    }

    private LootTable.Builder createWildCropDrops(ItemLike cropItem, ItemLike seedItem) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(cropItem)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(WILD_YIELD_MIN, WILD_YIELD_MAX)))))
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(seedItem)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(SEED_YIELD_MIN, SEED_YIELD_MAX)))));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return List.of(
                ModBlocks.GRILL_TABLE.get(),
                ModBlocks.GRILL_TABLE_SOUL.get(),
                ModBlocks.CUTTING_BOARD.get(),
                ModBlocks.WILD_STRAWBERRY.get(),
                ModBlocks.WILD_TOMATO.get(),
                ModBlocks.WILD_LETTUCE.get(),
                ModBlocks.WILD_PURPLE_ONION.get(),
                ModBlocks.STRAWBERRY_CROP.get(),
                ModBlocks.LETTUCE_CROP.get(),
                ModBlocks.PURPLE_ONION_CROP.get(),
                ModBlocks.TOMATO_CROP.get(),
                ModBlocks.TOMATO_CROP_POLE.get(),
                ModBlocks.FERTILE_FARMLAND.get(),
                ModBlocks.ORGANIC_SOIL.get()
        );
    }
}