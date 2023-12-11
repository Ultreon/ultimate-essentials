package io.github.ultreon.mods.essentials.teleport;

import java.util.UUID;

public record DirectTeleport(UUID origin, UUID destination) implements UuidTeleport {
    
}
