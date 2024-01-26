package io.github.ultreon.mods.essentials.homes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ultreon.mods.essentials.Constants;
import io.github.ultreon.mods.essentials.util.BaseLocation;
import io.github.ultreon.mods.essentials.util.Savable;
import io.github.ultreon.mods.essentials.util.Sendable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
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

public class Home implements Sendable, Savable, BaseLocation {
    public static final Codec<Home> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("name").forGetter(Home::getName),
        Codec.DOUBLE.fieldOf("x").forGetter(Home::getX),
        Codec.DOUBLE.fieldOf("y").forGetter(Home::getY),
        Codec.DOUBLE.fieldOf("z").forGetter(Home::getZ),
        Codec.FLOAT.fieldOf("yaw").forGetter(Home::getXRot),
        Codec.FLOAT.fieldOf("pitch").forGetter(Home::getYRot),
        ResourceKey.codec(Registries.DIMENSION).fieldOf("world").forGetter(Home::getLevelKey)
    ).apply(instance, Home::new));

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

    public Home(@NotNull String name, Vec3 position, Vec2 rotation, ResourceKey<Level> level) {
        this.name = name;
        this.position = position;
        this.rotation = rotation;
        this.level = level;
    }

    public Home(@NotNull String name, double x, double y, double z, float xRot, float yRot, ResourceKey<Level> level) {
        this(name, new Vec3(x, y, z), new Vec2(xRot, yRot), level);
    }

    public Home(String name, Vec3 position, float xRot, float yRot, ResourceKey<Level> level) {
        this(name, position.x, position.y, position.z, xRot, yRot, level);
    }

    public static Home load(CompoundTag tag) {
        String name = tag.getString("name");
        double x = tag.getDouble("x");
        double y = tag.getDouble("y");
        double z = tag.getDouble("z");
        float xRot = tag.getFloat("yaw");
        float yRot = tag.getFloat("pitch");
        ResourceKey<Level> level = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("world")));
        return new Home(name, x, y, z, xRot, yRot, level);
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putString("world", level.location().toString());
        tag.putString("name", name);
        tag.putDouble("x", position.x);
        tag.putDouble("y", position.y);
        tag.putDouble("z", position.z);
        tag.putFloat("yaw", rotation.x);
        tag.putFloat("pitch", rotation.y);

        return tag;
    }

    public static Home read(FriendlyByteBuf packetBuffer) {
        String name = packetBuffer.readUtf(20);
        double x = packetBuffer.readDouble();
        double y = packetBuffer.readDouble();
        double z = packetBuffer.readDouble();
        float xRot = packetBuffer.readFloat();
        float yRot = packetBuffer.readFloat();
        ResourceKey<Level> level = ResourceKey.create(Registries.DIMENSION, packetBuffer.readResourceLocation());

        return new Home(name, x, y, z, xRot, yRot, level);
    }

    public void write(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeUtf(name.substring(0, Math.min(name.length(), 20)), 20);
        packetBuffer.writeDouble(position.x);
        packetBuffer.writeDouble(position.y);
        packetBuffer.writeDouble(position.z);
        packetBuffer.writeFloat(rotation.x);
        packetBuffer.writeFloat(rotation.y);
        packetBuffer.writeResourceLocation(level.location());
    }

    public boolean teleport(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server != null) {
            ServerLevel level1 = server.getLevel(level);
            if (level1 != null) {
                player.teleportTo(level1, position.x, position.y, position.z, rotation.x, rotation.y);
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
        name = name.trim();
        this.name = name.substring(0, Math.min(name.length(), Constants.MAX_HOME_NAME_LEN));
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
        Home home = (Home) o;
        return name.equals(home.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public HomeReference asReference() {
        return () -> name;
    }
}
