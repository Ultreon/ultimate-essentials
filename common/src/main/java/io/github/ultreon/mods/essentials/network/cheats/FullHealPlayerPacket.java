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
public class FullHealPlayerPacket extends PacketToServer<FullHealPlayerPacket> {
    private final UUID playerUUID;

    public FullHealPlayerPacket(FriendlyByteBuf buffer) {
        this.playerUUID = buffer.readUUID();
    }

    public FullHealPlayerPacket(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser serverUser = ServerUser.get(sender);
        if (serverUser.hasPermission(Permissions.CHEATS_FULL_HEAL)) {
            ServerUser.get(playerUUID).fullHeal();
        }
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.playerUUID);
    }
}
