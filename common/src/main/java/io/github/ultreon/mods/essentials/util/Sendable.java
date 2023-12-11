package io.github.ultreon.mods.essentials.util;

import net.minecraft.network.FriendlyByteBuf;

public interface Sendable {
    void write(FriendlyByteBuf buffer);
}
