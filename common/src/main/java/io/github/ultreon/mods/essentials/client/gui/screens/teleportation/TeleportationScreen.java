package io.github.ultreon.mods.essentials.client.gui.screens.teleportation;

import com.ultreon.mods.lib.client.gui.screen.TitleStyle;
import io.github.ultreon.mods.essentials.client.gui.screens.UEssentialsMenuScreen;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.homes.ListHomesPacket;
import io.github.ultreon.mods.essentials.network.teleport.ListTpRequestsPacket;
import io.github.ultreon.mods.essentials.teleport.ClientTeleportManager;
import net.minecraft.network.chat.Component;

public class TeleportationScreen extends UEssentialsMenuScreen {
    public TeleportationScreen() {
        super(new Properties()
                .titleText("Main Teleportation Screen")
                .titleStyle(TitleStyle.DETACHED));
    }

    private void sendListRequest() {
        Networking.get().sendToServer(new ListTpRequestsPacket());
    }

    private void tpBack() {
        ClientTeleportManager.get().back();
    }

    @Override
    protected void init() {
        addButtonRow(
                Component.literal("Show Homes"), () -> Networking.get().sendToServer(new ListHomesPacket(ListHomesPacket.Type.PERSONAL)),
                Component.literal("New Home"), SetHome::open
        );
        addButtonRow(Component.literal("Show Warps"), WarpsScreen::open);
        addButtonRow(Component.literal("Teleport Requests"), this::sendListRequest);
        addButtonRow(Component.literal("Teleport Back"), this::tpBack);

        super.init();
    }
}
