package com.panzer.mods.dice_and_delish.datagen.lang;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

@SuppressWarnings("SameParameterValue")
public abstract class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, DiceAndDelish.MOD_ID, locale);
    }

    private String wildCropFormat = "%s";
    private String cropSeedsFormat = "%s";
    private String knifePattern = "%s";

    protected void addWildCropPrefix(String format) { this.wildCropFormat = format; }
    protected void addCropSeedsPrefix(String format) { this.cropSeedsFormat = format; }
    protected void addKnifePattern(String format) { this.knifePattern = format; }

    public void add(DeferredItem<?> key, String name) {
        this.add(key.get().getDescriptionId(), name);
    }

    public void add(DeferredBlock<?> key, String name) {
        this.add(key.get().getDescriptionId(), name);
    }

    protected void addCrop(DeferredBlock<?> wildBlock, DeferredItem<?> seeds, DeferredItem<?> cropItem, String cropName) {
        if (cropItem != null) add(cropItem, cropName);
        if (wildBlock != null) add(wildBlock, String.format(this.wildCropFormat, cropName));
        if (seeds != null) add(seeds, String.format(this.cropSeedsFormat, cropName));
    }

    protected void addKnife(DeferredItem<?> knifeItem, String materialName) {
        add(knifeItem, String.format(this.knifePattern, materialName));
    }

    protected void addCupContent(String key, String content) {
        add("dice_and_delish.iron_cup_content." + key, content);
    }

    protected void addCupTooltip(String key, String text) {
        add("dice_and_delish.iron_cup.tooltip." + key, text);
    }

    protected void addSubtitle(DeferredHolder<SoundEvent, SoundEvent> sound, String text) {
        add("subtitles." + sound.getId().getNamespace() + "." + sound.getId().getPath(), text);
    }

    protected void addAdvancement(String id, String title, String description) {
        add("advancements.dice_and_delish." + id + ".title", title);
        add("advancements.dice_and_delish." + id + ".description", description);
    }

    protected void addJeiInfo(String key, String description) {
        add("dice_and_delish.jei.info." + key, description);
    }

    protected void addJeiCategory(String key, String title) {
        add("jei.category.dice_and_delish." + key, title);
    }

    protected void addJadeConfig(String featureKey, String localizedName) {
        add("config.jade.plugin_" + DiceAndDelish.MOD_ID + "." + featureKey, localizedName);
    }

    protected void addJadeTooltip(String key, String localizedName) {
        add("jade." + DiceAndDelish.MOD_ID + "." + key, localizedName);
    }

    protected void addFilledNamePrefix(String prefix) {
        add("dice_and_delish.iron_cup.filled_name", prefix + "%s");
    }

    protected void addContainsPrefix(String prefix) {
        add("dice_and_delish.iron_cup_content.contains", prefix + "%s");
    }

    protected void addDeathMessage(String damageTypeId, String message) {
        add("death.attack." + DiceAndDelish.MOD_ID + "." + damageTypeId, message);
    }

    protected void addDeathMessagePlayer(String damageTypeId, String message) {
        add("death.attack." + DiceAndDelish.MOD_ID + "." + damageTypeId + ".player", message);
    }
}
