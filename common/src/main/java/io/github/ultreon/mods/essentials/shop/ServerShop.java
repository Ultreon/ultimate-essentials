package io.github.ultreon.mods.essentials.shop;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.util.PagedList;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.util.GsonHelper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class ServerShop extends Shop {
    public static final Codec<ServerShop> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        Codec.list(ShopItem.CODEC).fieldOf("items").forGetter(ServerShop::getItems)
    ).apply(instance, ServerShop::new));

    private static final List<Consumer<ServerShop>> loaders = new ArrayList<>();

    private final PagedList<ShopItem> items = new PagedList<>(ShopPage.PAGE_SIZE);
    private final Map<UUID, ShopItem> itemMap = new HashMap<>();

    private static ServerShop instance;

    private ServerShop(List<ShopItem> shopItems) {
        items.addAll(shopItems);
        for (ShopItem item : shopItems) {
            itemMap.put(item.getUUID(), item);
        }
    }

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

    public static ServerShop load() {
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

    public static ServerShop readFromJson() {
        File dataFile = UEssentials.getDataFile("shop.json");
        if (dataFile.exists()) {
            try (FileReader reader = new FileReader(dataFile)) {
                ServerShop serverShop = CODEC.parse(JsonOps.INSTANCE, GsonHelper.fromJson(UEssentials.GSON, reader, JsonObject.class)).resultOrPartial(UEssentials.LOGGER::error).orElseThrow();
                instance = serverShop;
                return serverShop;
            } catch (IOException e) {
                UEssentials.LOGGER.error("Failed to read shop data from file.", e);
            }
        }

        return null;
    }

    public void writeToJson(boolean ignoreIfExists) {
        File dataFile = UEssentials.getDataFile("shop.json");
        if (ignoreIfExists && dataFile.exists()) {
            return;
        }

        CODEC.encodeStart(JsonOps.INSTANCE, this).resultOrPartial(UEssentials.LOGGER::error).ifPresent((json) -> {
            try (FileWriter writer = new FileWriter(dataFile)) {
                UEssentials.GSON.toJson(json, writer);
            } catch (IOException e) {
                UEssentials.LOGGER.error("Failed to write shop data to file.", e);
            }
        });
    }
}
