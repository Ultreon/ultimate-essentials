package io.github.ultreon.mods.essentials.util;

import net.minecraft.network.FriendlyByteBuf;

public interface Receivable {
    void read(FriendlyByteBuf buffer);
}
