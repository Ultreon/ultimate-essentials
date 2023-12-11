package io.github.ultreon.mods.essentials.util;

import net.minecraft.network.chat.Component;

public interface Kickable {
    void kick(String reason);

    void kick(Component reason);
}
