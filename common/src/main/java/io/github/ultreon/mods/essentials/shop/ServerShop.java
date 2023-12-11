package io.github.ultreon.mods.essentials.shop;

import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.util.PagedList;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class ServerShop extends Shop {
    private static final List<Consumer<ServerShop>> loaders = new ArrayList<>();

    private final PagedList<ShopItem> items = new PagedList<>(ShopPage.PAGE_SIZE);
    private final Map<UUID, ShopItem> itemMap = new HashMap<>();

    private static ServerShop instance;

    public static ServerShop get() {
        return instance;
    }

    public ServerShop(CompoundTag tag) {
        this();

        items.clear();

        ListTag itemTags = tag.getList("Items", Tag.TAG_COMPOUND);

        for (Tag itemTag : itemTags) {
            if (itemTag instanceof CompoundTag) {
                ShopItem item = ShopItem.deserialize((CompoundTag) itemTag);
                items.add(item);
            }
        }
    }

    public ServerShop() {
        for (Consumer<ServerShop> loader : loaders) {
            loader.accept(this);
        }
    }

    public static void addLoader(Consumer<ServerShop> load) {
        loaders.add(load);
    }

    public static void save() {
        // Todo: add saving of shop
    }

    public ShopPage getPage(int page) {
        List<ShopItem> fullPage = items.getFullPage(page);
        return new ShopPage(NonNullList.of(ShopItem.EMPTY, Arrays.copyOf(fullPage.toArray(new ShopItem[]{}), ShopPage.PAGE_SIZE)));
    }

    public static void init() {

    }

    public static Shop load() {
        if (instance != null) {
            throw new IllegalStateException("Shop already loaded.");
        }

        instance = new ServerShop();

        File dataFile = UEssentials.getDataFile("shop.dat");
        try {
            CompoundTag nbt = NbtIo.read(dataFile);
            if (nbt == null) {
                return new ServerShop();
            }
            return new ServerShop(nbt);
        } catch (IOException e) {
            return new ServerShop();
        }
    }

    public static void unload() {
        instance = null;
    }

    public List<ShopItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public ShopItem getItem(UUID uuid) {
        return itemMap.get(uuid);
    }

    public int getPages() {
        return items.getPages();
    }

    public void addItem(ShopItem shopItem) {
        itemMap.put(shopItem.getUUID(), shopItem);
        items.add(shopItem);
    }

    public int buy(ServerUser user, UUID uuid) {
        return user.buy(uuid);
    }
}
