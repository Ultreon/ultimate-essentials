package io.github.ultreon.mods.essentials.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.util.homes.ServerHome;
import net.minecraft.server.level.ServerPlayer;

public class HomeEvent {
    public static final Event<Created> CREATED = EventFactory.createLoop();
    public static final Event<Set> SET = EventFactory.createLoop();
    public static final Event<Renamed> RENAMED = EventFactory.createLoop();
    public static final Event<Removed> REMOVED = EventFactory.createLoop();
    public static final Event<Teleporting> TELEPORTING = EventFactory.createEventResult();
    public static final Event<Teleported> TELEPORTED = EventFactory.createLoop();

    @FunctionalInterface
    public interface Created {
        void onCreated(ServerHome home, ServerUser owner);
    }

    @FunctionalInterface
    public interface Set {
        void onSet(ServerHome home, ServerUser owner);
    }

    @FunctionalInterface
    public interface Renamed {
        void onRenamed(ServerHome home, String oldName);
    }

    @FunctionalInterface
    public interface Removed {
        void onRemoved(ServerHome home, ServerUser owner);
    }

    @FunctionalInterface
    public interface Teleporting {
        EventResult onTeleporting(ServerPlayer player, ServerHome home);
    }

    @FunctionalInterface
    public interface Teleported {
        void onTeleported(ServerPlayer player, ServerHome home);
    }
}
