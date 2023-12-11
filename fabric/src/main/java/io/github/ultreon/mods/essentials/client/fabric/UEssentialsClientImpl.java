package io.github.ultreon.mods.essentials.client.fabric;

import com.terraformersmc.modmenu.gui.ModsScreen;
import dev.architectury.platform.Platform;
import net.minecraft.client.Minecraft;

public class UEssentialsClientImpl {
    public static void showModList() {
        if (Platform.isModLoaded("modmenu")) {
            Minecraft.getInstance().setScreen(new ModsScreen(Minecraft.getInstance().screen));
        }
    }
}
