package com.nerdsoft.mods.nerdsoftkitchen.datagen.lang;

import com.nerdsoft.mods.nerdsoftkitchen.registry.block.ModBlocks;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import net.minecraft.data.PackOutput;

public class ModEsEsLanguageProvider extends ModLanguageProvider {

    public ModEsEsLanguageProvider(PackOutput output) {
        super(output, "es_es");
    }

    @Override
    protected void addTranslations() {
        // Bloques
        add(ModBlocks.GRILL_TABLE, "Parrilla");
        add(ModBlocks.GRILL_TABLE_SOUL, "Parrilla de Almas");
        add(ModBlocks.TOMATO_CROP_POLE, "Tomatera Entutorada");
        add(ModBlocks.FERTILE_FARMLAND, "Tierra Fértil");
        add(ModBlocks.ORGANIC_SOIL, "Tierra Orgánica");
        add(ModBlocks.CUTTING_BOARD, "Tabla de Cortar");

        // Cultivos y Semillas
        addWildCropPrefix("%s Silvestre");
        addCropSeedsPrefix("Semillas de %s");

        addCrop(
                ModBlocks.WILD_STRAWBERRY,
                ModItems.STRAWBERRY_SEEDS,
                ModItems.STRAWBERRY,
                "Fresa"
        );
        addCrop(
                ModBlocks.WILD_TOMATO,
                ModItems.TOMATO_SEEDS,
                ModItems.TOMATO,
                "Tomate"
        );
        addCrop(
                ModBlocks.WILD_LETTUCE,
                ModItems.LETTUCE_SEEDS,
                ModItems.LETTUCE,
                "Lechuga"
        );
        addCrop(
                ModBlocks.WILD_PURPLE_ONION,
                ModItems.PURPLE_ONION_SEEDS,
                ModItems.PURPLE_ONION,
                "Cebolla"
        );

        // Cuchillos
        addKnifePattern("Cuchillo de %s");

        addKnife(ModItems.STONE_KNIFE, "Piedra");
        addKnife(ModItems.IRON_KNIFE, "Hierro");
        addKnife(ModItems.GOLD_KNIFE, "Oro");
        addKnife(ModItems.DIAMOND_KNIFE, "Diamante");
        addKnife(ModItems.OBSIDIAN_KNIFE, "Obsidiana");
        addKnife(ModItems.NETHERITE_KNIFE, "Netherita");

        // Items y Comidas
        add(ModItems.IRON_CUP, "Taza de Hierro");
        add(ModItems.RAW_CHICKEN_PIECES, "Trozos de Pollo Crudo");
        add(ModItems.COOKED_CHICKEN_PIECES, "Trozos de Pollo Cocinado");
        add(ModItems.FRIED_EGG, "Huevo Frito");
        add(ModItems.RAW_SANDWICH_BREAD, "Pan de Sándwich");
        add(ModItems.TOASTED_SANDWICH_BREAD, "Pan de Sándwich Tostado");
        add(ModItems.SALAD, "Ensalada");
        add(ModItems.CHEESE, "Queso");
        add(ModItems.CHEESE_SLICE, "Loncha de Queso");
        add(ModItems.CHEESE_RAW_SANDWICH, "Sándwich de Queso");
        add(ModItems.CHEESE_TOASTED_SANDWICH, "Sándwich de Queso Tostado");
        add(ModItems.GRILLED_CHEESE, "Queso a la Parrilla");
        add(ModItems.ORGANIC_MIXTURE, "Mezcla Orgánica");

        // Iron Cups
        addFilledNamePrefix("Taza de Hierro de ");
        addContainsPrefix("Contiene: ");
        addCupContent("milk", "Leche");
        addCupContent("yogurt", "Yogur");
        addCupContent("strawberry_yogurt", "Yogur de Fresa");
        add("nerdsoftkitchen.iron_cup.tooltip.empty", "Haz clic derecho en una vaca para llenarla de leche");

        // Tooltips
        add("itemGroup.nerdsoftkitchen.kitchen_tab", "NerdSoft Kitchen");
        add("subtitles.block.nerdsoftkitchen.grill.place_food", "Asando comida");
        add("jade.nerdsoftkitchen.grill_table.slot_remaining", "%ss");
        add("config.jade.plugin_nerdsoftkitchen.grill_table_progress", "Tiempo de Cocción");

        // Logros
        addAdvancement(
                "root",
                "NerdSoft Kitchen",
                "Fabrica una Parrilla"
        );
        addAdvancement(
                "iron_cup",
                "Mini-Cubo",
                "Fabrica una Taza de Hierro"
        );
        addAdvancement(
                "cutting_board",
                "Estación del Chef",
                "Fabrica una Tabla de Cortar"
        );
        addAdvancement(
                "grill_soul",
                "Barbacoa Espectral",
                "Fabrica una Parrilla de Almas"
        );
        addAdvancement(
                "master_knife",
                "Maestro Cuchillero",
                "Fabrica un Cuchillo de Diamante, Obsidiana o Netherita"
        );
        addAdvancement(
                "milk_cup",
                "¿Hay Leche?",
                "Llena una Taza de Hierro con leche"
        );
        addAdvancement(
                "yogurt",
                "Fermentado",
                "Prepara Yogur natural"
        );
        addAdvancement(
                "strawberry_yogurt",
                "Dulce Capricho",
                "Prepara Yogur de Fresa"
        );
        addAdvancement(
                "grow_strawberry",
                "Fresal",
                "Cosecha una Fresa"
        );
        addAdvancement(
                "grow_lettuce",
                "Verdes Frescos",
                "Cosecha Lechuga"
        );
        addAdvancement(
                "grow_purple_onion",
                "Lágrimas de Cocina",
                "Cosecha una Cebolla Morada"
        );
        addAdvancement(
                "grow_tomato",
                "Madurado en la Rama",
                "Cosecha un Tomate"
        );
        addAdvancement(
                "trellis_master",
                "Hacia lo Alto",
                "Haz crecer una tomatera en su estaca"
        );
        addAdvancement(
                "harvest_all",
                "Huerta Variada",
                "Cosecha cada cultivo al menos una vez"
        );
        addAdvancement(
                "make_salad",
                "Fresca y Crujiente",
                "Prepara una Ensalada"
        );
        addAdvancement(
                "cook_chicken_pieces",
                "Troceado y a la Parrilla",
                "Cocina Trozos de Pollo en la parrilla"
        );
        addAdvancement(
                "fry_egg",
                "Sunny Side Up",
                "Fríe un Huevo en la parrilla"
        );
        addAdvancement(
                "grilled_cheese",
                "Fundido y Delicioso",
                "Prepara un sándwich de Queso Fundido"
        );
        addAdvancement(
                "gourmet",
                "Chef Gourmet",
                "Domina cada disciplina de la cocina: cultivo, corte, parrilla y lácteos"
        );

        // JEI Info
        add("jei.category.nerdsoftkitchen.grill_cooking", "Cocción en Parrilla");
        addJeiInfo(
                "grill_table",
                "Cocina comida directamente en la rejilla de la parrilla, o usa las ranuras inferiores como una fogata normal. ¡Coloca una Bala de Heno debajo para cocinar un 25% más rápido!"
        );
        addJeiInfo(
                "grill_table_soul",
                "Fabricada con una Fogata de Almas. Funciona igual que la parrilla normal, ¡pero cocina un 10% más rápido!"
        );
        addJeiInfo(
                "iron_cup",
                "Haz clic derecho en una vaca para llenarla de leche. Combina una taza llena con azúcar en la mesa de crafteo para obtener yogur."
        );
        addJeiInfo(
                "iron_cup_milk",
                "No se fabrica: usa clic derecho con una Taza de Hierro vacía sobre una vaca (que no sea Champiñaca) para llenarla de leche."
        );
        addJeiInfo(
                "iron_cup_yogurt",
                "Combina una Taza de Hierro con leche y Azúcar para obtener Yogur natural."
        );
        addJeiInfo(
                "iron_cup_strawberry_yogurt",
                "Combina una Taza de Hierro con leche, Azúcar y una Fresa, o una Taza de Yogur natural con una Fresa."
        );
        addJeiInfo(
                "cutting_board",
                "Haz clic derecho con un ingrediente cortable para colocarlo, luego haz clic derecho con cualquier cuchillo (etiqueta #c:tools/knife) para cortarlo. Cortar reduce la durabilidad del cuchillo en 1."
        );

        // Datapack
        add("datapack.nerdsoftkitchen.description", "Recursos de NerdSoft Kitchen");
    }
}
