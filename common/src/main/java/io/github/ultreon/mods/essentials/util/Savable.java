package io.github.ultreon.mods.essentials.util;

import net.minecraft.nbt.CompoundTag;

public interface Savable {
    CompoundTag save(CompoundTag tag);
}
