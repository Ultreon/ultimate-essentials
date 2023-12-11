package io.github.ultreon.mods.essentials.easy;

import io.github.ultreon.mods.essentials.UEssentials;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.io.File;
import java.util.UUID;

@SuppressWarnings("ClassCanBeRecord")
public class Server {
    private final MinecraftServer wrapper;

    public Server(MinecraftServer wrapper) {
        this.wrapper = wrapper;
    }

    public File getCoreDataFolder() {
        return UEssentials.getDataFolder();
    }

    public File getDataFile(String path) {
        return new File(UEssentials.getDataFolder(), path);
    }

    public ServerPlayer getPlayer(UUID uuid) {
        return wrapper.getPlayerList().getPlayer(uuid);
    }

    public ServerPlayer getPlayer(String username) {
        return wrapper.getPlayerList().getPlayerByName(username);
    }
}
