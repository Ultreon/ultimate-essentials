package io.github.ultreon.mods.essentials.network.homes;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.homes.HomeReference;
import io.github.ultreon.mods.essentials.network.NetworkUtils;
import io.github.ultreon.mods.essentials.user.ServerUser;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

@Getter
public class DeleteHomePacket extends PacketToServer<DeleteHomePacket> {
    private final HomeReference home;

    public DeleteHomePacket(FriendlyByteBuf buffer) {
        this.home = NetworkUtils.readHomeReference(buffer);
    }

    public DeleteHomePacket(HomeReference home) {
        this.home = home;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser serverUser = ServerUser.get(sender);
        serverUser.getHomes().delete(home);
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        NetworkUtils.writeHomeReference(buffer, home);
    }
}
