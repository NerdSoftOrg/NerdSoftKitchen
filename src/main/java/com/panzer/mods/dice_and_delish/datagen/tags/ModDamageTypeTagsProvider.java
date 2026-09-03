package com.panzer.mods.dice_and_delish.datagen.tags;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.registry.data.ModDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagsProvider extends TagsProvider<DamageType> {

    public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, DiceAndDelish.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        ResourceLocation cookwareBurn = ModDamageTypes.COOKWARE_BURN.location();

        this.tag(DamageTypeTags.IS_FIRE).addOptional(cookwareBurn);
        this.tag(DamageTypeTags.IGNITES_ARMOR_STANDS).addOptional(cookwareBurn);
        this.tag(DamageTypeTags.NO_KNOCKBACK).addOptional(cookwareBurn);
        this.tag(DamageTypeTags.BURN_FROM_STEPPING).addOptional(cookwareBurn);
    }
}
