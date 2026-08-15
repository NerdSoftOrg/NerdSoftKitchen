package com.nerdsoft.mods.nerdsoftkitchen.datagen.lang;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public abstract class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, NerdSoftKitchen.MOD_ID, locale);
    }

    private String wildCropFormat = "%s";
    private String cropSeedsFormat = "%s";
    private String knifePattern = "%s";

    protected void addWildCropPrefix(String format) {
        this.wildCropFormat = format;
    }

    protected void addCropSeedsPrefix(String format) {
        this.cropSeedsFormat = format;
    }

    protected void addKnifePattern(String format) {
        this.knifePattern = format;
    }

    public void add(DeferredItem<?> key, String name) {
        this.add(key.get().getDescriptionId(), name);
    }

    public void add(DeferredBlock<?> key, String name) {
        this.add(key.get().getDescriptionId(), name);
    }

    protected void addCrop(DeferredBlock<?> wildBlock, DeferredItem<?> seeds, DeferredItem<?> cropItem, String cropName) {
        if (cropItem != null) {
            add(cropItem, cropName);
        }
        if (wildBlock != null) {
            add(wildBlock, String.format(this.wildCropFormat, cropName));
        }
        if (seeds != null) {
            add(seeds, String.format(this.cropSeedsFormat, cropName));
        }
    }

    protected void addKnife(DeferredItem<?> knifeItem, String materialName) {
        add(knifeItem, String.format(this.knifePattern, materialName));
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

    protected void addJeiInfo(String key, String description) {
        add("nerdsoftkitchen.jei.info." + key, description);
    }
}