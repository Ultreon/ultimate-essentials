package io.github.ultreon.mods.essentials.client;

import dev.architectury.event.CompoundEventResult;
import io.github.ultreon.mods.essentials.client.gui.screens.connect.ConnectServerScreen;
import io.github.ultreon.mods.essentials.client.gui.screens.message.ErrorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;

public class ClientEvents {
    public static CompoundEventResult<Screen> onScreenOpen(Screen screen) {
        Minecraft mc = Minecraft.getInstance();

        if ((screen instanceof DisconnectedScreen || screen instanceof ProgressScreen) && mc.screen instanceof ErrorScreen) {
            return CompoundEventResult.interruptTrue(mc.screen);
        }

        if (screen instanceof JoinMultiplayerScreen) {
            CompoundEventResult.interruptTrue(new ConnectServerScreen());
        }

        return CompoundEventResult.pass();
    }
}
