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
public class RequestTpToPacket extends PacketToServer<RequestTpToPacket> {
    private final UUID playerUUID;

    public RequestTpToPacket(FriendlyByteBuf buf) {
        super();
        this.playerUUID = buf.readUUID();
    }

    public RequestTpToPacket(UUID playerTo) {
        super();
        this.playerUUID = playerTo;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        UEssentials.LOGGER.debug("TP Request for " + sender.getGameProfile().getName() + " from: " + Objects.requireNonNull(UEssentials.server().getPlayerList().getPlayer(playerUUID)).getGameProfile().getName());
        ServerTeleportManager.get(sender).requestTeleportTo(playerUUID);
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.playerUUID);
    }
}
