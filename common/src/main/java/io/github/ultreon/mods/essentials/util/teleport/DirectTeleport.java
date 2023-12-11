package io.github.ultreon.mods.essentials.util.teleport;

import java.util.UUID;

@Deprecated
public record DirectTeleport(UUID origin, UUID destination) implements UUIDTeleport {
}
