package io.github.ultreon.mods.essentials;

import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;

public class UEssentialsMain {
    public static final String NAMESPACE = "ultimate_essentials";
    public static final String NBT_NAME = "UltimateEssentials";
    public static final String MOD_VERSION;

    private static final ModuleManager moduleManager = new ModuleManager();

    static {
        // Create gson instance.
        Mod mod = Platform.getMod(NAMESPACE);
        MOD_VERSION = mod.getVersion();
    }

    private void loadModules() {

    }

    public static ResourceLocation res(String path) {
        return new ResourceLocation(NAMESPACE, path);
    }

    public static String subMod(String id) {
        return NAMESPACE + "_" + id;
    }
}
