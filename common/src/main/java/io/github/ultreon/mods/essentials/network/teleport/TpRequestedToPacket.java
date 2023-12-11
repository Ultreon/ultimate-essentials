package io.github.ultreon.mods.essentials.network.teleport;

import com.ultreon.mods.lib.network.api.packet.PacketToClient;
import io.github.ultreon.mods.essentials.teleport.ClientTeleportManager;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

@Getter
public class TpRequestedToPacket extends PacketToClient<TpRequestedToPacket> {
    private final UUID sender;

    public TpRequestedToPacket(FriendlyByteBuf buf) {
        super();
        this.sender = buf.readUUID();
    }

    public TpRequestedToPacket(UUID sender) {
        super();
        this.sender = sender;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.sender);
    }

    @Override
    protected void handle() {
        ClientTeleportManager.get().receiveTeleportRequestTo(sender);
    }
}
