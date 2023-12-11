package io.github.ultreon.mods.essentials.util;

import net.minecraft.world.phys.Vec3;

public interface PositionHolder {
    double getX();

    double getY();

    double getZ();

    Vec3 getPosition();
}
