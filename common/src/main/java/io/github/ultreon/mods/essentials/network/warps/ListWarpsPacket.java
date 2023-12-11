package io.github.ultreon.mods.essentials.network.warps;

import com.ultreon.mods.lib.network.api.packet.PacketToClient;
import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.Constants;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.client.gui.screens.teleportation.WarpsScreen;
import io.github.ultreon.mods.essentials.network.NetworkUtils;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.warps.WarpReference;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ListWarpsPacket extends PacketToServer<ListWarpsPacket> {
    public ListWarpsPacket(FriendlyByteBuf buf) {
        super();
    }

    public ListWarpsPacket() {
        super();
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {

    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        UEssentials.getWarps().showList(ServerUser.get(sender));
    }

    public static class Begin extends PacketToClient<Begin> {
        public Begin(FriendlyByteBuf buf) {
            super();
        }

        public Begin() {
            super();
        }

        public void toBytes(FriendlyByteBuf buf) {

        }

        @Override
        protected void handle() {
            WarpsScreen.handleListStart();
        }
    }

    public static class Entry extends PacketToClient<Entry> {
        private final WarpReference warp;

        public Entry(FriendlyByteBuf buf) {
            super();
            this.warp = NetworkUtils.readWarpReference(buf);
        }

        public Entry(WarpReference warp) {
            super();
            this.warp = warp;
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeUtf(warp.title(), Constants.MAX_WARP_TITLE_LEN);
            buf.writeUtf(warp.description(), Constants.MAX_WARP_DESCRIPTION_LEN);
        }

        @Override
        protected void handle() {
            WarpsScreen.handleWarp(warp);
        }
    }

    public static class End extends PacketToClient<End> {
        public End(FriendlyByteBuf buf) {
            super();
        }

        public End() {
            super();
        }

        public void toBytes(FriendlyByteBuf buf) {

        }

        @Override
        protected void handle() {
            WarpsScreen.handleListEnd();
        }

    }
}
