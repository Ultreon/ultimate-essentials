package io.github.ultreon.mods.essentials.user;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record LocalAccountInfo(@NotNull String username, boolean isAdmin) {
    public LocalAccountInfo(@NotNull String username) {
        this(username, false);
    }

    public boolean isRoot() {
        return Objects.equals(username, "root");
    }

    public boolean isUndefined() {
        return Objects.equals(username, "<<undefined>>");
    }

    public boolean isValid() {
        return !isUndefined();
    }
}
