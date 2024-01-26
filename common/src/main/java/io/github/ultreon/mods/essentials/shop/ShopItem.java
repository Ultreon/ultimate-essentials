package io.github.ultreon.mods.essentials.shop;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ShopItem {
    public static final Codec<ShopItem> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ItemStack.CODEC.fieldOf("item").forGetter(ShopItem::getItem),
            Codec.DOUBLE.fieldOf("price").forGetter(ShopItem::getPrice)
    ).apply(instance, ShopItem::new));

    private ItemStack item;
    @Getter
    @Setter
    private double price;
    private final UUID uniqueId;

    private static final Set<UUID> ids = new HashSet<>();

    public static final ShopItem EMPTY = new ShopItem(ItemStack.EMPTY);

    public ShopItem(UUID uniqueId, ItemStack item, double price) {
        this.item = item;
        this.price = BigDecimal.valueOf(price).setScale(2, RoundingMode.CEILING).doubleValue();
        this.uniqueId = uniqueId;
    }

    public ShopItem(ItemStack item, double price) {
        this.item = item;
        this.price = BigDecimal.valueOf(price).setScale(2, RoundingMode.CEILING).doubleValue();
        this.uniqueId = generateId();
    }

    public ShopItem(UUID uniqueId, ItemStack item) {
        this(uniqueId, item, 0);
    }

    public ShopItem(ItemStack item) {
        this(generateId(), item);
    }

    private static UUID generateId() {
        UUID id;
        do {
            id = UUID.randomUUID();
        } while (ids.contains(id));

        ids.add(id);

        return id;
    }

    public static ShopItem read(FriendlyByteBuf buffer) {
        ItemStack stack = buffer.readItem();
        UUID uuid = buffer.readUUID();
        double price = buffer.readDouble();

        return new ShopItem(uuid, stack, price);
    }

    public static DataResult<JsonElement> encode(ShopItem item, JsonElement prefix) {
        return CODEC.encode(item, JsonOps.INSTANCE, prefix);
    }

    public static DataResult<ShopItem> decode(JsonElement element) {
        return CODEC.parse(JsonOps.INSTANCE, element);
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(item);
        buffer.writeUUID(uniqueId);
        buffer.writeDouble(price);
    }

    public static ShopItem deserialize(CompoundTag tag) {
        CompoundTag itemTag = tag.getCompound("Item");
        UUID uuid = tag.getUUID("uuid");
        double price = tag.getDouble("price");
        ItemStack stack = ItemStack.of(itemTag);

        return new ShopItem(uuid, stack, price);
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.put("Item", item.save(new CompoundTag()));
        tag.putUUID("uuid", uniqueId);
        tag.putDouble("price", price);

        return tag;
    }

    public ItemStack getItem() {
        return item.copy();
    }

    public void setItem(ItemStack item) {
        this.item = item.copy();
    }

    public boolean canBuyWith(double money) {
        return money >= price;
    }

    public boolean isEmpty() {
        return item.isEmpty();
    }

    public UUID getUUID() {
        return uniqueId;
    }
}
