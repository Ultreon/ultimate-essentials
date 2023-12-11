package io.github.ultreon.mods.essentials.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface LevelKeyHolder {
    ResourceKey<Level> getLevelKey();
}
