package com.panzer.mods.dice_and_delish.util;

import com.panzer.mods.dice_and_delish.DiceAndDelish;

@SuppressWarnings({"unused", "StringConcatenationArgumentToLogCall"})
public final class ModLogger {

    private static final String PREFIX = "[Panzer] [Dice&Delish]";
    private static final String SINGLE_FORMAT = "{} {}";
    private static final String ARGS_FORMAT = "{} ";

    private ModLogger() {
    }

    public static void info(String message) {
        DiceAndDelish.LOGGER.info(SINGLE_FORMAT, PREFIX, message);
    }

    public static void info(String message, Object... args) {
        DiceAndDelish.LOGGER.info(PREFIX + " " + message, args);
    }

    public static void debug(String message) {
        DiceAndDelish.LOGGER.debug(SINGLE_FORMAT, PREFIX, message);
    }

    public static void debug(String message, Object... args) {
        DiceAndDelish.LOGGER.debug(PREFIX + " " + message, args);
    }

    public static void warn(String message) {
        DiceAndDelish.LOGGER.warn(SINGLE_FORMAT, PREFIX, message);
    }

    public static void warn(String message, Object... args) {
        DiceAndDelish.LOGGER.warn(PREFIX + " " + message, args);
    }

    public static void error(String message) {
        DiceAndDelish.LOGGER.error(SINGLE_FORMAT, PREFIX, message);
    }

    public static void error(String message, Object... args) {
        DiceAndDelish.LOGGER.error(PREFIX + " " + message, args);
    }

    public static void error(String message, Throwable throwable) {
        DiceAndDelish.LOGGER.error(SINGLE_FORMAT, PREFIX, message, throwable);
    }
}
