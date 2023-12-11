package io.github.ultreon.mods.essentials.shop;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class ShopPage {
    public static final int ROWS = 9;
    public static final int COLUMNS = 9;
    public static final int PAGE_SIZE = ROWS * COLUMNS;

    private final NonNullList<ShopItem> items;

    public ShopPage(NonNullList<ShopItem> items) {
        if (items.size() != PAGE_SIZE) {
            throw new IndexOutOfBoundsException(((Integer) items.size()).toString());
        }

        this.items = items;
    }

    public ShopPage() {
        this.items = NonNullList.withSize(PAGE_SIZE, new ShopItem(ItemStack.EMPTY));
    }

    public List<ShopItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
