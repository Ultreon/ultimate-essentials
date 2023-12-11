package io.github.ultreon.mods.essentials.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Paginated list, page 0 is the first page.
 * Page size 1 == 1 object, 5 == 5 objects.
 *
 * @param <T> the type of items in the list.
 * @author Qboi123
 */
@SuppressWarnings("unused")
public class PagedList<T> extends ArrayList<T> {
    private final int pageSize;

    public PagedList(int pageSize, int initialCapacity) {
        super(initialCapacity);
        this.pageSize = pageSize;
    }

    public PagedList(int pageSize) {
        super();
        this.pageSize = pageSize;
    }

    public PagedList(int pageSize, @NotNull Collection<? extends T> collection) {
        super(collection);
        this.pageSize = pageSize;
    }

    public PagedList(@NotNull PagedList<? extends T> pagedList) {
        super(pagedList);
        this.pageSize = pagedList.pageSize;
    }

    @Deprecated
    @Override
    public T get(int index) {
        return super.get(index);
    }

    public List<T> getFullPage(int page) {
        int a = page * pageSize;
        int b = a + pageSize;
        int c = size();

        return super.subList(Math.min(a, c), Math.min(b, c));
    }

    /**
     * Get the object at the page and index.
     *
     * @param page  page number, 0 is first.
     * @param index index at that page.
     * @return the object at the page and index.
     * @see ArrayList#get(int)
     */
    public T get(int page, int index) {
        if (index >= pageSize) {
            return null;
        }

        return super.get((page * pageSize) + index);
    }

    public int getPages() {
        return (int) Math.ceil((float) size() / pageSize);
    }
}
