package io.github.ultreon.mods.essentials.util.common;

public interface Mirrorable<T extends Mirrorable<T>> {
    T mirror();
}
