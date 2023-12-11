package io.github.ultreon.mods.essentials.util.teleport;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

@Deprecated
public interface Position4DDestination extends Position3DDestination {
    ResourceKey<Level> getDestLevel();
}
