package io.github.ultreon.mods.essentials.network.warps;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.user.Permissions;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.util.warps.Warp;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

@Getter
public class SetWarpPacket extends PacketToServer<SetWarpPacket> {
    private final String name;

    public SetWarpPacket(FriendlyByteBuf buf) {
        super();
        this.name = buf.readUtf(20);
    }

    public SetWarpPacket(String name) {
        super();
        this.name = name;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(sender);
        if (!user.hasPermission(Permissions.CREATE_WARPS)) {
            user.handleIllegalPacket();
            return;
        }

        if (StringUtils.isAllBlank(name)) {
            // Todo: add default home here.
            return;
        }

        UEssentials.LOGGER.debug("Set warp with name: " + this.name);
        UEssentials.getWarps().setWarp(new Warp(name, sender.getX(), sender.getY(), sender.getZ(), sender.getXRot(), sender.getYRot(), sender.serverLevel().dimension()));
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.name);
    }
}
