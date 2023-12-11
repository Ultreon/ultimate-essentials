package io.github.ultreon.mods.essentials.util;

import io.github.ultreon.mods.essentials.UEssentials;

import java.time.LocalDate;

@SuppressWarnings({"SameParameterValue", "unused"})
public class GeneralConstants {
    public static final String NETWORK_VERSION = func(2021, 7, 19, 17, 55);

    static {
        UEssentials.LOGGER.info("Network version: " + NETWORK_VERSION);
    }

    private static String func(int year, int mon, int day, int hour, int min) {
        LocalDate date = LocalDate.of(year, mon, day);
        return (date.getYear() - 2000) + "." + (date.getDayOfYear() - 1) + "." + (hour * 60 + min);
    }

    private static String func(int year, int mon, int day, int hour, int min, int var) {
        LocalDate date = LocalDate.of(year, mon, day);
        return (date.getYear() - 2000) + "." + (date.getDayOfYear() - 1) + "." + (hour * 60 + min) + "-" + var;
    }
}
