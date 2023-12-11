package io.github.ultreon.mods.essentials.network.homes;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.homes.HomeReference;
import io.github.ultreon.mods.essentials.network.NetworkUtils;
import io.github.ultreon.mods.essentials.user.ServerUser;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

@Getter
public class RepositionHomePacket extends PacketToServer<RepositionHomePacket> {
    private final HomeReference home;
    private final Vec3 newPos;

    public RepositionHomePacket(FriendlyByteBuf buffer) {
        this.home = NetworkUtils.readHomeReference(buffer);
        this.newPos = NetworkUtils.readVec3(buffer);
    }

    public RepositionHomePacket(HomeReference home, Vec3 newPos) {
        this.home = home;
        this.newPos = newPos;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser serverUser = ServerUser.get(sender);
        serverUser.getHomes().delete(home);
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        NetworkUtils.writeHomeReference(buffer, home);
        NetworkUtils.writeVec3(buffer, newPos);
    }
}
