package io.github.ultreon.mods.essentials.network.teleport;

import com.ultreon.mods.lib.network.api.packet.PacketToClient;
import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.client.gui.screens.TpRequestsScreen;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.teleport.ServerTeleportManager;
import io.github.ultreon.mods.essentials.teleport.TeleportRequest;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Getter
public class ListTpRequestsPacket extends PacketToServer<ListTpRequestsPacket> {
    public ListTpRequestsPacket(FriendlyByteBuf ignoredBuf) {
        super();
    }
    public ListTpRequestsPacket() {
        super();
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {

    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerTeleportManager homes = ServerTeleportManager.get(sender);
        Set<TeleportRequest> requests = homes.getRequests();

        Networking.get().sendToClient(new Begin(), sender);
        for (TeleportRequest request : requests) {
            UEssentials.LOGGER.debug(request);
            Networking.get().sendToClient(new Entry(request), sender);
        }
        Networking.get().sendToClient(new End(), sender);
    }

    public static class Begin extends PacketToClient<Begin> {
        public Begin(FriendlyByteBuf ignoredBuf) {
            super();
        }
        public Begin() {
            super();
        }

        public void toBytes(FriendlyByteBuf buf) {

        }

        @Override
        protected void handle() {
            TpRequestsScreen.handleListStart();
        }
    }

    public static class Entry extends PacketToClient<Entry> {
        private final TeleportRequest request;

        public Entry(FriendlyByteBuf buf) {
            super();
            this.request = TeleportRequest.read(buf);
        }

        public Entry(TeleportRequest request) {
            super();
            this.request = request;
        }

        public void toBytes(FriendlyByteBuf buf) {
            request.write(buf);
        }

        @Override
        protected void handle() {
            TpRequestsScreen.handleEntry(request);
        }
    }

    public static class End extends PacketToClient<End> {
        public End(FriendlyByteBuf ignoredBuf) {
            super();
        }
        public End() {
            super();
        }

        public void toBytes(FriendlyByteBuf buf) {

        }

        @Override
        protected void handle() {
            TpRequestsScreen.handleListEnd();
        }
    }
}
