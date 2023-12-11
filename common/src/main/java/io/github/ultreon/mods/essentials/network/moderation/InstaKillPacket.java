package io.github.ultreon.mods.essentials.network.moderation;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.user.Permissions;
import io.github.ultreon.mods.essentials.user.ServerUser;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class InstaKillPacket extends PacketToServer<InstaKillPacket> {
    private final UUID uuid;

    public InstaKillPacket(FriendlyByteBuf buf) {
        uuid = buf.readUUID();
    }

    public InstaKillPacket(UUID uuid) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser senderUser = ServerUser.get(sender);
        if (!senderUser.hasPermission(Permissions.INSTA_KILL)) {
            senderUser.handleIllegalPacket();
            return;
        }
        ServerUser.get(uuid).instaKill();
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(uuid);
    }
}
