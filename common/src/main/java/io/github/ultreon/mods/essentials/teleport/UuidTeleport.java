package io.github.ultreon.mods.essentials.teleport;

import java.util.UUID;

public interface UuidTeleport extends UuidOrigin, UuidDestination {
    UUID origin();

    UUID destination();
}
