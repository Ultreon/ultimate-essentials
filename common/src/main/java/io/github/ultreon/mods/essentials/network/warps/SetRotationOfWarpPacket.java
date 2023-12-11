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
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

@Getter
public class SetRotationOfWarpPacket extends PacketToServer<SetRotationOfWarpPacket> {
    private final WarpReference warp;
    private final Vec2 newRot;

    public SetRotationOfWarpPacket(FriendlyByteBuf buffer) {
        this.warp = NetworkUtils.readWarpReference(buffer);
        this.newRot = NetworkUtils.readVec2(buffer);
    }

    public SetRotationOfWarpPacket(WarpReference warp, Vec2 newRot) {
        this.warp = warp;
        this.newRot = newRot;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(sender);
        if (!user.hasPermission(Permissions.SET_ROTATION_WARPS)) {
            user.handleIllegalPacket();
            return;
        }

        UEssentials.getWarps().rotate(warp, newRot);
    }

    public WarpReference getHome() {
        return warp;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        NetworkUtils.writeWarpReference(buffer, warp);
        NetworkUtils.writeVec2(buffer, newRot);
    }
}
