package com.nerdsoft.mods.nerdsoftkitchen;

import com.nerdsoft.mods.nerdsoftkitchen.datagen.DataGenerators;
import com.nerdsoft.mods.nerdsoftkitchen.registry.block.ModBlocks;
import com.nerdsoft.mods.nerdsoftkitchen.registry.blockentity.ModBlockEntities;
import com.nerdsoft.mods.nerdsoftkitchen.registry.data.ModCapabilities;
import com.nerdsoft.mods.nerdsoftkitchen.registry.data.ModDataComponents;
import com.nerdsoft.mods.nerdsoftkitchen.registry.data.ModDamageTypes;
import com.nerdsoft.mods.nerdsoftkitchen.registry.gui.ModCreativeTabs;
import com.nerdsoft.mods.nerdsoftkitchen.registry.gui.ModMenuTypes;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItemTags;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import com.nerdsoft.mods.nerdsoftkitchen.registry.recipe.ModIngredientTypes;
import com.nerdsoft.mods.nerdsoftkitchen.registry.recipe.ModRecipeSerializers;
import com.nerdsoft.mods.nerdsoftkitchen.registry.recipe.ModRecipeTypes;
import com.nerdsoft.mods.nerdsoftkitchen.registry.sound.ModSounds;
import com.nerdsoft.mods.nerdsoftkitchen.registry.worldgen.ModPlacedFeatures;
import com.nerdsoft.mods.nerdsoftkitchen.util.NerdSoftKitchenLogger;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(NerdSoftKitchen.MOD_ID)
public final class NerdSoftKitchen {

    public static final String MOD_ID = "nerdsoftkitchen";
    public static final Logger LOGGER = LoggerFactory.getLogger("NerdSoftKitchen");

    public NerdSoftKitchen(IEventBus modEventBus) {
        NerdSoftKitchenLogger.info("Initializing NerdSoft Kitchen...");

        // Data & Utility Registries
        ModDataComponents.register(modEventBus);
        ModDamageTypes.register(modEventBus);
        ModCapabilities.register(modEventBus);

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
        ModPlacedFeatures.register(modEventBus);

        // Datagen setup
        DataGenerators.register(modEventBus);

        NerdSoftKitchenLogger.info("NerdSoft Kitchen initialization complete!");
    }
}