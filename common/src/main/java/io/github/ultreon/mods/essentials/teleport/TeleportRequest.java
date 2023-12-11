package io.github.ultreon.mods.essentials.teleport;

import io.github.ultreon.mods.essentials.util.Sendable;
import io.github.ultreon.mods.essentials.util.UniqueObject;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Objects;
import java.util.UUID;

@Getter
public class TeleportRequest implements UuidTeleport, Sendable, Cloneable {
    private final TpDirection to;
    private final UUID sender;
    private final UUID receiver;

    public TeleportRequest(TpDirection to, UUID sender, UUID receiver) {
        this.to = to;
        this.sender = sender;
        this.receiver = receiver;
    }

    public TeleportRequest(TpDirection to, UniqueObject sender, UniqueObject receiver) {
        this.to = to;
        this.sender = sender.uuid();
        this.receiver = receiver.uuid();
    }

    public static TeleportRequest read(FriendlyByteBuf buffer) {
        TpDirection to = buffer.readEnum(TpDirection.class);
        UUID requester = buffer.readUUID();
        UUID recipient = buffer.readUUID();
        return new TeleportRequest(to, requester, recipient);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeEnum(to);
        buffer.writeUUID(sender);
        buffer.writeUUID(receiver);
    }

    @Override
    public UUID destination() {
        return switch (to) {
            case RECEIVER -> receiver;
            case SENDER -> sender;
        };
    }

    @Override
    public UUID origin() {
        return switch (to) {
            case RECEIVER -> sender;
            case SENDER -> receiver;
        };
    }

    public enum TpDirection {
        RECEIVER, SENDER
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeleportRequest request = (TeleportRequest) o;
        return to == request.to && Objects.equals(sender, request.sender) && Objects.equals(receiver, request.receiver);
    }

    @Deprecated
    @Override
    public int hashCode() {
        return Objects.hash(to, sender, receiver);
    }

    @Override
    public String toString() {
        return "TeleportRequest{" +
                "to=" + to +
                ", requester=" + sender +
                ", recipient=" + receiver +
                '}';
    }

    @Override
    public TeleportRequest clone() {
        try {
            return (TeleportRequest) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError("Should be cloneable.", e);
        }
    }

    public TeleportRequest copy() {
        return new TeleportRequest(to, sender, receiver);
    }
}
