package io.github.ultreon.mods.essentials.network.moderation;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.user.Permissions;
import io.github.ultreon.mods.essentials.user.ServerUser;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
public class KickPlayerPacket extends PacketToServer<KickPlayerPacket> {
    private final UUID player;
    @Nullable
    private final Component reason;

    public KickPlayerPacket(FriendlyByteBuf buffer) {
        this.player = buffer.readUUID();
        if (buffer.readBoolean()) this.reason = buffer.readComponent();
        else this.reason = null;
    }

    public KickPlayerPacket(UUID player, @Nullable String reason) {
        this(player, reason == null ? null : Component.literal(reason));
    }

    public KickPlayerPacket(UUID player, @Nullable Component reason) {
        this.player = player;
        this.reason = reason;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(sender);
        if (user.hasPermission(Permissions.KICK_PLAYERS)) {
            ServerUser.get(player).kick(reason);
        }
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.player);
        buffer.writeBoolean(this.reason != null);

        if (this.reason != null) buffer.writeComponent(this.reason);
    }
}
