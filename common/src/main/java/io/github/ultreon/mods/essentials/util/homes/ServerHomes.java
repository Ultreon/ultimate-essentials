package io.github.ultreon.mods.essentials.util.homes;

import io.github.ultreon.mods.essentials.event.HomeEvent;
import io.github.ultreon.mods.essentials.homes.HomeReference;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.homes.ListHomesPacket;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.teleport.ServerTeleportManager;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.util.LocationImpl;
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
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ServerHomes {
    private final ServerUser owner;
    private final Set<ServerHome> homes;

    ServerHomes(ServerUser owner, Set<ServerHome> homes) {
        this.owner = owner;
        this.homes = homes;
    }

    public ServerHomes(ServerUser owner) {
        this.owner = owner;
        this.homes = new HashSet<>();
    }

    public static ServerHomes from(ServerUser user, ListTag list) {
        Set<ServerHome> homes = new HashSet<>();
        for (Tag tag : list) {
            if (tag instanceof CompoundTag) {
                ServerHome home = ServerHome.load((CompoundTag) tag);
                homes.add(home);
            }
        }

        return new ServerHomes(user, homes);
    }

    public Set<ServerHome> getAll() {
        return Collections.unmodifiableSet(homes);
    }

    public void setHome(ServerHome home) {
        homes.remove(home);
        homes.add(home);
        HomeEvent.SET.invoker().onSet(home, owner);
    }

    @Nullable
    public ServerHome add(ServerHome home) {
        homes.add(home);
        HomeEvent.CREATED.invoker().onCreated(home, owner);
        return home;
    }

    public ListTag to(ListTag list) {
        for (ServerHome home : homes) {
            list.add(home.save(new CompoundTag()));
        }

        return list;
    }

    public void delete(ServerHome home) {
        homes.remove(home);
        HomeEvent.REMOVED.invoker().onRemoved(home, owner);
    }

    private boolean teleport(ServerPlayer player, ServerHome home) {
        if (home == null) {
            return false;
        }

        ServerTeleportManager.get(player).setBackLocation(new LocationImpl(player.level(), player.position(), player.getRotationVector()));

        ResourceKey<Level> levelKey = home.getLevelKey();
        ServerLevel level = UEssentials.server().getLevel(levelKey);
        if (level != null) {
            double x = home.getX();
            double y = home.getY();
            double z = home.getZ();

            float yRot = home.getYRot();
            float xRot = home.getXRot();

            player.teleportTo(level, x, y, z, yRot, xRot);
            return true;
        }
        return false;
    }

    public boolean teleport(ServerPlayer player, HomeReference ref) {
        ServerHome home = get(ref.homeName());
        if (home == null) {
            return false;
        }
        return teleport(player, home);
    }

    public boolean teleport(ServerPlayer player, String name) {
        ServerHome home = get(name);
        if (home == null) {
            return false;
        }
        return teleport(player, home);
    }

    @Nullable
    public ServerHome get(HomeReference ref) {
        return get(ref.homeName());
    }

    @Nullable
    public ServerHome get(String name) {
        return get0(name).orElse(null);
    }

    public boolean has(HomeReference ref) {
        return get(ref.homeName()) != null;
    }

    public boolean has(String name) {
        return get(name) != null;
    }

    public boolean has(ServerHome home) {
        return homes.contains(home);
    }

    public void delete(HomeReference ref) {
        delete(ref.homeName());
    }

    public void delete(String name) {
        get0(name).ifPresent(this::delete);
    }

    public void rename(String old, String new_) {
        get0(old).ifPresent(home -> home.setName(new_));
    }

    public void reposition(String name, Vec3 pos) {
        get0(name).ifPresent(home -> home.setPosition(pos));
    }

    public void rotate(String name, Vec2 rot) {
        get0(name).ifPresent(home -> home.setRotation(rot));
    }

    public void rename(HomeReference ref, String newName) {
        get0(ref.homeName()).ifPresent(home -> home.setName(newName));
    }

    public void reposition(HomeReference ref, Vec3 pos) {
        get0(ref.homeName()).ifPresent(home -> home.setPosition(pos));
    }

    public void rotate(HomeReference ref, Vec2 rot) {
        get0(ref.homeName()).ifPresent(home -> home.setRotation(rot));
    }

    @NotNull
    private Optional<ServerHome> get0(String name) {
        return homes.stream().filter(home -> home.getName().equals(name)).findFirst();
    }

    @Nullable
    public ServerHome add(String name) {
        Vec3 pos = owner.getPosition();
        Vec2 rot = owner.getRotation();
        ServerLevel level = owner.getLevel();
        return add(new ServerHome(name, pos, rot, level.dimension()));
    }



    public void showList(ServerUser user) {
        if (user.uuid() != this.owner.uuid()) {
            showScreen0(user);
            return;
        }

        ServerPlayer player = user.player();

        ListHomesPacket.Type type = ListHomesPacket.Type.PERSONAL;
        Set<ServerHome> allHomes = getAll();

        Networking.get().sendToClient(new ListHomesPacket.Begin(type), player);
        for (ServerHome home : allHomes) {
            UEssentials.LOGGER.debug(home);
            Networking.get().sendToClient(new ListHomesPacket.Entry(home), player);
        }
        Networking.get().sendToClient(new ListHomesPacket.End(type), player);
    }

    private void showScreen0(ServerUser user) {
        // Pass
    }

    public void showList(ServerPlayer player) {
        showList(ServerUser.get(player));
    }
}
