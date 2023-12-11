package io.github.ultreon.mods.essentials.network;

import io.github.ultreon.mods.essentials.Constants;
import io.github.ultreon.mods.essentials.homes.Home;
import io.github.ultreon.mods.essentials.homes.HomeReference;
import io.github.ultreon.mods.essentials.warps.WarpReference;
import lombok.experimental.UtilityClass;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@SuppressWarnings("unused")
@UtilityClass
public final class NetworkUtils {
    public static void writeZonedDateTime(FriendlyByteBuf buffer, ZonedDateTime zonedDateTime) {
        buffer.writeLong(zonedDateTime.toLocalDateTime().toEpochSecond(zonedDateTime.getOffset()));
        buffer.writeInt(zonedDateTime.getOffset().getTotalSeconds());
        buffer.writeUtf(zonedDateTime.getZone().getId());
    }

    public static ZonedDateTime readZonedDateTime(FriendlyByteBuf buffer) {
        long epochSecond = buffer.readLong();
        int zoneOffsetSeconds = buffer.readInt();
        String zoneId = buffer.readUtf();
        return ZonedDateTime.of(LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.ofTotalSeconds(zoneOffsetSeconds)), ZoneId.of(zoneId));
    }

    public static void writeLocalDateTime(FriendlyByteBuf buffer, LocalDateTime localDateTime, ZoneOffset offset) {
        buffer.writeLong(localDateTime.toEpochSecond(offset));
    }

    public static LocalDateTime readLocalDateTime(FriendlyByteBuf buffer, ZoneOffset offset) {
        long epochSecond = buffer.readLong();
        return LocalDateTime.ofEpochSecond(epochSecond, 0, offset);
    }

    public static void writeHomeReference(FriendlyByteBuf buffer, HomeReference ref) {
        buffer.writeUtf(ref.homeName(), Constants.MAX_HOME_NAME_LEN);
    }

    public static HomeReference readHomeReference(FriendlyByteBuf buffer) {
        String homeName = buffer.readUtf(Constants.MAX_HOME_NAME_LEN);
        return () -> homeName;
    }

    public static void writeWarpReference(FriendlyByteBuf buffer, WarpReference ref) {
        buffer.writeUtf(ref.title(), Constants.MAX_WARP_TITLE_LEN);
        buffer.writeUtf(ref.description(), Constants.MAX_WARP_DESCRIPTION_LEN);
    }

    public static WarpReference readWarpReference(FriendlyByteBuf buffer) {
        String title = buffer.readUtf(Constants.MAX_WARP_TITLE_LEN);
        String description = buffer.readUtf(Constants.MAX_WARP_DESCRIPTION_LEN);
        return new WarpReference() {
            @Override
            public String title() {
                return title;
            }

            @Override
            public String description() {
                return description;
            }
        };
    }

    public static void writeHome(FriendlyByteBuf buffer, Home home) {
        home.write(buffer);
    }

    public static Home readHome(FriendlyByteBuf buffer) {
        return Home.read(buffer);
    }

    public static void writeVec3(FriendlyByteBuf buffer, Vec3 vec3) {
        buffer.writeDouble(vec3.x);
        buffer.writeDouble(vec3.y);
        buffer.writeDouble(vec3.z);
    }

    public static Vec3 readVec3(FriendlyByteBuf buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        return new Vec3(x, y, z);
    }

    public static void writeVector3d(FriendlyByteBuf buffer, Vector3d vector3d) {
        buffer.writeDouble(vector3d.x);
        buffer.writeDouble(vector3d.y);
        buffer.writeDouble(vector3d.z);
    }

    public static Vector3d readVector3d(FriendlyByteBuf buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        return new Vector3d(x, y, z);
    }

    public static void writeVector3f(FriendlyByteBuf buffer, Vector3f vec3f) {
        buffer.writeFloat(vec3f.x());
        buffer.writeFloat(vec3f.y());
        buffer.writeFloat(vec3f.z());
    }

    public static Vector3f readVector3f(FriendlyByteBuf buffer) {
        float x = buffer.readFloat();
        float y = buffer.readFloat();
        float z = buffer.readFloat();
        return new Vector3f(x, y, z);
    }

    public static void writeVec3i(FriendlyByteBuf buffer, Vec3i vec3i) {
        buffer.writeInt(vec3i.getX());
        buffer.writeInt(vec3i.getY());
        buffer.writeInt(vec3i.getZ());
    }

    public static Vec3i readVec3i(FriendlyByteBuf buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        return new Vec3i(x, y, z);
    }

    public static void writeVec2(FriendlyByteBuf buffer, Vec2 vec2) {
        buffer.writeFloat(vec2.x);
        buffer.writeFloat(vec2.y);
    }

    public static Vec2 readVec2(FriendlyByteBuf buffer) {
        float x = buffer.readFloat();
        float y = buffer.readFloat();
        return new Vec2(x, y);
    }

    public static void writeVector4f(FriendlyByteBuf buffer, Vector4f vector4f) {
        buffer.writeFloat(vector4f.x());
        buffer.writeFloat(vector4f.y());
        buffer.writeFloat(vector4f.z());
        buffer.writeFloat(vector4f.w());
    }

    public static Vector4f readVector4f(FriendlyByteBuf buffer) {
        float x = buffer.readFloat();
        float y = buffer.readFloat();
        float z = buffer.readFloat();
        float w = buffer.readFloat();
        return new Vector4f(x, y, z, w);
    }
    public static void writeQuaternionf(FriendlyByteBuf buffer, Quaternionf vector4f) {
        buffer.writeFloat(vector4f.x());
        buffer.writeFloat(vector4f.y());
        buffer.writeFloat(vector4f.z());
        buffer.writeFloat(vector4f.w());
    }

    public static Quaternionf readQuaternionf(FriendlyByteBuf buffer) {
        float x = buffer.readFloat();
        float y = buffer.readFloat();
        float z = buffer.readFloat();
        float w = buffer.readFloat();
        return new Quaternionf(x, y, z, w);
    }
}
