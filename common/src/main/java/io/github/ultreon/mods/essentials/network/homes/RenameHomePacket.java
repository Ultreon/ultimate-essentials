package io.github.ultreon.mods.essentials.network.homes;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.Constants;
import io.github.ultreon.mods.essentials.homes.HomeReference;
import io.github.ultreon.mods.essentials.network.NetworkUtils;
import io.github.ultreon.mods.essentials.user.ServerUser;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

@Getter
public class RenameHomePacket extends PacketToServer<RenameHomePacket> {
    private final HomeReference home;
    private final String newName;

    public RenameHomePacket(FriendlyByteBuf buffer) {
        this.home = NetworkUtils.readHomeReference(buffer);
        this.newName = buffer.readUtf(Constants.MAX_HOME_NAME_LEN);
    }

    public RenameHomePacket(HomeReference home, String newName) {
        this.home = home;
        this.newName = newName;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser serverUser = ServerUser.get(sender);
        serverUser.getHomes().delete(home);
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        NetworkUtils.writeHomeReference(buffer, home);
        buffer.writeUtf(newName, Constants.MAX_HOME_NAME_LEN);
    }
}
