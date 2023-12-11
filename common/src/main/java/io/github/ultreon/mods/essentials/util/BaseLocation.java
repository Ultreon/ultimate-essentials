package io.github.ultreon.mods.essentials.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface BaseLocation extends PositionHolder, RotationHolder {
    ResourceKey<Level> getLevelKey();
}
