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
import org.jetbrains.annotations.NotNull;

@Getter
public class DeleteWarpPacket extends PacketToServer<DeleteWarpPacket> {
    private final WarpReference warp;

    public DeleteWarpPacket(FriendlyByteBuf buffer) {
        this.warp = NetworkUtils.readWarpReference(buffer);
    }

    public DeleteWarpPacket(WarpReference warp) {
        this.warp = warp;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(sender);
        if (!user.hasPermission(Permissions.DELETE_WARPS)) {
            user.handleIllegalPacket();
            return;
        }

        UEssentials.getWarps().delete(warp);
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        NetworkUtils.writeWarpReference(buffer, warp);
    }
}
