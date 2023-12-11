package io.github.ultreon.mods.essentials.teleport;

import io.github.ultreon.mods.essentials.util.BaseLocation;
import io.github.ultreon.mods.essentials.util.UniqueObject;
import lombok.Getter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

@SuppressWarnings("unused")
public class PositionTeleportImpl implements PositionTeleport {
    private final double destX;
    private final double destY;
    private final double destZ;
    @Getter
    private final float destYaw;
    @Getter
    private final float destPitch;
    private final ResourceKey<Level> destLevel;
    private final UUID origin;

    public PositionTeleportImpl(UUID origin, double destX, double destY, double destZ, float destYaw, float destPitch, Level destLevel) {
        this(origin, destX, destY, destZ, destYaw, destPitch, destLevel.dimension());
    }

    public PositionTeleportImpl(UUID origin, Vec3 vec3d, Vec2 vec2f, Level destLevel) {
        this(origin, vec3d.x, vec3d.y, vec3d.z, vec2f.x, vec2f.y, destLevel.dimension());
    }

    public PositionTeleportImpl(UuidOrigin origin, double destX, double destY, double destZ, float destYaw, float destPitch, Level destLevel) {
        this(origin.origin(), destX, destY, destZ, destYaw, destPitch, destLevel.dimension());
    }

    public PositionTeleportImpl(UniqueObject origin, double destX, double destY, double destZ, float destYaw, float destPitch, Level destLevel) {
        this(origin.uuid(), destX, destY, destZ, destYaw, destPitch, destLevel.dimension());
    }

    public PositionTeleportImpl(UUID origin, Position4DDestination dest, float destYaw, float destPitch) {
        this(origin, dest.getDestX(), dest.getDestY(), dest.getDestZ(), destYaw, destPitch, dest.getDestLevel());
    }

    public PositionTeleportImpl(UuidOrigin origin, Position4DDestination dest, float destYaw, float destPitch) {
        this(origin.origin(), dest.getDestX(), dest.getDestY(), dest.getDestZ(), destYaw, destPitch, dest.getDestLevel());
    }

    public PositionTeleportImpl(UniqueObject origin, Position4DDestination dest, float destYaw, float destPitch) {
        this(origin.uuid(), dest.getDestX(), dest.getDestY(), dest.getDestZ(), destYaw, destPitch, dest.getDestLevel());
    }

    public PositionTeleportImpl(UUID origin, BaseLocation dest) {
        this(origin, dest.getX(), dest.getY(), dest.getZ(), dest.getXRot(), dest.getYRot(), dest.getLevelKey());
    }

    public PositionTeleportImpl(UuidOrigin origin, BaseLocation dest) {
        this(origin.origin(), dest.getX(), dest.getY(), dest.getZ(), dest.getXRot(), dest.getYRot(), dest.getLevelKey());
    }

    public PositionTeleportImpl(UniqueObject origin, BaseLocation dest) {
        this(origin.uuid(), dest.getX(), dest.getY(), dest.getZ(), dest.getXRot(), dest.getYRot(), dest.getLevelKey());
    }

    public PositionTeleportImpl(UuidOrigin origin, double destX, double destY, double destZ, float destYaw, float destPitch, ResourceKey<Level> destLevel) {
        this(origin.origin(), destX, destY, destZ, destYaw, destPitch, destLevel);
    }

    public PositionTeleportImpl(UniqueObject origin, double destX, double destY, double destZ, float destYaw, float destPitch, ResourceKey<Level> destLevel) {
        this(origin.uuid(), destX, destY, destZ, destYaw, destPitch, destLevel);
    }

    public PositionTeleportImpl(UUID origin, double destX, double destY, double destZ, float destYaw, float destPitch, ResourceKey<Level> destLevel) {
        this.destX = destX;
        this.destY = destY;
        this.destZ = destZ;
        this.destYaw = destYaw;
        this.destPitch = destPitch;
        this.destLevel = destLevel;
        this.origin = origin;
    }

    @Override
    public double getDestX() {
        return destX;
    }

    @Override
    public double getDestY() {
        return destY;
    }

    @Override
    public double getDestZ() {
        return destZ;
    }

    @Override
    public ResourceKey<Level> getDestLevel() {
        return destLevel;
    }

    @Override
    public UUID origin() {
        return origin;
    }
}
