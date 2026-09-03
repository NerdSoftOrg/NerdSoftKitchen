package com.panzer.mods.dice_and_delish;

import com.panzer.mods.dice_and_delish.datagen.DataGenerators;
import com.panzer.mods.dice_and_delish.registry.block.ModBlocks;
import com.panzer.mods.dice_and_delish.registry.blockentity.ModBlockEntities;
import com.panzer.mods.dice_and_delish.registry.data.ModCapabilities;
import com.panzer.mods.dice_and_delish.registry.data.ModCustomStats;
import com.panzer.mods.dice_and_delish.registry.data.ModDataComponents;
import com.panzer.mods.dice_and_delish.registry.data.ModDamageTypes;
import com.panzer.mods.dice_and_delish.registry.gui.ModCreativeTabs;
import com.panzer.mods.dice_and_delish.registry.gui.ModMenuTypes;
import com.panzer.mods.dice_and_delish.registry.tags.ModItemTags;
import com.panzer.mods.dice_and_delish.registry.item.ModItems;
import com.panzer.mods.dice_and_delish.registry.recipe.ModIngredientTypes;
import com.panzer.mods.dice_and_delish.registry.recipe.ModRecipeSerializers;
import com.panzer.mods.dice_and_delish.registry.recipe.ModRecipeTypes;
import com.panzer.mods.dice_and_delish.registry.sound.ModSounds;
import com.panzer.mods.dice_and_delish.registry.world.worldgen.ModFeatures;
import com.panzer.mods.dice_and_delish.registry.world.worldgen.ModPlacedFeatures;
import com.panzer.mods.dice_and_delish.util.ModLogger;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(DiceAndDelish.MOD_ID)
public final class DiceAndDelish {

    public static final String MOD_ID = "dice_and_delish";
    public static final Logger LOGGER = LoggerFactory.getLogger("DiceAndDelish");

    public DiceAndDelish(IEventBus modEventBus) {
        ModLogger.info("Initializing Dice & Delish...");

        // Data & Utility Registries
        ModDataComponents.register(modEventBus);
        ModDamageTypes.register(modEventBus);
        ModCapabilities.register(modEventBus);
        ModCustomStats.register(modEventBus);

        // Core Game Registries
        ModSounds.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModItemTags.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        // Recipe Registries
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        ModIngredientTypes.register(modEventBus);

        // WorldGen Datagen Helpers
        ModFeatures.register(modEventBus);
        ModPlacedFeatures.register(modEventBus);

        // Datagen setup
        DataGenerators.register(modEventBus);

        ModLogger.info("Dice & Delish initialization complete!");
    }
}
