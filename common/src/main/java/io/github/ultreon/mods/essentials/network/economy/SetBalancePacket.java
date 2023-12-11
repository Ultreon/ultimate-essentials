package io.github.ultreon.mods.essentials.network.economy;

import com.ultreon.mods.lib.network.api.packet.BiDirectionalPacket;
import io.github.ultreon.mods.essentials.client.gui.screens.ShopScreen;
import io.github.ultreon.mods.essentials.user.LocalUser;
import io.github.ultreon.mods.essentials.user.ServerUser;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

@Getter
public class SetBalancePacket extends BiDirectionalPacket<SetBalancePacket> {
    private final double amount;

    public SetBalancePacket(FriendlyByteBuf buf) {
        super();
        amount = buf.readDouble();
    }

    public SetBalancePacket(double amount) {
        this.amount = amount;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(this.amount);
    }

    @Override
    protected void handleClient() {
        LocalUser targetUser = LocalUser.get();
        targetUser.handlePacket(this);

        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof ShopScreen scr) {
            scr.handlePacket(this);
        }
    }

    @Override
    protected void handleServer(ServerPlayer sender) {
        ServerUser targetUser = ServerUser.get(sender);
        targetUser.setBalance(amount);
    }
}
