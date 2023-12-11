package io.github.ultreon.mods.essentials.util;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum ErrorCode {
    OFFLINE(100),
    CONNECTION_CHANGE(101),
    SUCCESS(200),
    WARNINGS(201),
    REDIRECTING(300),
    REDIRECTING_TO_NEW(301),
    REDIRECTING_TO_OLD(302),
    REDIRECTING_BECAUSE_NO_ACCESS(303),
    REDIRECTING_BECAUSE_NOT_FOUND(304),
    DISCONNECTED(400),
    ILLEGAL_VALUE(401),
    ALREADY_EXISTS(402),
    ACCESS_DENIED(403),
    NOT_FOUND(404),
    CORRUPT(405),
    ACCESSED_FROM_OTHER_LOCATION(406),
    SERVER_ERROR(500),
    ILLEGAL_STATE(501),
    CRASHED(599),
    ;

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    @Nullable
    public static ErrorCode fromCode(int code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.code == code) {
                return errorCode;
            }
        }
        return null;
    }

    public Component getDisplayName() {
        return Component.translatable("ultimate_essentials.error_code." + name().toLowerCase(Locale.ROOT), code());
    }
}
