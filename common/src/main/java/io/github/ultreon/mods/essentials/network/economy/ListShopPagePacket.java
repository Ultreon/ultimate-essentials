package io.github.ultreon.mods.essentials.network.economy;

import com.ultreon.mods.lib.network.api.packet.PacketToClient;
import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.client.gui.screens.ShopScreen;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.shop.ServerShop;
import io.github.ultreon.mods.essentials.shop.ShopItem;
import io.github.ultreon.mods.essentials.shop.ShopPage;
import io.github.ultreon.mods.essentials.user.ServerUser;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@SuppressWarnings({"unused", "DuplicatedCode"})
public class ListShopPagePacket extends PacketToServer<ListShopPagePacket> {
    private Type type;

    public ListShopPagePacket(FriendlyByteBuf buffer) {
        super();
    }

    public ListShopPagePacket() {
        super();
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {

    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(sender);
        ServerShop shop = ServerShop.get();
        int pages = shop.getPages();
        Networking.get().sendToClient(new Begin(pages, user.getBalance()), sender);

        for (int page = 0; page < pages; page++) {
            List<ShopItem> items = shop.getPage(page).getItems();
            Networking.get().sendToClient(new Page(page, items), sender);
        }

        Networking.get().sendToClient(new End(), sender);
    }

    public enum Type {
        PERSONAL, OTHER
    }

    @Getter
    public static class Begin extends PacketToClient<Begin> {
        private final int size;
        private final double balance;

        public Begin(FriendlyByteBuf buf) {
            super();
            size = buf.readInt();
            balance = buf.readDouble();
        }

        public Begin(int size, double balance) {
            super();
            this.size = size;
            this.balance = balance;
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeInt(size);
            buf.writeDouble(balance);
        }

        @Override
        protected void handle() {
            Screen screen = Minecraft.getInstance().screen;
            if (screen instanceof ShopScreen scr) {
                scr.handlePacket(this);
            }
        }
    }

    @Getter
    public static class Page extends PacketToClient<Page> {
        private final List<ShopItem> items;
        @Getter
        private final int page;

        public Page(FriendlyByteBuf buf) {
            super();
            page = buf.readInt();

            NonNullList<ShopItem> items = NonNullList.create();
            for (int i = 0; i < ShopPage.PAGE_SIZE; i++) {
                items.add(ShopItem.read(buf));
            }

            this.items = items;

        }

        public Page(int page, List<ShopItem> items) {
            super();
            this.page = page;
            this.items = items;
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeInt(page);

            for (ShopItem item : items) {
                Objects.requireNonNullElse(item, ShopItem.EMPTY).write(buf);
            }
        }

        public List<ShopItem> getItems() {
            return Collections.unmodifiableList(this.items);
        }

        @Override
        protected void handle() {
            Screen screen = Minecraft.getInstance().screen;
            if (screen instanceof ShopScreen scr) {
                scr.handlePacket(this);
            }
        }
    }

    @Getter
    public static class End extends PacketToClient<End> {
        public End(FriendlyByteBuf buffer) {
            super();
        }

        public End() {
            super();
        }

        public void toBytes(FriendlyByteBuf buf) {
        }

        @Override
        protected void handle() {
            Screen screen = Minecraft.getInstance().screen;
            if (screen instanceof ShopScreen scr) {
                scr.handlePacket(this);
            }
        }
    }
}
