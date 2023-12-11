package io.github.ultreon.mods.essentials.network.moderation;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.teleport.ServerTeleportManager;
import io.github.ultreon.mods.essentials.user.Permissions;
import io.github.ultreon.mods.essentials.user.ServerUser;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

@Getter
public class TeleportMeToPacket extends PacketToServer<TeleportMeToPacket> {
    private final UUID dest;

    public TeleportMeToPacket(FriendlyByteBuf buf) {
        super();
        this.dest = buf.readUUID();
    }

    public TeleportMeToPacket(UUID player) {
        super();
        this.dest = player;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(Objects.requireNonNull(sender));
        if (user.hasPermission(Permissions.TELEPORT_ME)) {
            UEssentials.LOGGER.debug("Teleport player " + sender.getGameProfile().getName() + " to: " + Objects.requireNonNull(UEssentials.server().getPlayerList().getPlayer(dest)).getGameProfile().getName());
            ServerTeleportManager.get(sender).teleportTo(dest);
        }
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.dest);
    }
}
