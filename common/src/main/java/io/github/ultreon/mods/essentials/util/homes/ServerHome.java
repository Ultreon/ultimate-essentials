package io.github.ultreon.mods.essentials.util.homes;

import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.event.HomeEvent;
import io.github.ultreon.mods.essentials.homes.Home;
import io.github.ultreon.mods.essentials.homes.HomeReference;
import io.github.ultreon.mods.essentials.util.BaseLocation;
import io.github.ultreon.mods.essentials.util.common.Saveable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
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
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ServerHome extends Home implements Saveable, BaseLocation {
    @NotNull
    private String name;
    @Getter
    @Setter
    private Vec3 position;
    @Getter
    @Setter
    private Vec2 rotation;
    @Setter
    private ResourceKey<Level> level;

    public ServerHome(@NotNull String name, Vec3 position, Vec2 rotation, ResourceKey<Level> level) {
        super(name, position, rotation, level);
        this.name = name;
        this.position = position;
        this.rotation = rotation;
        this.level = level;
    }

    public ServerHome(@NotNull String name, double x, double y, double z, float xRot, float yRot, ResourceKey<Level> level) {
        this(name, new Vec3(x, y, z), new Vec2(xRot, yRot), level);
    }

    public static ServerHome load(CompoundTag tag) {
        ResourceLocation levelLoc = new ResourceLocation(tag.getString("world"));
        ResourceKey<Level> level = ResourceKey.create(Registries.DIMENSION, levelLoc);
        String name = tag.getString("name");
        double x = tag.getDouble("x");
        double y = tag.getDouble("y");
        double z = tag.getDouble("z");
        float yRot = tag.getFloat("yaw");
        float xRot = tag.getFloat("pitch");

        return new ServerHome(name, x, y, z, xRot, yRot, level);
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putString("world", level.location().toString());
        tag.putString("name", name);
        tag.putDouble("x", position.x);
        tag.putDouble("y", position.y);
        tag.putDouble("z", position.z);
        tag.putFloat("yaw", rotation.y);
        tag.putFloat("pitch", rotation.x);

        return tag;
    }

    public boolean teleport(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server != null) {
            ServerLevel level1 = server.getLevel(level);
            if (level1 != null) {
                if (HomeEvent.TELEPORTING.invoker().onTeleporting(player, this).isFalse()) {
                    return false;
                }
                player.teleportTo(level1, position.x, position.y, position.z, rotation.y, rotation.x);
                HomeEvent.TELEPORTED.invoker().onTeleported(player, this);
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

    @Override
    public float getXRot() {
        return rotation.x;
    }

    @Override
    public float getYRot() {
        return rotation.y;
    }

    public ResourceKey<Level> getLevelKey() {
        return level;
    }

    public ServerLevel getLevel() {
        return UEssentials.server().getLevel(level);
    }

    public void setLevel(ServerLevel level) {
        this.level = level.dimension();
    }

    @Override
    public String toString() {
        return "Home{" +
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
        ServerHome home = (ServerHome) o;
        return name.equals(home.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public HomeReference asReference() {
        return () -> name;
    }

    @NotNull
    public String getDescription() {
        return "";
    }
}
