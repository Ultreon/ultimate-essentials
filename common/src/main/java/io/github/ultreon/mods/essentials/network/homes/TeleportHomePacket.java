package io.github.ultreon.mods.essentials.network.homes;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.homes.Home;
import io.github.ultreon.mods.essentials.homes.HomeReference;
import io.github.ultreon.mods.essentials.network.NetworkUtils;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.util.homes.ServerHomes;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

@Getter
@SuppressWarnings("unused")
public class TeleportHomePacket extends PacketToServer<TeleportHomePacket> {
    private final HomeReference home;

    public TeleportHomePacket(FriendlyByteBuf buffer) {
        home = NetworkUtils.readHomeReference(buffer);
    }

    public TeleportHomePacket(Home home) {
        this.home = home::getName;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(sender);
        ServerHomes homes = user.getHomes();

        if (homes.has(this.home)) {
            homes.teleport(sender, this.home);
        } else {
            UEssentials.LOGGER.warn("Illegal home packet: " + home);
        }
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        NetworkUtils.writeHomeReference(buffer, this.home);
    }
}
