package io.github.ultreon.mods.essentials.user;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.chars.CharList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * <def>NOTE: Permissions is not compatible for other mods to use. This should be modified first to allow external use.</def>
 */
public record Permission(@NotNull String id, boolean def) {
    public static final int LENGTH_LIMIT = 64;
    public static final Codec<Permission> CODEC = Codec.STRING.xmap(Permission::new, Permission::id);

    public Permission(@NotNull String id, boolean def) {
        if (id.length() > 64) {
            throw new IndexOutOfBoundsException("Length is above limit of " + LENGTH_LIMIT);
        }

        if (!id.equals("*")) {
            if (id.startsWith(".")) {
                throw new IllegalArgumentException("Permission id can't start with a dot.");
            }
            if (id.endsWith(".")) {
                throw new IllegalArgumentException("Permission id can't end with a dot.");
            }

            CharList symbols = new CharArrayList("abcdefghijklmnopqrstuvwxyz_".toCharArray());

            int i = 0;
            boolean lastCharWasDot = false;
            for (char c : id.toCharArray()) {
                if (i == 0) {
                    if (!symbols.contains(c)) {
                        throw new IllegalArgumentException("Expected a letter, got '" + c + "' at index " + i);
                    }
                } else if (lastCharWasDot) {
                    if (c == '.') {
                        throw new IllegalArgumentException("Multiple dots after each other isn't allowed.");
                    }
                    lastCharWasDot = false;
                } else if (c != '.' && !symbols.contains(c)) {
                    throw new IllegalArgumentException("Expected a letter or a dot, got '" + c + "' at index " + i);
                } else if (c == '.') {
                    lastCharWasDot = true;
                }
                i++;
            }
        }
        this.id = id;
        this.def = def;
    }

    public Permission(String id) {
        this(id, false);
    }

    public String getTranslationId() {
        if (id.equals("*")) return "ultimate_essentials.permission";
        return "ultimate_essentials.permission." + id;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Permission that = (Permission) other;

        return id().equals(that.id());
    }

    public boolean isChildOf(Permission other) {
        if (other.id.equals("*")) {
            return true;
        }
        return this.id.startsWith(other.id + "."); // a.def.c starts with a.def. // child sw parent // this sw other
    }

    public boolean compatibleWith(Permission other) {
        if (other.id.equals("*")) {
            return true;
        }
        return this.id.startsWith(other.id + "."); // a.def.c starts with a.def. // child sw parent // this sw other
    }

    public boolean isParentOf(Permission other) {
        if (this.id.equals("*")) {
            return true;
        }
        return (other.id).startsWith(this.id + "."); // a.def.c starts with a.def. // child sw parent // other sw this
    }

    @Override
    public int hashCode() {
        return Objects.hash(id());
    }

    @Override
    public String toString() {
        return "`" + id + "`";
    }
}
