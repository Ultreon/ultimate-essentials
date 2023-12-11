package io.github.ultreon.mods.essentials.util.common;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

@Deprecated
public interface BaseLocation extends PositionHolder, RotationHolder {
    ResourceKey<Level> getLevelKey();
}
