package io.github.ultreon.mods.essentials.network.teleport;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.teleport.ServerTeleportManager;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

@Getter
public class TeleportBackPacket extends PacketToServer<TeleportBackPacket> {
    public TeleportBackPacket(FriendlyByteBuf buf) {
        super();
    }

    public TeleportBackPacket() {

    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerTeleportManager.get(sender).back();
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {

    }
}
