package io.github.ultreon.mods.essentials.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class LocationImpl implements BaseLocation {
    private final ResourceKey<Level> levelKey;
    private final double x;
    private final double y;
    private final double z;
    private final float xRot;
    private final float yRot;

    public LocationImpl(Level level, Vec3 pos, Vec2 rot) {
        this(level, pos, rot.x, rot.y);
    }

    public LocationImpl(Level level, Vec3 pos, float xRot, float yRot) {
        this(level, pos.x, pos.y, pos.z, xRot, yRot);
    }

    public LocationImpl(Level level, double x, double y, double z, Vec2 rot) {
        this(level, x, y, z, rot.x, rot.y);
    }

    public LocationImpl(Level level, double x, double y, double z, float xRot, float yRot) {
        this.levelKey = level.dimension();
        this.x = x;
        this.y = y;
        this.z = z;
        this.xRot = xRot;
        this.yRot = yRot;
    }

    public LocationImpl(ResourceKey<Level> levelKey, Vec3 pos, Vec2 rot) {
        this(levelKey, pos, rot.x, rot.y);
    }

    public LocationImpl(ResourceKey<Level> levelKey, Vec3 pos, float xRot, float yRot) {
        this(levelKey, pos.x, pos.y, pos.z, xRot, yRot);
    }

    public LocationImpl(ResourceKey<Level> levelKey, double x, double y, double z, Vec2 rot) {
        this(levelKey, x, y, z, rot.x, rot.y);
    }

    public LocationImpl(ResourceKey<Level> levelKey, double x, double y, double z, float xRot, float yRot) {
        this.levelKey = levelKey;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xRot = xRot;
        this.yRot = yRot;
    }

    @Override
    public ResourceKey<Level> getLevelKey() {
        return levelKey;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public Vec3 getPosition() {
        return new Vec3(x, y, z);
    }

    @Override
    public float getXRot() {
        return xRot;
    }

    @Override
    public float getYRot() {
        return yRot;
    }

    @Override
    public Vec2 getRotation() {
        return new Vec2(xRot, yRot);
    }
}
