package io.github.ultreon.mods.essentials.network.economy;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.user.ServerUser;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class ReduceMoneyPacket extends PacketToServer<ReduceMoneyPacket> {
    private final UUID targetPlayer;
    private final double amount;

    public ReduceMoneyPacket(FriendlyByteBuf buf) {
        super();
        this.targetPlayer = buf.readUUID();
        this.amount = buf.readDouble();
    }

    public ReduceMoneyPacket(UUID targetPlayer, double amount) {
        super();
        this.targetPlayer = targetPlayer;
        this.amount = amount;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser.get(sender).reduceMoney(amount);
        ServerUser.get(targetPlayer).addMoney(amount);
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(targetPlayer);
        buffer.writeDouble(amount);
    }
}
