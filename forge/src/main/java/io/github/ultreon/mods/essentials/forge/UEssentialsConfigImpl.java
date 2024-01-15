package io.github.ultreon.mods.essentials.forge;

import io.github.ultreon.mods.essentials.UEssentialsConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class UEssentialsConfigImpl {
    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UEssentialsConfig.COMMON_SPEC, "essentials-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, UEssentialsConfig.CLIENT_SPEC, "essentials-client.toml"
    }
}
