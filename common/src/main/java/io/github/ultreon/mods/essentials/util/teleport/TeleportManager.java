package io.github.ultreon.mods.essentials.util.teleport;

import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.teleport.TpRequestedFromPacket;
import io.github.ultreon.mods.essentials.network.teleport.TpRequestedToPacket;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.util.common.BaseLocation;
import lombok.Setter;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.*;

@Deprecated
public final class TeleportManager extends BaseTeleportManager {
    private static final Map<UUID, TeleportManager> instance = new HashMap<>();
    private static final Map<UUID, Set<TeleportRequest>> requesterEntries = new HashMap<>();
    private static final Map<UUID, Set<TeleportRequest>> recipientEntries = new HashMap<>();
    @Setter
    private BaseLocation backLocation = null;

    public TeleportManager(ServerUser user) {
        this(user.uuid());
    }

    public static TeleportManager get(ServerPlayer player) {
        return instance.computeIfAbsent(player.getUUID(), TeleportManager::new);
    }

    public static TeleportManager get(UUID playerUUID) {
        return instance.computeIfAbsent(playerUUID, TeleportManager::new);
    }

    public static void canAcceptAll(UUID playerUUID) {
        get(playerUUID);
    }

    private final UUID sourceUUID;

    public TeleportManager(UUID playerUUID) {
        this.sourceUUID = playerUUID;
    }

    @Override
    public Player getPlayer() {
        return getServerPlayer();
    }

    public ServerPlayer getServerPlayer() {
        return UEssentials.server().getPlayerList().getPlayer(sourceUUID);
    }

    public void accept(ServerPlayer player) {
        accept(player.getUUID());
    }

    public void accept(UUID requesterUUID) {
        UEssentials.LOGGER.debug(requesterUUID);
        UEssentials.LOGGER.debug(recipientEntries);
        List<TeleportRequest> teleportRequests = new ArrayList<>(recipientEntries.get(this.sourceUUID));
        for (TeleportRequest request : teleportRequests) {
            UEssentials.LOGGER.debug(request);
            if (request.getSender().equals(requesterUUID)) {
                UEssentials.LOGGER.debug("ACCEPTING!!!");
                accept0(request);
            }
        }
    }

    private void accept0(TeleportRequest request) {
        // Remove request.
        remove(request);

        // Teleport
        teleport(request.origin(), request.destination());
    }

    public void teleportTo(UUID destinationUUID) {
        teleport(sourceUUID, destinationUUID);
    }

    public void teleportHere(UUID originUUID) {
        teleport(originUUID, sourceUUID);
    }

    private void teleport(UUID originUUID, UUID destinationUUID) {
        // Get origin & destination players, the teleport the origin player to the location of the destination player.
        ServerPlayer origin = UEssentials.player(originUUID);
        ServerPlayer destination = UEssentials.player(destinationUUID);

        UEssentials.LOGGER.debug(origin.getGameProfile().getName());
        UEssentials.LOGGER.debug(destination.getGameProfile().getName());

        origin.teleportTo(destination.serverLevel(), destination.getX(), destination.getY(), destination.getZ(), destination.getYRot(), destination.getXRot());
    }

    public void deny(UUID requesterUUID) {
        UEssentials.LOGGER.debug(requesterUUID);
        UEssentials.LOGGER.debug(recipientEntries);
        List<TeleportRequest> teleportRequests = new ArrayList<>(recipientEntries.get(this.sourceUUID));
        for (TeleportRequest request : teleportRequests) {
            UEssentials.LOGGER.debug(request);
            if (request.getSender().equals(requesterUUID)) {
                UEssentials.LOGGER.debug("DENYING");
                deny0(request);
            }
        }
    }

    private void deny0(TeleportRequest request) {
        // Remove request.
        remove(request);
    }

    /**
     * Remove a teleport request.
     *
     * @param request the request to remove.
     */
    private void remove(TeleportRequest request) {
        // Remove request entries.
        UEssentials.LOGGER.debug(requesterEntries.computeIfAbsent(request.getSender(), uuid -> new HashSet<>()).remove(request));
        UEssentials.LOGGER.debug(recipientEntries.computeIfAbsent(request.getReceiver(), uuid -> new HashSet<>()).remove(request));
    }

