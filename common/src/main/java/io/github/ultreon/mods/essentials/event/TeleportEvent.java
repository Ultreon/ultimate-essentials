package io.github.ultreon.mods.essentials.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import io.github.ultreon.mods.essentials.teleport.TeleportRequest;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class TeleportEvent {
    public static final Event<Requested> REQUESTED = EventFactory.createEventResult();
    public static final Event<Response> ACCEPTED = EventFactory.createLoop();
    public static final Event<Response> DENIED = EventFactory.createLoop();
    public static final Event<Cancelled> CANCELLED = EventFactory.createLoop();

    @FunctionalInterface
    public interface Requested {
        EventResult onRequested(ServerPlayer player, UUID target);
    }

    @FunctionalInterface
    public interface Response {
        void onResponse(ServerPlayer responder, TeleportRequest request);
    }

    @FunctionalInterface
    public interface Cancelled {
        void onDenied(TeleportRequest request);
    }
}
