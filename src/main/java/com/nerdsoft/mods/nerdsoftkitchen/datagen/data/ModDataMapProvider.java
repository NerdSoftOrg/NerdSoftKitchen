package com.nerdsoft.mods.nerdsoftkitchen.datagen.data;

import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {

    // Matches vanilla's own ratios: seeds ~30%, raw crops/vegetables ~65%, prepared/cooked foods ~85%.
    private static final float SEED_CHANCE = 0.3F;
    private static final float RAW_CROP_CHANCE = 0.65F;
    private static final float PREPARED_FOOD_CHANCE = 0.85F;

    // 2400 ticks = smelts 3 items, same as a standard coal block being 1/3 of its 1600-tick coal value scaled up.
    private static final int ORGANIC_SOIL_BURN_TIME = 2400;

    public ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider provider) {
        Builder<Compostable, net.minecraft.world.item.Item> compostables = this.builder(NeoForgeDataMaps.COMPOSTABLES);

        compostables.add(ModItems.STRAWBERRY_SEEDS.getKey(), new Compostable(SEED_CHANCE), false);
        compostables.add(ModItems.TOMATO_SEEDS.getKey(), new Compostable(SEED_CHANCE), false);
        compostables.add(ModItems.LETTUCE_SEEDS.getKey(), new Compostable(SEED_CHANCE), false);
        compostables.add(ModItems.PURPLE_ONION_SEEDS.getKey(), new Compostable(SEED_CHANCE), false);

        compostables.add(ModItems.WILD_STRAWBERRY.getKey(), new Compostable(SEED_CHANCE), false);
        compostables.add(ModItems.WILD_TOMATO.getKey(), new Compostable(SEED_CHANCE), false);
        compostables.add(ModItems.WILD_LETTUCE.getKey(), new Compostable(SEED_CHANCE), false);
        compostables.add(ModItems.WILD_PURPLE_ONION.getKey(), new Compostable(SEED_CHANCE), false);

        compostables.add(ModItems.STRAWBERRY.getKey(), new Compostable(RAW_CROP_CHANCE), false);
        compostables.add(ModItems.TOMATO.getKey(), new Compostable(RAW_CROP_CHANCE), false);
        compostables.add(ModItems.LETTUCE.getKey(), new Compostable(RAW_CROP_CHANCE), false);
        compostables.add(ModItems.PURPLE_ONION.getKey(), new Compostable(RAW_CROP_CHANCE), false);

        compostables.add(ModItems.SALAD.getKey(), new Compostable(PREPARED_FOOD_CHANCE), false);

        Builder<FurnaceFuel, net.minecraft.world.item.Item> fuels = this.builder(NeoForgeDataMaps.FURNACE_FUELS);
        fuels.add(ModItems.ORGANIC_SOIL.getKey(), new FurnaceFuel(ORGANIC_SOIL_BURN_TIME), false);
    }
}