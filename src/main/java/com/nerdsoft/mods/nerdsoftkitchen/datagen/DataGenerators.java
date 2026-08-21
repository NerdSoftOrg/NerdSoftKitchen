package com.nerdsoft.mods.nerdsoftkitchen.datagen;

import com.nerdsoft.mods.nerdsoftkitchen.datagen.advancement.ModAdvancementProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.block.ModBlockStateProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.block.ModBlockTagsProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.data.ModDamageTypeProvider;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.data.ModDamageTypeTagsProvider;
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
import com.nerdsoft.mods.nerdsoftkitchen.registry.world.worldgen.ModPlacedFeatures;
import com.nerdsoft.mods.nerdsoftkitchen.util.NerdSoftKitchenLogger;
//? if >=1.21.2 {
/*import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.DataMapProvider;
import java.util.Set;
*///?}
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

@SuppressWarnings("CommentedOutCode")
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

        //? if <1.21.2 {
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
            generator.addProvider(event.includeServer(), new ModDamageTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
        }
        //?} else {
        /*DataProvider.Factory<ModBlockStateProvider> blockStateFactory = output -> new ModBlockStateProvider(output, existingFileHelper);
        generator.addProvider(event.includeClient(), blockStateFactory);

        DataProvider.Factory<ModItemModelProvider> itemModelFactory = output -> new ModItemModelProvider(output, existingFileHelper);
        generator.addProvider(event.includeClient(), itemModelFactory);

        DataProvider.Factory<ModEnUsLanguageProvider> enUsLangFactory = ModEnUsLanguageProvider::new;
        generator.addProvider(event.includeClient(), enUsLangFactory);

        DataProvider.Factory<ModEsEsLanguageProvider> esEsLangFactory = ModEsEsLanguageProvider::new;
        generator.addProvider(event.includeClient(), esEsLangFactory);

        DataProvider.Factory<ModSoundDefinitionsProvider> soundFactory = output -> new ModSoundDefinitionsProvider(output, existingFileHelper);
        generator.addProvider(event.includeClient(), soundFactory);

        DataProvider.Factory<RecipeProvider.Runner> recipeFactory = output -> new ModRecipeProvider.Runner(output, lookupProvider);
        generator.addProvider(event.includeServer(), recipeFactory);

        DataProvider.Factory<ModLootTableProvider> lootTableFactory = output -> new ModLootTableProvider(output, lookupProvider);
        generator.addProvider(event.includeServer(), lootTableFactory);

        DataProvider.Factory<ModAdvancementProvider> advancementFactory = output -> new ModAdvancementProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), advancementFactory);

        DataProvider.Factory<ModDataMapProvider> dataMapFactory = output -> new ModDataMapProvider(output, lookupProvider);
        generator.addProvider(event.includeServer(), dataMapFactory);

        RegistrySetBuilder datapackBuilder = new RegistrySetBuilder()
                .add(Registries.DAMAGE_TYPE, ModDamageTypeProvider::bootstrap)
                .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
                .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

        DataProvider.Factory<DatapackBuiltinEntriesProvider> datapackFactory = output -> new DatapackBuiltinEntriesProvider(output, lookupProvider, datapackBuilder, Set.of(com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen.MOD_ID));
        generator.addProvider(event.includeServer(), datapackFactory);

        if (event.includeServer()) {
            ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(packOutput, lookupProvider, existingFileHelper);
            generator.addProvider(true, (DataProvider.Factory<ModBlockTagsProvider>) output -> blockTagsProvider);

            DataProvider.Factory<ModItemTagsProvider> itemTagsFactory = output -> new ModItemTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper);
            generator.addProvider(true, itemTagsFactory);

            DataProvider.Factory<ModDamageTypeTagsProvider> damageTypeTagsFactory = output -> new ModDamageTypeTagsProvider(output, lookupProvider, existingFileHelper);
            generator.addProvider(true, damageTypeTagsFactory);
        }
        *///?}
    }
}
