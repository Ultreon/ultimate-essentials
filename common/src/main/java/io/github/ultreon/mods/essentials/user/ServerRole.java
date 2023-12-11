package io.github.ultreon.mods.essentials.user;

import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.permission.SetRolePermissionPacket;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ServerRole extends Role {
    @NotNull
    private final Set<Permission> permissions;

    public ServerRole(@NotNull String name, @NotNull Permission... permissions) {
        super(name);
        this.permissions = new HashSet<>(Arrays.asList(permissions));
    }

    public ServerRole(Properties properties) {
        super(properties.name);
        this.permissions = properties.permissions;
    }

    @NotNull
    public Set<Permission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return permissions.stream().anyMatch(permission -> permission.isChildOf(Permissions.MASTER) || permission.equals(Permissions.MASTER)) ||
                permissions.stream().anyMatch(permission -> permission.isChildOf(perm) || permission.equals(perm));
    }

    @Override
    public boolean hasExactPermission(Permission permission) {
        return permissions.contains(permission);
    }

    @Override
    public void addPermission(Permission permission) {
        permissions.add(permission);
        Networking.get().sendToAllClients(new SetRolePermissionPacket(this, permission, true));
    }

    @Override
    public void removePermission(Permission permission) {
        permissions.remove(permission);
        Networking.get().sendToAllClients(new SetRolePermissionPacket(this, permission, false));
    }

    public static class Properties {
        private String name;
        private final Set<Permission> permissions = new HashSet<>();

        public Properties(String name) {
            this.name = name;
        }

        @Contract("_->this")
        public Properties permission(Permission permission) {
            permissions.add(permission);
            return this;
        }

        @Contract("_->this")
        public Properties name(String name) {
            this.name = name;
            return this;
        }
    }
}
