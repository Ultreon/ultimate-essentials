package io.github.ultreon.mods.essentials.user;

import io.github.ultreon.mods.essentials.network.permission.CreateRolePacket;
import io.github.ultreon.mods.essentials.network.permission.DeleteRolePacket;
import io.github.ultreon.mods.essentials.network.permission.SetRolePermissionPacket;

import java.util.HashMap;
import java.util.Map;

public class ClientRoles {
    private static final Map<String, ClientRole> roles = new HashMap<>();

    public static ClientRole getRole(String name) {
        return roles.get(name);
    }

    public static void handlePacket(SetRolePermissionPacket packet) {
        ClientRole role = getRole(packet.getRoleName());
        role.handlePacket(packet);
    }

    public static void handlePacket(CreateRolePacket packet) {
        String roleName = packet.getRoleName();
        roles.put(roleName, new ClientRole(roleName));
    }

    public static void handlePacket(DeleteRolePacket deleteRolePacket) {
        roles.remove(deleteRolePacket.getRole());
    }
}
