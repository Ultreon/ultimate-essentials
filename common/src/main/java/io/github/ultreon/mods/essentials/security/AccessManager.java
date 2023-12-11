package io.github.ultreon.mods.essentials.security;

import io.github.ultreon.mods.essentials.UEssentials;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AccessManager {
    private static final AccessManager instance = new AccessManager();
    private final Set<UUID> access = new HashSet<>();

    private AccessManager() {

    }

    public static AccessManager get() {
        return instance;
    }

    public boolean hasAccess(UUID playerUUID) {
        return access.contains(playerUUID) || UEssentials.isServerGod(playerUUID);
    }

    public boolean hasAccess(ServerPlayer player) {
        return access.contains(player.getUUID());
    }

    public boolean addAccess(UUID playerUUID) {
        return access.add(playerUUID);
    }

    public boolean addAccess(ServerPlayer player) {
        return access.add(player.getUUID());
    }

    public boolean removeAccess(UUID playerUUID) {
        return access.remove(playerUUID);
    }

    public boolean removeAccess(ServerPlayer player) {
        return access.remove(player.getUUID());
    }

    public ListTag save() {
        ListTag list = new ListTag();
        for (UUID uuid : access) {
            list.add(NbtUtils.createUUID(uuid));
        }
        return list;
    }

    public void load(ListTag list) {
        access.clear();
        for (Tag tag : list) {
            try {
                access.add(NbtUtils.loadUUID(tag));
            } catch (Exception ignored) {

            }
        }
    }
}
