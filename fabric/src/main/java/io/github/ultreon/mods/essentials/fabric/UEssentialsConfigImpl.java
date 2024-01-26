package io.github.ultreon.mods.essentials.fabric;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.UEssentialsConfig;
import net.minecraftforge.fml.config.ModConfig;

public class UEssentialsConfigImpl {
    public static void init() {
        ForgeConfigRegistry.INSTANCE.register(UEssentials.MOD_ID, ModConfig.Type.COMMON, UEssentialsConfig.COMMON_SPEC);
        ForgeConfigRegistry.INSTANCE.register(UEssentials.MOD_ID, ModConfig.Type.CLIENT, UEssentialsConfig.CLIENT_SPEC);
    }
}
