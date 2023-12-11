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
public class TeleportToMePacket extends PacketToServer<TeleportToMePacket> {
    private final UUID orig;

    public TeleportToMePacket(FriendlyByteBuf buf) {
        super();
        this.orig = buf.readUUID();
    }

    public TeleportToMePacket(UUID orig) {
        super();
        this.orig = orig;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(sender);
        if (user.hasPermission(Permissions.TELEPORT_OTHERS)) {
            UEssentials.LOGGER.debug("Teleport player " + sender.getGameProfile().getName() + " to: " + Objects.requireNonNull(UEssentials.server().getPlayerList().getPlayer(orig)).getGameProfile().getName());
            ServerTeleportManager.get(sender).teleportHere(orig);
        }
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.orig);
    }
}
