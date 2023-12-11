package io.github.ultreon.mods.essentials.user;

import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.permission.SetRolePermissionPacket;

import java.util.Set;

public class ClientRole extends Role {
    private Set<Permission> permissions;

    public ClientRole(String name) {
        super(name);
    }

    @Override
    public Set<Permission> getPermissions() {
        return permissions;
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
        Networking.get().sendToServer(new SetRolePermissionPacket(this, permission, true));
    }

    @Override
    public void removePermission(Permission permission) {
        Networking.get().sendToServer(new SetRolePermissionPacket(this, permission, false));
    }

    public void handlePacket(SetRolePermissionPacket packet) {
        boolean enable = packet.isEnable();
        Permission permission = new Permission(packet.getPermission());

        if (enable) permissions.add(permission);
        else permissions.remove(permission);
    }
}
