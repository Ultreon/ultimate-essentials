package io.github.ultreon.mods.essentials.util.teleport;

import java.util.UUID;

@Deprecated
public interface UUIDTeleport extends UUIDOrigin, UUIDDestination {
    UUID origin();

    UUID destination();
}
