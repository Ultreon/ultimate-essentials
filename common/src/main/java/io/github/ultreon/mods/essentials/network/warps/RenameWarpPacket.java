package io.github.ultreon.mods.essentials.network.warps;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.Constants;
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
public class RenameWarpPacket extends PacketToServer<RenameWarpPacket> {
    private final WarpReference warp;
    @Getter
    private final String newName;

    public RenameWarpPacket(FriendlyByteBuf buffer) {
        this.warp = NetworkUtils.readWarpReference(buffer);
        this.newName = buffer.readUtf(Constants.MAX_HOME_NAME_LEN);
    }

    public RenameWarpPacket(WarpReference warp, String newName) {
        this.warp = warp;
        this.newName = newName;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(sender);
        if (!user.hasPermission(Permissions.RENAME_WARPS)) {
            user.handleIllegalPacket();
            return;
        }

        UEssentials.getWarps().rename(warp, newName);
    }

    public WarpReference getHome() {
        return warp;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        NetworkUtils.writeWarpReference(buffer, warp);
        buffer.writeUtf(newName, Constants.MAX_HOME_NAME_LEN);
    }
}
