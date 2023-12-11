package io.github.ultreon.mods.essentials.network.homes;

import com.ultreon.mods.lib.network.api.packet.PacketToClient;
import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.client.gui.screens.teleportation.HomesScreen;
import io.github.ultreon.mods.essentials.homes.Home;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.util.homes.ServerHomes;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

@Getter
@SuppressWarnings("unused")
public class ListHomesPacket extends PacketToServer<ListHomesPacket> {
    private final Type type;

    public ListHomesPacket(FriendlyByteBuf buf) {
        super();
        this.type = buf.readEnum(Type.class);
    }

    public ListHomesPacket(Type type) {
        super();
        this.type = type;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(sender);
        ServerHomes homes = user.getHomes();
        homes.showList(sender);
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.type);
    }

    public enum Type {
        PERSONAL, OTHER
    }

    @Getter
    public static class Begin extends PacketToClient<Begin> {
        private final Type type;

        public Begin(FriendlyByteBuf buf) {
            super();
            this.type = buf.readEnum(Type.class);
        }

        public Begin(Type type) {
            super();
            this.type = type;
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeEnum(type);
        }

        @Override
        protected void handle() {
            HomesScreen.handleListStart();
        }
    }

    @Getter
    public static class Entry extends PacketToClient<Entry> {
        private final Home home;

        public Entry(FriendlyByteBuf buf) {
            super();
            this.home = Home.read(buf);
        }

        public Entry(Home home) {
            super();
            this.home = home;
        }

        public void toBytes(FriendlyByteBuf buf) {
            home.write(buf);
        }

        @Override
        protected void handle() {
            HomesScreen.handleHome(home);
        }
    }

    @Getter
    public static class End extends PacketToClient<End> {
        private final Type type;

        public End(FriendlyByteBuf buf) {
            super();
            this.type = buf.readEnum(Type.class);
        }

        public End(Type type) {
            super();
            this.type = type;
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeEnum(type);
        }

        @Override
        protected void handle() {
            HomesScreen.handleListEnd();
        }
    }
}
