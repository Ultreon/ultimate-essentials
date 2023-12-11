package io.github.ultreon.mods.essentials.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("unused")
@AllArgsConstructor
public class Location implements PositionHolder, RotationHolder, LevelKeyHolder {
    @Setter
    @Getter
    public double x;
    @Setter
    @Getter
    public double y;
    @Setter
    @Getter
    public double z;
    public float xRot;
    public float yRot;
    @Setter
    public ResourceKey<Level> level;

    public float getXRot() {
        return xRot;
    }

    public void setXRot(float xRot) {
        this.xRot = xRot;
    }

    public float getYRot() {
        return yRot;
    }

    public void setYRot(float yRot) {
        this.yRot = yRot;
    }

    public void setLevel(Level level) {
        this.level = level.dimension();
    }

    @Override
    public Vec3 getPosition() {
        return new Vec3(x, y, z);
    }

    @Override
    public Vec2 getRotation() {
        return new Vec2(xRot, yRot);
    }

    @Override
    public ResourceKey<Level> getLevelKey() {
        return level;
    }
}
