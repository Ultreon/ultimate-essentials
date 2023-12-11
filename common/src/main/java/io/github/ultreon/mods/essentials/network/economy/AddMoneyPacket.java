package io.github.ultreon.mods.essentials.network.economy;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.user.ServerUser;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class AddMoneyPacket extends PacketToServer<AddMoneyPacket> {
    private final UUID target;
    private final double amount;

    public AddMoneyPacket(FriendlyByteBuf buf) {
        super();
        this.target = buf.readUUID();
        this.amount = buf.readDouble();
    }

    public AddMoneyPacket(UUID target, double amount) {
        super();
        this.target = target;
        this.amount = amount;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser targetUser = ServerUser.get(target);
        targetUser.addMoney(amount);
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.target);
        buffer.writeDouble(this.amount);
    }
}
