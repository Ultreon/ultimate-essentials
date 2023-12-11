package io.github.ultreon.mods.essentials.user;

import java.util.HashSet;
import java.util.Set;

class LocalUserData {
    final Set<Permission> permissions = new HashSet<>();
    final Set<ClientRole> roles = new HashSet<>();
    double balance = 0f;
    boolean moderator = false;
    boolean admin = false;
}
