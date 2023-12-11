package io.github.ultreon.mods.essentials.network.teleport;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.teleport.ServerTeleportManager;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

@Getter
public class RequestTpFromPacket extends PacketToServer<RequestTpFromPacket> {
    private final UUID playerUUID;

    public RequestTpFromPacket(FriendlyByteBuf buf) {
        super();
        this.playerUUID = buf.readUUID();
    }

    public RequestTpFromPacket(UUID playerTo) {
        super();
        this.playerUUID = playerTo;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        UEssentials.LOGGER.debug("TP Request for " + sender.getGameProfile().getName() + " to: " + Objects.requireNonNull(UEssentials.server().getPlayerList().getPlayer(playerUUID)).getGameProfile().getName());
        ServerTeleportManager.get(sender).requestTeleportFrom(playerUUID);
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.playerUUID);
    }
}
