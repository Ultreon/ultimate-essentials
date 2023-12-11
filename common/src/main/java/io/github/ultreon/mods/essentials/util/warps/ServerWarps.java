package io.github.ultreon.mods.essentials.util.warps;

import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.warps.ListWarpsPacket;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.warps.WarpReference;
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

public class ServerWarps {
    @NotNull
    private final Set<Warp> warps;

    private ServerWarps(@NotNull Set<Warp> warps) {
        this.warps = warps;
    }

    public ServerWarps() {
        this.warps = new HashSet<>();
    }

    @NotNull
    public static ServerWarps from(ListTag list) {
        Set<Warp> warps = new HashSet<>();
        for (Tag tag : list) {
            if (tag instanceof CompoundTag) {
                Warp warp = io.github.ultreon.mods.essentials.util.warps.Warp.load((CompoundTag) tag);
                warps.add(warp);
            }
        }

        return new ServerWarps(warps);
    }

    @NotNull
    public Set<Warp> getAll() {
        return Collections.unmodifiableSet(warps);
    }

    public void setWarp(@NotNull Warp warp) {
        warps.remove(warp);
        warps.add(warp);
    }

    @Nullable
    public Warp add(@NotNull Warp warp) {
        warps.add(warp);
        return warp;
    }

    public ListTag to(ListTag list) {
        for (@NotNull Warp warp : warps) {
            list.add(warp.save(new CompoundTag()));
        }

        return list;
    }

    public void delete(@NotNull Warp warp) {
        warps.remove(warp);
    }

    private boolean teleport(@NotNull ServerPlayer player, @Nullable Warp warp) {
        if (warp == null) {
            return false;
        }

        ResourceKey<Level> levelKey = warp.getLevelKey();
        ServerLevel level = UEssentials.server().getLevel(levelKey);
        if (level != null) {
            double x = warp.getX();
            double y = warp.getY();
            double z = warp.getZ();

            float yRot = warp.getYRot();
            float xRot = warp.getXRot();

            player.teleportTo(level, x, y, z, xRot, yRot);
            return true;
        }
        return false;
    }

    public boolean teleport(@NotNull ServerPlayer player, @NotNull WarpReference ref) {
        Warp warp = get(ref.title());
        if (warp == null) {
            return false;
        }
        return teleport(player, warp);
    }

    public boolean teleport(@NotNull ServerPlayer player, @NotNull String name) {
        Warp warp = get(name);
        if (warp == null) {
            return false;
        }
        return teleport(player, warp);
    }

    @Nullable
    public Warp get(@NotNull WarpReference ref) {
        return get(ref.title());
    }

    @Nullable
    public Warp get(@NotNull String name) {
        return _get(name).orElse(null);
    }

    public boolean has(@NotNull WarpReference ref) {
        return get(ref.title()) != null;
    }

    public boolean has(@NotNull String name) {
        return get(name) != null;
    }

    public boolean has(@NotNull Warp warp) {
        return warps.contains(warp);
    }

    public void delete(@NotNull WarpReference ref) {
        delete(ref.title());
    }

    public void delete(@NotNull String name) {
        _get(name).ifPresent(this::delete);
    }

    public void rename(@NotNull String from, @NotNull String to) {
        _get(from).ifPresent(warp -> warp.setName(to));
    }

    public void reposition(@NotNull String name, @NotNull Vec3 to) {
        _get(name).ifPresent(warp -> warp.setPosition(to));
    }

    public void rotate(@NotNull String name, @NotNull Vec2 to) {
        _get(name).ifPresent(warp -> warp.setRotation(to));
    }

    public void rename(@NotNull WarpReference ref, String to) {
        _get(ref.title()).ifPresent(warp -> warp.setName(to));
    }

    public void reposition(@NotNull WarpReference ref, @NotNull Vec3 to) {
        _get(ref.title()).ifPresent(warp -> warp.setPosition(to));
    }

    public void rotate(@NotNull WarpReference ref, @NotNull Vec2 to) {
        _get(ref.title()).ifPresent(warp -> warp.setRotation(to));
    }

    @NotNull
    private Optional<Warp> _get(@NotNull String name) {
        return warps.stream().filter(warp -> warp.getName().equals(name)).findFirst();
    }

    @Nullable
    public Warp add(@NotNull String name, @NotNull Vec3 pos, @NotNull Vec2 rot, @NotNull ServerLevel level) {
        return add(new Warp(name, pos, rot, level.dimension()));
    }

    public void showList(@NotNull ServerUser user) {
        ServerPlayer player = user.player();

        Set<Warp> allWarps = getAll();

        Networking.get().sendToClient(new ListWarpsPacket.Begin(), player);
        for (@NotNull Warp warp : allWarps) {
            UEssentials.LOGGER.debug(warp);
            Networking.get().sendToClient(new ListWarpsPacket.Entry(warp.asReference()), player);
        }
        Networking.get().sendToClient(new ListWarpsPacket.End(), player);
    }
}
