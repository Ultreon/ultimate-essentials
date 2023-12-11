package io.github.ultreon.mods.essentials.network.teleport;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.teleport.ServerTeleportManager;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

@Getter
public class AcceptTpRequestPacket extends PacketToServer<AcceptTpRequestPacket> {
    private final UUID reqSender;

    public AcceptTpRequestPacket(FriendlyByteBuf buf) {
        super();
        this.reqSender = buf.readUUID();
    }

    public AcceptTpRequestPacket(UUID reqSender) {
        super();
        this.reqSender = reqSender;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerTeleportManager.get(Objects.requireNonNull(sender)).accept(reqSender);
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.reqSender);
    }
}
