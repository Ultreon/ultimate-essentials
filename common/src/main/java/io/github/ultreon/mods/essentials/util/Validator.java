package io.github.ultreon.mods.essentials.util;

public class Validator<T> {
    private final Validation<T> ops = Validation.initial();

    public Validator<T> addValidation(Validation<T> ops) {
        this.ops.then(ops);
        return this;
    }

    public void isValid(String input) {

    }
}
