package com.panzer.mods.dice_and_delish.registry.data;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Custom stats for the mod's cooking stations.
 * <p>
 * SkilletBlock and GrillTableBlock used to both call {@code player.awardStat(Stats.INTERACT_WITH_CAMPFIRE)}
 * when food was placed to cook — the same vanilla stat/advancement, shared between two unrelated
 * blocks. That made the vanilla Campfire advancement (and anything else keyed off that stat) trigger
 * from either block interchangeably. Each block now awards its own custom stat instead.
 */
public final class ModCustomStats {

    private static final DeferredRegister<ResourceLocation> CUSTOM_STATS =
            DeferredRegister.create(Registries.CUSTOM_STAT, DiceAndDelish.MOD_ID);

    public static final DeferredHolder<ResourceLocation, ResourceLocation> INTERACT_WITH_SKILLET =
            CUSTOM_STATS.register("interact_with_skillet",
                    () -> ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, "interact_with_skillet"));

    public static final DeferredHolder<ResourceLocation, ResourceLocation> INTERACT_WITH_GRILL_TABLE =
            CUSTOM_STATS.register("interact_with_grill_table",
                    () -> ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, "interact_with_grill_table"));

    // Stat<ResourceLocation> instances themselves are not part of the registry — Stats.CUSTOM
    // (a StatType<ResourceLocation>) lazily creates and caches one Stat per ResourceLocation the
    // first time it's requested, exactly like Stats.INTERACT_WITH_CAMPFIRE does internally for
    // "minecraft:interact_with_campfire". These suppliers just centralize that lookup.
    public static Stat<ResourceLocation> interactWithSkilletStat() {
        return Stats.CUSTOM.get(INTERACT_WITH_SKILLET.get(), StatFormatter.DEFAULT);
    }

    public static Stat<ResourceLocation> interactWithGrillTableStat() {
        return Stats.CUSTOM.get(INTERACT_WITH_GRILL_TABLE.get(), StatFormatter.DEFAULT);
    }

    private ModCustomStats() {
    }

    public static void register(IEventBus eventBus) {
        CUSTOM_STATS.register(eventBus);
    }
}
