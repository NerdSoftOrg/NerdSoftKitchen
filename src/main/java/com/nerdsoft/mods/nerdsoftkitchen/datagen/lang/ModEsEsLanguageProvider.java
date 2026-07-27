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
        add(ModBlocks.GRILL_TABLE.get(), "Parrilla");
        add(ModBlocks.GRILL_TABLE_SOUL.get(), "Parrilla de almas");
        add(ModBlocks.WILD_STRAWBERRY.get(), "Fresa Silvestre");
        add(ModBlocks.WILD_TOMATO.get(), "Tomate Silvestre");
        add(ModBlocks.WILD_LETTUCE.get(), "Lechuga Silvestre");
        add(ModBlocks.WILD_PURPLE_ONION.get(), "Cebolla Silvestre");
        add(ModBlocks.TOMATO_CROP_POLE.get(), "Tomatera Entutorada");

        add(ModItems.STRAWBERRY_SEEDS.get(), "Semillas de Fresa");
        add(ModItems.TOMATO_SEEDS.get(), "Semillas de Tomate");
        add(ModItems.LETTUCE_SEEDS.get(), "Semillas de Lechuga");
        add(ModItems.PURPLE_ONION_SEEDS.get(), "Semillas de Cebolla");

        add(ModItems.STRAWBERRY.get(), "Fresa");
        add(ModItems.TOMATO.get(), "Tomate");
        add(ModItems.LETTUCE.get(), "Lechuga");
        add(ModItems.PURPLE_ONION.get(), "Cebolla");
        add(ModItems.IRON_CUP.get(), "Taza de Hierro");
        add(ModItems.RAW_CHICKEN_PIECES.get(), "Piezas de Pollo Crudas");
        add(ModItems.COOKED_CHICKEN_PIECES.get(), "Piezas de Pollo Cocinadas");
        add(ModItems.FRIED_EGG.get(), "Huevo Frito");
        add(ModItems.SALAD.get(), "Ensalada");

        addFilledNamePrefix("Taza de Hierro de ");
        addContainsPrefix("Contiene: ");
        addCupContent("milk", "Leche");
        addCupContent("yogurt", "Yogur");
        addCupContent("strawberry_yogurt", "Yogur de Fresa");

        add("itemGroup.nerdsoftkitchen.kitchen_tab", "NerdSoft Kitchen");
        add("subtitles.block.nerdsoftkitchen.grill.sizzle", "Chisporroteo de aceite");

        add("jade.nerdsoftkitchen.grill_table.slot_remaining", "%ss");
        add("config.jade.plugin_nerdsoftkitchen.grill_table_progress", "Tiempo de Cocción");

        addAdvancement("root", "NerdSoft Kitchen", "Craftea una Parrilla");
        addAdvancement("iron_cup", "Cubito de Hierro", "Craftea una Taza de Hierro");
        addAdvancement("milk_cup", "¿Hay Leche?", "Llena una Taza de Hierro con leche");
        addAdvancement("strawberry_yogurt", "Dulce Capricho", "Prepara Yogur de Fresa");
        addAdvancement("harvest_all", "Huerta Variada", "Cosecha cada cultivo silvestre al menos una vez");

        add("datapack.nerdsoftkitchen.description", "Recursos de NerdSoft Kitchen");

        add("jei.category.nerdsoftkitchen.grill_cooking", "Cocción en Parrilla");
    }
}