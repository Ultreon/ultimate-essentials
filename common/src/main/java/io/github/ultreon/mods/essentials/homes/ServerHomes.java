package io.github.ultreon.mods.essentials.homes;

import com.google.common.annotations.Beta;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.homes.ListHomesPacket;
import io.github.ultreon.mods.essentials.UEssentials;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Deprecated
public class ServerHomes {
    private final UUID owner;
    private final Set<Home> homes;

    ServerHomes(UUID owner, Set<Home> homes) {
        this.owner = owner;
        this.homes = homes;
    }

    public ServerHomes(UUID owner) {
        this.owner = owner;
        this.homes = new HashSet<>();
    }

    public static ServerHomes from(UUID player, ListTag list) {
        Set<Home> homes = new HashSet<>();
        for (Tag tag : list) {
            if (tag instanceof CompoundTag) {
                Home home = Home.load((CompoundTag) tag);
                homes.add(home);
            }
        }

        return new ServerHomes(player, homes);
    }

    public Set<Home> getAll() {
        return Collections.unmodifiableSet(homes);
    }

    public void setHome(Home home) {
        homes.remove(home);
        homes.add(home);
    }

    public void addHome(Home home) {
        homes.add(home);
    }

    public ListTag to(ListTag list) {
        for (Home home : homes) {
            list.add(home.save(new CompoundTag()));
        }

        return list;
    }

    public void delete(Home home) {
        homes.remove(home);
    }

    public void showList(ServerPlayer player) {
        if (player.getUUID() != this.owner) {
            showScreen0(player);
            return;
        }

        ListHomesPacket.Type type = ListHomesPacket.Type.PERSONAL;
        Set<Home> allHomes = getAll();

        Networking.get().sendToClient(new ListHomesPacket.Begin(type), player);
        for (Home home : allHomes) {
            UEssentials.LOGGER.debug(home);
            Networking.get().sendToClient(new ListHomesPacket.Entry(home), player);
        }
        Networking.get().sendToClient(new ListHomesPacket.End(type), player);
    }

    @Beta
    private void showScreen0(ServerPlayer player) {

    }

    private void teleport(ServerPlayer player, Home home) {
        ResourceKey<Level> levelKey = home.getLevelKey();
        ServerLevel level = UEssentials.server().getLevel(levelKey);
        if (level != null) {
            double x = home.getX();
            double y = home.getY();
            double z = home.getZ();

            float yRot = home.getYRot();
            float xRot = home.getXRot();

            player.teleportTo(level, x, y, z, xRot, yRot);
        }
    }

    public void teleport(ServerPlayer player, String name) {
        teleport(player, get(name));
    }

    public void teleport(ServerPlayer player, HomeReference ref) {
        teleport(player, get(ref.homeName()));
    }

    public boolean has(Home home) {
        return homes.contains(home);
    }

    public Home get(HomeReference ref) {
        return get(ref.homeName());
    }

    public Home get(String name) {
        return _get(name).orElse(null);
    }

    public boolean has(HomeReference ref) {
        return get(ref.homeName()) != null;
    }

    public boolean has(String name) {
        return get(name) != null;
    }

    public void delete(HomeReference ref) {
        delete(ref.homeName());
    }

    public void delete(String name) {
        _get(name).ifPresent(this::delete);
    }

    public void rename(String name, String newName) {
        _get(name).ifPresent(home -> home.setName(newName));
    }

    public void reposition(String name, Vec3 pos) {
        _get(name).ifPresent(home -> home.setPosition(pos));
    }

    public void setRotation(String name, Vec2 rot) {
        _get(name).ifPresent(home -> home.setRotation(rot));
    }

    public void rename(HomeReference ref, String newName) {
        _get(ref.homeName()).ifPresent(home -> home.setName(newName));
    }

    public void reposition(HomeReference ref, Vec3 pos) {
        _get(ref.homeName()).ifPresent(home -> home.setPosition(pos));
    }

    public void setRotation(HomeReference ref, Vec2 rot) {
        _get(ref.homeName()).ifPresent(home -> home.setRotation(rot));
    }

    @NotNull
    private Optional<Home> _get(String name) {
        return homes.stream().filter(home -> home.getName().equals(name)).findFirst();
    }
}
