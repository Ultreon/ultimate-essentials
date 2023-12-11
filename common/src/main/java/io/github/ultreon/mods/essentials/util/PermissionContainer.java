package io.github.ultreon.mods.essentials.util;

import io.github.ultreon.mods.essentials.user.Permission;

public interface PermissionContainer {
    boolean hasPermission(Permission perm);

    boolean hasExactPermission(Permission permission);

    void addPermission(Permission perm);

    void removePermission(Permission perm);
}
