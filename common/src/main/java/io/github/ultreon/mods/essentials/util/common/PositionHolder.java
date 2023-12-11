package io.github.ultreon.mods.essentials.util.common;

import net.minecraft.world.phys.Vec3;

@Deprecated
public interface PositionHolder {
    double getX();

    double getY();

    double getZ();

    Vec3 getPosition();
}