    public void acceptAll() {
        boolean acceptorHasOriginRequest = false;
        boolean acceptorHasOverflow = false;

        TeleportRequest toRequester = null;
        List<TeleportRequest> toRecipient = new ArrayList<>();

        for (TeleportRequest request : TeleportManager.recipientEntries.get(sourceUUID)) {
            if (request.getTo() == TeleportRequest.TpDirection.REQUESTER) {
                if (acceptorHasOriginRequest) {
                    toRequester = null;
                    acceptorHasOverflow = true;
                } else {
                    toRequester = request;
                    acceptorHasOriginRequest = true;
                }
            } else if (request.getTo() == TeleportRequest.TpDirection.RECIPIENT) {
                toRecipient.add(request);
            }
        }

        if (acceptorHasOverflow) {
            getServerPlayer().sendSystemMessage(Component.literal("You can't teleport to multiple destinations."));
            return;
        }

        for (TeleportRequest request : toRecipient) {
            accept0(request);
        }

        if (toRequester != null) accept0(toRequester);
    }

    public void requestTeleportTo(ServerUser user) {
        this.requestTeleportTo(user.uuid());
    }

    @Override
    public void requestTeleportTo(UUID recipient) {
        ServerPlayer to = UEssentials.player(sourceUUID);
        if (to == null) {
            return;
        }

        requesterEntries.computeIfAbsent(sourceUUID, uuid1 -> new HashSet<>())
                .add(new TeleportRequest(TeleportRequest.TpDirection.RECIPIENT, sourceUUID, recipient));

        recipientEntries.computeIfAbsent(recipient, uuid1 -> new HashSet<>())
                .add(new TeleportRequest(TeleportRequest.TpDirection.RECIPIENT, sourceUUID, recipient));

        ServerUser source = ServerUser.get(to.getUUID());
        ServerUser target = ServerUser.get(recipient);
        Networking.get().sendToClient(new TpRequestedFromPacket(source.uuid()), target.player());
//        target.handleTeleportRequestFrom(source);
    }

    public void requestTeleportFrom(ServerUser user) {
        this.requestTeleportFrom(user.uuid());
    }

    @Override
    public void requestTeleportFrom(UUID recipient) {
        ServerPlayer from = UEssentials.server().getPlayerList().getPlayer(sourceUUID);
        if (from == null) {
            return;
        }

        requesterEntries.computeIfAbsent(sourceUUID, uuid1 -> new HashSet<>())
                .add(new TeleportRequest(TeleportRequest.TpDirection.REQUESTER, sourceUUID, recipient));

        recipientEntries.computeIfAbsent(recipient, uuid1 -> new HashSet<>())
                .add(new TeleportRequest(TeleportRequest.TpDirection.REQUESTER, sourceUUID, recipient));

        ServerUser source = ServerUser.get(from.getUUID());
        ServerUser target = ServerUser.get(recipient);
        Networking.get().sendToClient(new TpRequestedToPacket(source.uuid()), target.player());
//        target.handleTeleportRequestTo(source);
    }

    @Override
    public void requestTeleportEntity(UUID uuid) {
        // Nope
    }

    public Set<TeleportRequest> getRequests() {
        return Collections.unmodifiableSet(recipientEntries.getOrDefault(sourceUUID, new HashSet<>()));
    }

    @Override
    public void back() {
        getServerPlayer().sendSystemMessage(Component.translatable("message.ultimate_essentials.teleport.noPrevious"));
        if (backLocation != null) {
            teleport(new PositionTeleportImpl(sourceUUID, backLocation));
        }
    }

    private void teleport(PositionTeleport teleport) {
        ServerPlayer origin = UEssentials.player(teleport.origin());
        double x = teleport.getDestX();
        double y = teleport.getDestY();
        double z = teleport.getDestZ();
        ResourceKey<Level> worldKey = teleport.getDestLevel();
        ServerLevel level = UEssentials.server().getLevel(worldKey);
        if (level != null) {
            origin.randomTeleport(x, y, z, false);
            origin.setLevel(level);
        }
    }

    private void teleport(UUIDTeleport teleport) {
        ServerPlayer orig = UEssentials.player(teleport.origin());
        ServerPlayer dest = UEssentials.player(teleport.destination());
        orig.randomTeleport(dest.getX(), dest.getY(), dest.getZ(), false);
        orig.setLevel(dest.serverLevel());
    }
}
