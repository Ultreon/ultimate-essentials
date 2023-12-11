package io.github.ultreon.mods.essentials.network.homes;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.util.homes.ServerHome;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

@Getter
public class SetHomePacket extends PacketToServer<SetHomePacket> {
    private final String name;

    public SetHomePacket(FriendlyByteBuf buf) {
        super();
        this.name = buf.readUtf(20);
    }

    public SetHomePacket(String name) {
        super();
        this.name = name;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(sender);

        if (StringUtils.isAllBlank(name)) {
            // Todo: add default home here.
            return;
        }
        UEssentials.LOGGER.debug("Set home for " + sender.getName().getString() + " with name: " + this.name);
        ServerHome home = new ServerHome(this.name, sender.getX(), sender.getY(), sender.getZ(), sender.getXRot(), sender.getYRot(), sender.serverLevel().dimension());
        user.getHomes().setHome(home);
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.name);
    }
}
