package com.nerdsoft.mods.nerdsoftkitchen;

import com.nerdsoft.mods.nerdsoftkitchen.registry.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(NerdSoftKitchen.MOD_ID)
public final class NerdSoftKitchen {

    public static final String MOD_ID = "nerdsoftkitchen";
    public static final Logger LOGGER = LoggerFactory.getLogger("NerdSoftKitchen");

    public NerdSoftKitchen(IEventBus modEventBus) {
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENUS.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);
        ModRecipeTypes.RECIPE_TYPES.register(modEventBus);
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        ModIngredientTypes.INGREDIENT_TYPES.register(modEventBus);
        modEventBus.addListener(com.nerdsoft.mods.nerdsoftkitchen.datagen.DataGenerators::gatherData);
    }
}