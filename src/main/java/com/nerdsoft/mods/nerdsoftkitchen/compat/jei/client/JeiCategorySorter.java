package com.nerdsoft.mods.nerdsoftkitchen.compat.jei.client;

//? if <1.21.2 {
import com.nerdsoft.mods.nerdsoftkitchen.util.NerdSoftKitchenLogger;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class JeiCategorySorter {

    private static final String FILE_NAME = "config/jei/recipe-category-sort-order.ini";
    private static final String TARGET_CATEGORY = "nerdsoftkitchen:grill_cooking";
    private static final String VANILLA_CAMPFIRE = "minecraft:campfire_cooking";

    private JeiCategorySorter() {
    }

    public static void forceGrillAfterCampfire() {
        try {
            File gameDir = Minecraft.getInstance().gameDirectory;
            File jeiConfigFile = new File(gameDir, FILE_NAME);

            File parentDir = jeiConfigFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                boolean created = parentDir.mkdirs();
                if (!created && !parentDir.exists()) {
                    NerdSoftKitchenLogger.warn("Could not create JEI config directories: {}", parentDir.getAbsolutePath());
                    return;
                }
            }

            List<String> currentLines = new ArrayList<>();
            if (jeiConfigFile.exists()) {
                currentLines = Files.readAllLines(jeiConfigFile.toPath(), StandardCharsets.UTF_8);
            } else {
                currentLines.add(VANILLA_CAMPFIRE);
            }

            List<String> newLines = buildSortedLines(currentLines);

            Files.write(jeiConfigFile.toPath(), newLines, StandardCharsets.UTF_8);
            NerdSoftKitchenLogger.info("Successfully reordered JEI category: placed '{}' after '{}' in {}.", TARGET_CATEGORY, VANILLA_CAMPFIRE, FILE_NAME);

        } catch (Exception e) {
            NerdSoftKitchenLogger.error("Failed to reorder JEI recipe categories in " + FILE_NAME, e);
        }
    }

    private static List<String> buildSortedLines(List<String> lines) {
        List<String> filteredLines = new ArrayList<>(lines);
        filteredLines.removeIf(line -> line.trim().equals(TARGET_CATEGORY));

        List<String> newLines = new ArrayList<>();
        boolean inserted = false;

        for (String line : filteredLines) {
            newLines.add(line);
            if (line.trim().equals(VANILLA_CAMPFIRE)) {
                newLines.add(TARGET_CATEGORY);
                inserted = true;
            }
        }

        if (!inserted) {
            newLines.add(TARGET_CATEGORY);
        }

        return newLines;
    }
}
//?}