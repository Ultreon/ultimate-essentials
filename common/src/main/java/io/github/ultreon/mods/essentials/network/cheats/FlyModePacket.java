package io.github.ultreon.mods.essentials.network.cheats;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.user.Permissions;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.user.User;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

@Getter
public class FlyModePacket extends PacketToServer<FlyModePacket> {
    private final User user;
    private final boolean enable;

    public FlyModePacket(FriendlyByteBuf buf) {
        user = ServerUser.get(buf.readUUID());
        enable = buf.readBoolean();
    }

    public FlyModePacket(User user, boolean enable) {
        this.user = user;
        this.enable = enable;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        if (!(user instanceof ServerUser serverUser)) return;
        if (!user.hasPermission(Permissions.GOD_MODE)) serverUser.handleIllegalPacket();
        serverUser.setFlyMode(enable);
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(user.uuid());
        buffer.writeBoolean(enable);
    }
}
