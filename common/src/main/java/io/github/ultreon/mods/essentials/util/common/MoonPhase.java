package io.github.ultreon.mods.essentials.util.common;

import lombok.Getter;

@Getter
public enum MoonPhase {
    NEW_MOON("New Moon \uD83C\uDF11"),
    WAXING_CRESCENT("Waxing Crescent \uD83C\uDF12"),
    FIRST_QUARTER("First Quarter \uD83C\uDF13"),
    WAXING_GIBBOUS("Waxing Gibbous \uD83C\uDF14"),
    FULL_MOON("Full Moon \uD83C\uDF15"),
    WANING_GIBBOUS("Waning Gibbous \uD83C\uDF16"),
    THIRD_QUARTER("Third Quarter \uD83C\uDF17"),
    WANING_CRESCENT("Waning Crescent \uD83C\uDF18"),
    ;

    private final String name;

    MoonPhase(String name) {
        this.name = name;
    }

}
