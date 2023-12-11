package io.github.ultreon.mods.essentials.util.teleport;

import io.github.ultreon.mods.essentials.util.common.Mirrorable;
import io.github.ultreon.mods.essentials.util.common.UniqueObject;
import lombok.Getter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;
import java.util.UUID;

@Deprecated
public class TeleportRequest implements UUIDTeleport, Cloneable, Mirrorable<TeleportRequest> {
    private final TpDirection tpDirection;
    @Getter
    private final UUID sender;
    @Getter
    private final UUID receiver;

    public TeleportRequest(TpDirection tpDirection, UUID sender, UUID receiver) {
        this.tpDirection = tpDirection;
        this.sender = sender;
        this.receiver = receiver;
    }

    public TeleportRequest(TpDirection tpDirection, UniqueObject sender, UniqueObject receiver) {
        this.tpDirection = tpDirection;
        this.sender = sender.uuid();
        this.receiver = receiver.uuid();
    }

    public TeleportRequest(TpDirection tpDirection, Entity sender, Player receiver) {
        this.tpDirection = tpDirection;
        this.sender = sender.getUUID();
        this.receiver = receiver.getUUID();
    }

    public TpDirection getTo() {
        return tpDirection;
    }

    @Override
    public UUID destination() {
        return switch (tpDirection) {
            case RECIPIENT -> receiver;
            case REQUESTER -> sender;
        };
    }

    @Override
    public UUID origin() {
        return switch (tpDirection) {
            case RECIPIENT -> sender;
            case REQUESTER -> receiver;
        };
    }

    @Override
    public TeleportRequest mirror() {
        return new TeleportRequest(tpDirection, receiver, sender);
    }

    public enum TpDirection {
        RECIPIENT, REQUESTER
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeleportRequest request = (TeleportRequest) o;
        return tpDirection == request.tpDirection && Objects.equals(sender, request.sender) && Objects.equals(receiver, request.receiver);
    }

    @Deprecated
    @Override
    public int hashCode() {
        return Objects.hash(tpDirection, sender, receiver);
    }

    @Override
    public String toString() {
        return "TeleportRequest{" +
                "to=" + tpDirection +
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
        return new TeleportRequest(tpDirection, sender, receiver);
    }
}
