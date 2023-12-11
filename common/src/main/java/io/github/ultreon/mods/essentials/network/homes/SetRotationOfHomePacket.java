package io.github.ultreon.mods.essentials.network.homes;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.homes.HomeReference;
import io.github.ultreon.mods.essentials.network.NetworkUtils;
import io.github.ultreon.mods.essentials.user.ServerUser;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

@Getter
public class SetRotationOfHomePacket extends PacketToServer<SetRotationOfHomePacket> {
    private final HomeReference home;
    private final Vec2 newRot;

    public SetRotationOfHomePacket(FriendlyByteBuf buffer) {
        this.home = NetworkUtils.readHomeReference(buffer);
        this.newRot = NetworkUtils.readVec2(buffer);
    }

    public SetRotationOfHomePacket(HomeReference home, Vec2 newRot) {
        this.home = home;
        this.newRot = newRot;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser serverUser = ServerUser.get(sender);
        serverUser.getHomes().delete(home);
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        NetworkUtils.writeHomeReference(buffer, home);
        NetworkUtils.writeVec2(buffer, newRot);
    }
}
