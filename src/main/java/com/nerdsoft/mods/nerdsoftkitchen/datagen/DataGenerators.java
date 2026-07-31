package com.nerdsoft.mods.nerdsoftkitchen.datagen;

import com.nerdsoft.mods.nerdsoftkitchen.datagen.advancement.ModAdvancementProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.block.ModBlockStateProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.block.ModBlockTagsProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.data.ModDamageTypeProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.data.ModDataMapProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.data.ModLootTableProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.item.ModItemModelProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.item.ModItemTagsProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.lang.ModEnUsLanguageProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.lang.ModEsEsLanguageProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.recipe.ModRecipeProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.sound.ModSoundDefinitionsProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.worldgen.ModBiomeModifiers;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.worldgen.ModConfiguredFeatures;
import com.nerdsoft.mods.nerdsoftkitchen.registry.worldgen.ModPlacedFeatures;
import com.nerdsoft.mods.nerdsoftkitchen.util.NerdSoftKitchenLogger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.concurrent.CompletableFuture;

public final class DataGenerators {

    private DataGenerators() {
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener(DataGenerators::gatherData);
        NerdSoftKitchenLogger.info("Data Generators listener attached.");
    }

    private static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModEnUsLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new ModEsEsLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new ModSoundDefinitionsProvider(packOutput, existingFileHelper));

        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ModLootTableProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ModAdvancementProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModDataMapProvider(packOutput, lookupProvider));

        event.createDatapackRegistryObjects(new RegistrySetBuilder()
                .add(Registries.DAMAGE_TYPE, ModDamageTypeProvider::bootstrap)
                .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
                .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap));

        if (event.includeServer()) {
            event.createBlockAndItemTags(
                    (output, lookup) -> new ModBlockTagsProvider(output, lookup, existingFileHelper),
                    (output, lookup, blockTags) -> new ModItemTagsProvider(output, lookup, blockTags, existingFileHelper)
            );
        }
    }
}