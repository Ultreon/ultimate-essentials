package io.github.ultreon.mods.essentials.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.ultreon.mods.essentials.UEssentials;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(UEssentials.MOD_ID)
public class UEssentialsForge {
    private final UEssentials instance;

    public UEssentialsForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(UEssentials.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        this.instance = new UEssentials();
    }

    public UEssentials getActualInstance() {
        return instance;
    }
}