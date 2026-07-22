package com.nerdsoft.mods.nerdsoftkitchen.datagen;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public abstract class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, NerdSoftKitchen.MOD_ID, locale);
    }

    protected void addFilledNamePrefix(String prefix) {
        add("nerdsoftkitchen.iron_cup.filled_name", prefix + "%s");
    }

    protected void addContainsPrefix(String prefix) {
        add("nerdsoftkitchen.iron_cup_content.contains", prefix + "%s");
    }

    protected void addCupContent(String key, String content) {
        add("nerdsoftkitchen.iron_cup_content." + key, content);
    }

    protected void addAdvancement(String id, String title, String description) {
        add("advancements.nerdsoftkitchen." + id + ".title", title);
        add("advancements.nerdsoftkitchen." + id + ".description", description);
    }
}