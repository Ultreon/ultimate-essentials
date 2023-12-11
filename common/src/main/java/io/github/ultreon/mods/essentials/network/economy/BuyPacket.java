package io.github.ultreon.mods.essentials.network.economy;

import com.ultreon.mods.lib.network.api.packet.PacketToClient;
import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.init.ModSounds;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.shop.ServerShop;
import io.github.ultreon.mods.essentials.user.ServerUser;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@SuppressWarnings({"unused", "DuplicatedCode"})
public class BuyPacket extends PacketToServer<BuyPacket> {
    private final UUID uuid;

    public BuyPacket(FriendlyByteBuf buf) {
        super();
        this.uuid = buf.readUUID();
    }

    public BuyPacket(UUID uuid) {
        super();
        this.uuid = uuid;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(sender);
        ServerShop shop = ServerShop.get();
        Networking.get().sendToClient(new Response(shop.buy(user, uuid)), sender);
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUUID(uuid);
    }

    @Getter
    public static class Response extends PacketToClient<Response> {
        private final int code;

        public Response(FriendlyByteBuf buf) {
            super();
            code = buf.readInt();
        }

        public Response(int code) {
            super();
            this.code = code;
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeInt(code);
        }

        @Override
        protected void handle() {
            // Todo: add menu.
            if (code == 0) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.SHOP_BUY, 1.0F));
            } else {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.ERROR, 1.0F));
            }
        }
    }
}
