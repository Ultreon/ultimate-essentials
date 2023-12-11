package io.github.ultreon.mods.essentials.exceptions;

public class IllegalSizeException extends RuntimeException {
    public IllegalSizeException() {
    }

    public IllegalSizeException(String message) {
        super(message);
    }

    public IllegalSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalSizeException(Throwable cause) {
        super(cause);
    }
}
