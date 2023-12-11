package io.github.ultreon.mods.essentials.fabric;

import io.github.ultreon.mods.essentials.UEssentials;
import net.fabricmc.api.ModInitializer;

public class UEssentialsFabric implements ModInitializer {
    private UEssentials instance;

    @Override
    public void onInitialize() {
        this.instance = new UEssentials();
    }

    public UEssentials getActualInstance() {
        return instance;
    }
}