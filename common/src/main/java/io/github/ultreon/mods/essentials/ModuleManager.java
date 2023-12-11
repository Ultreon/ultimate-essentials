package io.github.ultreon.mods.essentials;

import java.util.ArrayList;
import java.util.List;

class ModuleManager {
    private static final List<UEssentialsModule> modules = new ArrayList<>();

    void register(UEssentialsModule module) {
        modules.add(module);
    }
}
