package io.github.ultreon.mods.essentials.user;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServerPermissions {
    private static final Map<String, Permission> permissions = new HashMap<>();

    public static Collection<Permission> getPermissions() {
        return Collections.unmodifiableCollection(permissions.values());
    }

    public static void create(Permission perm) {
        permissions.put(perm.id(), perm);
    }

    @Nullable
    public static Permission getPermission(String name) {
        return permissions.get(name);
    }

    static {
        Permissions.getPerms().forEach(ServerPermissions::create);
    }
}
