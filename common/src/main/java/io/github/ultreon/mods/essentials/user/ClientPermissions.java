package io.github.ultreon.mods.essentials.user;

import dev.architectury.event.events.client.ClientPlayerEvent;
import net.minecraft.client.player.LocalPlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class ClientPermissions {
    private static final Set<Permission> permissions = new HashSet<>();

    public static void registerPermission(Permission permission) {
        permissions.add(permission);
    }

    public static Set<Permission> getAllPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    public static void init() {
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(ClientPermissions::reset);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(ClientPermissions::reset);
    }

    private static void reset(LocalPlayer localPlayer) {
        permissions.clear();
    }
}
