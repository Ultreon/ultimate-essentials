package io.github.ultreon.mods.essentials.util.warps;

import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.teleport.ServerTeleportManager;
import io.github.ultreon.mods.essentials.util.BaseLocation;
import io.github.ultreon.mods.essentials.util.LocationImpl;
import io.github.ultreon.mods.essentials.util.common.Saveable;
import io.github.ultreon.mods.essentials.warps.WarpReference;
import lombok.NonNull;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Warp implements Saveable, BaseLocation {
    @NotNull
    private String name;
    @NotNull
    private String description;
    private Vec3 position;
    private Vec2 rotation;
    private ResourceKey<Level> level;

    public Warp(@NotNull String name, double x, double y, double z, float xRot, float yRot, @NotNull ResourceKey<Level> level) {
        this(name, new Vec3(x, y, z), new Vec2(xRot, yRot), level);
    }

    public Warp(@NotNull String name, @NotNull String description, double x, double y, double z, float xRot, float yRot, @NotNull ResourceKey<Level> level) {
        this(name, description, new Vec3(x, y, z), new Vec2(xRot, yRot), level);
    }

    public Warp(@NotNull String name, @NotNull Vec3 position, @NotNull Vec2 rotation, @NotNull ResourceKey<Level> level) {
        this(name, "", position, rotation, level);
    }

    public Warp(@NotNull String name, @NotNull String description, @NotNull Vec3 position, @NotNull Vec2 rotation, @NotNull ResourceKey<Level> level) {
        this.name = name;
        this.description = description;
        this.position = position;
        this.rotation = rotation;
        this.level = level;
    }

    @NotNull
    public static Warp load(@NotNull CompoundTag tag) {
        ResourceLocation levelLoc = new ResourceLocation(tag.getString("world"));
        ResourceKey<Level> level = ResourceKey.create(Registries.DIMENSION, levelLoc);
        String name = tag.getString("name");
        double x = tag.getDouble("x");
        double y = tag.getDouble("y");
        double z = tag.getDouble("z");
        float yRot = tag.getFloat("yaw");
        float xRot = tag.getFloat("pitch");
        String description = tag.getString("description");

        return new Warp(name, description, x, y, z, xRot, yRot, level);
    }

    @NotNull
    public CompoundTag save(@NotNull CompoundTag tag) {
        tag.putString("world", level.location().toString());
        tag.putString("name", name);
        tag.putDouble("x", position.x);
        tag.putDouble("y", position.y);
        tag.putDouble("z", position.z);
        tag.putFloat("yaw", rotation.y);
        tag.putFloat("pitch", rotation.x);
        tag.putString("description", description);

        return tag;
    }

    public boolean teleport(@NotNull ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server != null) {
            ServerLevel level1 = server.getLevel(level);
            if (level1 != null) {
                ServerTeleportManager.get(player).setBackLocation(new LocationImpl(player.level(), player.position(), player.getRotationVector()));
                player.teleportTo(level1, position.x, position.y, position.z, rotation.y, rotation.x);
            }
            return true;
        }
        return false;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Override
    public double getX() {
        return position.x;
    }

    @Override
    public double getY() {
        return position.y;
    }

    @Override
    public double getZ() {
        return position.z;
    }

    @NotNull
    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(@NotNull Vec3 position) {
        this.position = position;
    }

    @Override
    public float getXRot() {
        return rotation.x;
    }

    @Override
    public float getYRot() {
        return rotation.y;
    }

    @NotNull
    public Vec2 getRotation() {
        return rotation;
    }

    public void setRotation(@NotNull Vec2 rotation) {
        this.rotation = rotation;
    }

    @NotNull
    public ResourceKey<Level> getLevelKey() {
        return level;
    }

    @Nullable
    public ServerLevel getLevel() {
        return UEssentials.server().getLevel(level);
    }

    public void setLevel(@NotNull ResourceKey<Level> level) {
        this.level = level;
    }

    public void setLevel(@NotNull ServerLevel level) {
        this.level = level.dimension();
    }

    @NotNull
    @Override
    @Contract(pure = true)
    public String toString() {
        return "Warp{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", rotation=" + rotation +
                ", level=" + level +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warp warp = (Warp) o;
        return name.equals(warp.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    public WarpReference asReference() {
        return new WarpReference() {
            @Override
            public String title() {
                return name;
            }

            @Override
            public String description() {
                return getDescription();
            }
        };
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NotNull String description) {
        this.description = description;
    }
}
