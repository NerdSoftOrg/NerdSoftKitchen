package com.panzer.mods.dice_and_delish.compat.jei.client;

//? if <1.21.2 {
import com.panzer.mods.dice_and_delish.util.ModLogger;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class JeiCategorySorter {

    private static final String FILE_NAME = "config/jei/recipe-category-sort-order.ini";
    private static final String VANILLA_CAMPFIRE = "minecraft:campfire_cooking";

    private static final List<String> MOD_CATEGORIES = List.of(
            "dice_and_delish:grill_cooking",
            "dice_and_delish:cutting_board",
            "dice_and_delish:skillet_cooking"
    );

    private JeiCategorySorter() {
    }

    public static void forceCategoriesOrder() {
        try {
            File gameDir = Minecraft.getInstance().gameDirectory;
            File jeiConfigFile = new File(gameDir, FILE_NAME);

            if (!jeiConfigFile.exists()) {
                ModLogger.warn("JEI config file '{}' does not exist yet.", FILE_NAME);
                return;
            }

            List<String> currentLines = Files.readAllLines(jeiConfigFile.toPath(), StandardCharsets.UTF_8);
            List<String> newLines = buildSortedLines(currentLines);

            if (!currentLines.equals(newLines)) {
                Files.write(jeiConfigFile.toPath(), newLines, StandardCharsets.UTF_8);
                ModLogger.info("Successfully reordered JEI categories after '{}' in {}.", VANILLA_CAMPFIRE, FILE_NAME);
            }

        } catch (Exception e) {
            ModLogger.error("Failed to reorder JEI recipe categories in " + FILE_NAME, e);
        }
    }

    private static List<String> buildSortedLines(List<String> lines) {
        List<String> filteredLines = new ArrayList<>(lines);
        filteredLines.removeIf(line -> MOD_CATEGORIES.contains(line.trim()));

        List<String> newLines = new ArrayList<>();
        boolean inserted = false;

        for (String line : filteredLines) {
            newLines.add(line);
            if (line.trim().equals(VANILLA_CAMPFIRE)) {
                newLines.addAll(MOD_CATEGORIES);
                inserted = true;
            }
        }

        if (!inserted) {
            newLines.addAll(MOD_CATEGORIES);
        }

        return newLines;
    }
}
//?}
