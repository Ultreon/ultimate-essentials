package io.github.ultreon.mods.essentials.util.common;

import net.minecraft.nbt.CompoundTag;

public interface Saveable {
    CompoundTag save(CompoundTag tag);
}
