package io.github.ultreon.mods.essentials.network.warps;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.network.NetworkUtils;
import io.github.ultreon.mods.essentials.user.Permissions;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.warps.WarpReference;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

@Getter
public class RepositionWarpPacket extends PacketToServer<RepositionWarpPacket> {
    private final WarpReference warp;
    @Getter
    private final Vec3 newPos;

    public RepositionWarpPacket(FriendlyByteBuf buffer) {
        this.warp = NetworkUtils.readWarpReference(buffer);
        this.newPos = NetworkUtils.readVec3(buffer);
    }

    public RepositionWarpPacket(WarpReference warp, Vec3 newPos) {
        this.warp = warp;
        this.newPos = newPos;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(sender);
        if (!user.hasPermission(Permissions.REPOSITION_WARPS)) {
            user.handleIllegalPacket();
            return;
        }

        UEssentials.getWarps().reposition(warp, newPos);
    }

    public WarpReference getHome() {
        return warp;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        NetworkUtils.writeWarpReference(buffer, warp);
        NetworkUtils.writeVec3(buffer, newPos);
    }
}
