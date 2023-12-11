package io.github.ultreon.mods.essentials.util;

import java.util.function.Function;

public interface Validation<T> extends Function<T, Boolean> {
    boolean test(T t);

    @Override
    default Boolean apply(T t) {
        return test(t);
    }

    Validation<?> INITIAL = (obj) -> true;

    @SuppressWarnings("unchecked")
    static <T> Validation<T> initial() {
        return (Validation<T>) INITIAL;
    }

    default Validation<T> then(Validation<T> sup) {
        return (obj) -> test(obj) && sup.test(obj);
    }
}
