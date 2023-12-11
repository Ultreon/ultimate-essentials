package io.github.ultreon.mods.essentials.util;

public class TextUtils {
    public static String trimFormatting(String string) {
        return string.replaceAll("§[A-Fa-f0-9]", "");
    }
}
