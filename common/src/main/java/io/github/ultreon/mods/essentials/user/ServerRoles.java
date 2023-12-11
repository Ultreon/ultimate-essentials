package io.github.ultreon.mods.essentials.user;

import io.github.ultreon.mods.essentials.network.permission.SetRolePermissionPacket;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ServerRoles {
    private static final Map<String, ServerRole> roles = new HashMap<>();

    public static Collection<ServerRole> getRoles() {
        return Collections.unmodifiableCollection(roles.values());
    }

    public static void createRole(ServerRole.Properties properties) {
        ServerRole serverRole = new ServerRole(properties);
        roles.put(serverRole.getName(), serverRole);
    }

    public static void deleteRole(String name) {
        roles.remove(name);
    }

    public static void handlePacket(SetRolePermissionPacket setRolePermissionPacket) {
        ServerRole serverRole = roles.get(setRolePermissionPacket.getRoleName());
        serverRole.addPermission(ServerPermissions.getPermission(setRolePermissionPacket.getPermission()));
    }
}
