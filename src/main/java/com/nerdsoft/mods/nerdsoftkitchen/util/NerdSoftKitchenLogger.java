package com.nerdsoft.mods.nerdsoftkitchen.util;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;

@SuppressWarnings({"unused", "StringConcatenationArgumentToLogCall"})
public final class NerdSoftKitchenLogger {

    private static final String PREFIX = "[NS] [Kitchen]";
    private static final String SINGLE_FORMAT = "{} {}";
    private static final String ARGS_FORMAT = "{} ";

    private NerdSoftKitchenLogger() {
    }

    public static void info(String message) {
        NerdSoftKitchen.LOGGER.info(SINGLE_FORMAT, PREFIX, message);
    }

    public static void info(String message, Object... args) {
        NerdSoftKitchen.LOGGER.info(PREFIX + " " + message, args);
    }

    public static void warn(String message) {
        NerdSoftKitchen.LOGGER.warn(SINGLE_FORMAT, PREFIX, message);
    }

    public static void warn(String message, Object... args) {
        NerdSoftKitchen.LOGGER.warn(PREFIX + " " + message, args);
    }

    public static void error(String message) {
        NerdSoftKitchen.LOGGER.error(SINGLE_FORMAT, PREFIX, message);
    }

    public static void error(String message, Object... args) {
        NerdSoftKitchen.LOGGER.error(PREFIX + " " + message, args);
    }

    public static void error(String message, Throwable throwable) {
        NerdSoftKitchen.LOGGER.error(SINGLE_FORMAT, PREFIX, message, throwable);
    }
}