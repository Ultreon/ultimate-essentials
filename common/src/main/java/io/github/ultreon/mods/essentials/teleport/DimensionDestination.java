package io.github.ultreon.mods.essentials.teleport;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface DimensionDestination {
    ResourceKey<Level> getDestLevel();
}
