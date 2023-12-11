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
public class DeOpPlayerPacket extends PacketToServer<DeOpPlayerPacket> {
    private final UUID player;

    public DeOpPlayerPacket(FriendlyByteBuf buf) {
        super();
        this.player = buf.readUUID();
    }

    public DeOpPlayerPacket(UUID player) {
        super();
        this.player = player;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(player);
        if (user.hasPermission(Permissions.DEOP_PLAYERS)) {
            ServerUser.get(player).deop();
        }
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.player);
    }
}
