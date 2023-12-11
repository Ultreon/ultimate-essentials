package io.github.ultreon.mods.essentials.user;

import io.github.ultreon.mods.essentials.util.Named;
import io.github.ultreon.mods.essentials.util.PermissionContainer;
import lombok.Getter;

import java.util.Set;

@Getter
public abstract class Role implements PermissionContainer, Named {
    private final String name;

    public Role(String name) {
        this.name = name;
    }

    public abstract Set<Permission> getPermissions();

    @Override
    public abstract boolean hasPermission(Permission permission);

    @Override
    public abstract void addPermission(Permission permission);

    @Override
    public abstract void removePermission(Permission permission);
}
