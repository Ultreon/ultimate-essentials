package io.github.ultreon.mods.essentials.network.economy;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.user.ServerUser;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

@Getter
public class ExchangeExpPacket extends PacketToServer<ExchangeExpPacket> {
    private final int money;

    public ExchangeExpPacket(FriendlyByteBuf buf) {
        this.money = buf.readInt();
    }

    public ExchangeExpPacket(int money) {
        this.money = money;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(sender);
        if (user.canExchangeExp(money)) {
            user.exchangeExp(money);
        }
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(money);
    }
}
