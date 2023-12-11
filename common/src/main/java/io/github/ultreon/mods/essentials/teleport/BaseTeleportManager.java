package io.github.ultreon.mods.essentials.teleport;

import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public abstract class BaseTeleportManager {
    protected abstract void requestTeleportTo(UUID uuid);

    protected abstract void requestTeleportFrom(UUID uuid);

    protected abstract void requestTeleportEntity(UUID uuid);

    public abstract Player getPlayer();

    public abstract void accept(UUID requester);

    public abstract void deny(UUID requester);

    public abstract void back();
}
