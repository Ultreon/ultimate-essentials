package io.github.ultreon.mods.essentials.client.forge;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.ModListScreen;

public class UEssentialsClientImpl {
    public static void showModList() {
        Minecraft.getInstance().setScreen(new ModListScreen(Minecraft.getInstance().screen));
    }
}
