package io.github.ultreon.mods.essentials.exceptions;

public class IllegalDistException extends IllegalArgumentException {
    public IllegalDistException() {
        super();
    }

    public IllegalDistException(String message) {
        super(message);
    }

    public IllegalDistException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDistException(Throwable cause) {
        super(cause);
    }
}
