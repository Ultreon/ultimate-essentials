package io.github.ultreon.mods.essentials.teleport;

import io.github.ultreon.mods.essentials.client.gui.screens.toast.FailureToast;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.moderation.TeleportMeToPacket;
import io.github.ultreon.mods.essentials.network.moderation.TeleportToMePacket;
import io.github.ultreon.mods.essentials.network.teleport.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class ClientTeleportManager extends BaseTeleportManager {
    private static final ClientTeleportManager instance = new ClientTeleportManager();

    public ClientTeleportManager() {

    }

    public static ClientTeleportManager get() {
        return instance;
    }

    @Override
    public void requestTeleportTo(UUID uuid) {
        Networking.get().sendToServer(new RequestTpToPacket(uuid));
    }

    @Override
    public void requestTeleportFrom(UUID uuid) {
        Networking.get().sendToServer(new RequestTpFromPacket(uuid));
    }

    public void teleportMeTo(UUID playerUUID) {
        Networking.get().sendToServer(new TeleportMeToPacket(playerUUID));
    }

    public void teleportToMe(UUID playerUUID) {
        Networking.get().sendToServer(new TeleportToMePacket(playerUUID));
    }

    public void teleportMeTo(AbstractClientPlayer player) {
        Networking.get().sendToServer(new TeleportMeToPacket(player.getUUID()));
    }

    public void teleportToMe(AbstractClientPlayer player) {
        Networking.get().sendToServer(new TeleportToMePacket(player.getUUID()));
    }

    @Override
    public void requestTeleportEntity(UUID uuid) {
        // Nope
    }

    @Override
    public Player getPlayer() {
        return getClientPlayer();
    }

    public LocalPlayer getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    public void receiveTeleportRequestFrom(UUID playerUUID) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            PlayerInfo playerInfo = connection.getPlayerInfo(playerUUID);
            FailureToast failureToast;
            if (playerInfo != null) {
                failureToast = new FailureToast(
                        Component.literal("TP request to you"),
                        Component.literal(playerInfo.getProfile().getName()));
                Minecraft.getInstance().getToasts().addToast(failureToast);
            }
        }
    }

    public void receiveTeleportRequestTo(UUID playerUUID) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            PlayerInfo playerInfo = connection.getPlayerInfo(playerUUID);
            FailureToast failureToast;
            if (playerInfo != null) {
                failureToast = new FailureToast(
                        Component.literal("Request to TP to:"),
                        Component.literal(playerInfo.getProfile().getName()));
                Minecraft.getInstance().getToasts().addToast(failureToast);
            }
        }
    }

    @Override
    public void accept(UUID requester) {
        Networking.get().sendToServer(new AcceptTpRequestPacket(requester));
    }

    @Override
    public void deny(UUID requester) {
        Networking.get().sendToServer(new DenyTpRequestPacket(requester));
    }

    public void back() {
        Networking.get().sendToServer(new TeleportBackPacket());
    }
}
