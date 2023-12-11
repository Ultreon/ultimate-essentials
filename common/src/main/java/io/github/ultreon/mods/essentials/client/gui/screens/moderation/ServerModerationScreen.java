package io.github.ultreon.mods.essentials.client.gui.screens.moderation;

import com.ultreon.mods.lib.client.gui.screen.TitleStyle;
import io.github.ultreon.mods.essentials.client.gui.screens.TextInsertScreen;
import io.github.ultreon.mods.essentials.client.gui.screens.UEssentialsMenuScreen;
import io.github.ultreon.mods.essentials.client.gui.screens.teleportation.WarpsScreen;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.warps.SetWarpPacket;
import io.github.ultreon.mods.essentials.user.LocalUser;
import io.github.ultreon.mods.essentials.user.Permissions;
import net.minecraft.network.chat.Component;

public class ServerModerationScreen extends UEssentialsMenuScreen {
    public ServerModerationScreen() {
        super(new Properties().titleLang("gui.ultimate_essentials.moderation").titleStyle(TitleStyle.DETACHED));

        if (!LocalUser.get().isModerator()) return;

        addButtonRow(Component.literal("Show Warps"), WarpsScreen::open);

        if (LocalUser.get().hasPermission(Permissions.CREATE_WARPS)) {
            addButtonRow(Component.literal("Create a New Warp"), btn -> TextInsertScreen.open("Set a New Warp", "Name")
                    .done((textInsertScreen, s) -> Networking.get().sendToServer(new SetWarpPacket(s)))
                    .execute());
        }

        if (LocalUser.get().hasPermission(Permissions.WEATHER_SET)) {
            addButtonRow(Component.literal("Create a New Warp"), btn -> new WeatherSetScreen().open());
        }
    }
}
