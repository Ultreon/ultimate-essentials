package io.github.ultreon.mods.essentials.user;

public class UserSettings {
    private static boolean darkMode = true;

    public static boolean hasDarkMode() {
        return darkMode;
    }

    public static void setDarkMode(boolean darkMode) {
        UserSettings.darkMode = darkMode;
    }
}
