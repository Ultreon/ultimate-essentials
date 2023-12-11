
package io.github.ultreon.mods.essentials.network.cheats;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.user.Permissions;
import io.github.ultreon.mods.essentials.user.ServerUser;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class BurnPlayerPacket extends PacketToServer<BurnPlayerPacket> {
    private final UUID playerUUID;
    private final int seconds;

    public BurnPlayerPacket(FriendlyByteBuf buffer) {
        this.playerUUID = buffer.readUUID();
        this.seconds = buffer.readInt();
    }

    public BurnPlayerPacket(UUID playerUUID, int seconds) {
        this.playerUUID = playerUUID;
        this.seconds = seconds;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser serverUser = ServerUser.get(sender);
        if (serverUser.hasPermission(Permissions.CHEATS_BURN)) {
            ServerUser.get(playerUUID).burn(seconds);
        }
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.playerUUID);
        buffer.writeInt(this.seconds);
    }
}
