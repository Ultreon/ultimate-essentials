package io.github.ultreon.mods.essentials;

public enum TriState {
    TRUE, FALSE, DEFAULT;

    public boolean value() {
        return switch (this) {
            case TRUE -> true;
            case FALSE -> false;
            case DEFAULT -> throw new IllegalStateException("Tri state value is default.");
        };
    }
}
