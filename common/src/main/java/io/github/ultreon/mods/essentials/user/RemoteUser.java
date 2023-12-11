package io.github.ultreon.mods.essentials.user;

import com.mojang.authlib.GameProfile;
import io.github.ultreon.mods.essentials.client.gui.screens.message.ErrorScreen;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.cheats.FullFeedPlayerPacket;
import io.github.ultreon.mods.essentials.network.cheats.FullHealPlayerPacket;
import io.github.ultreon.mods.essentials.network.economy.AddMoneyPacket;
import io.github.ultreon.mods.essentials.network.economy.ReduceMoneyPacket;
import io.github.ultreon.mods.essentials.network.economy.SetBalancePacket;
import io.github.ultreon.mods.essentials.network.moderation.BanPlayerPacket;
import io.github.ultreon.mods.essentials.network.moderation.DeOpPlayerPacket;
import io.github.ultreon.mods.essentials.network.moderation.KickPlayerPacket;
import io.github.ultreon.mods.essentials.network.moderation.OpPlayerPacket;
import io.github.ultreon.mods.essentials.network.permission.SetUserPermissionPacket;
import io.github.ultreon.mods.essentials.teleport.ClientTeleportManager;
import io.github.ultreon.mods.essentials.teleport.Teleportable;
import io.github.ultreon.mods.essentials.teleport.TeleportableDestination;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RemoteUser extends AbstractClientUser implements TeleportableDestination, Teleportable, io.github.ultreon.mods.essentials.teleport.TeleportRequestable, io.github.ultreon.mods.essentials.util.Bannable {
    private static final Map<UUID, RemoteUser> users = new HashMap<>();
    private final Set<Permission> permissions = new HashSet<>();
    private final Set<ClientRole> roles = new HashSet<>();
    private double balance;

    public RemoteUser(UUID playerUUID) {
        super(playerUUID);
    }

    @NotNull
    public static List<RemoteUser> remotes() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            List<RemoteUser> collect = connection.getOnlinePlayers()
                    .stream()
                    .filter(playerInfo -> {
                        LocalPlayer player = Minecraft.getInstance().player;
                        if (player == null) {
                            return false;
                        }
                        UUID id = playerInfo.getProfile().getId();
                        if (id == null) return false;
                        return id != player.getUUID();
                    })
                    .map(RemoteUser::getOrCreate)
                    .filter(RemoteUser::isValid)
                    .toList();
            ArrayList<RemoteUser> objects = new ArrayList<>();
            objects.addAll(collect);
            objects.addAll(collect);
            objects.addAll(collect);
            objects.addAll(collect);
            objects.addAll(collect);
            objects.addAll(collect);
            objects.addAll(collect);
            return objects;
        }
        return new ArrayList<>();
    }

    private boolean isValid() {
        return player() != null;
    }

    @NotNull
    public static RemoteUser get(UUID user) {
        return getOrCreate(user);
    }

    @NotNull
    public static RemoteUser get(RemotePlayer player) {
        return getOrCreate(player.getUUID());
    }

    @Nullable
    public static RemoteUser get(String name) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection == null) {
            return null;
        }

        PlayerInfo playerInfo = connection.getPlayerInfo(name);
        if (playerInfo == null) {
            return null;
        }

        return getOrCreate(playerInfo.getProfile().getId());
    }

    @NotNull
    private static RemoteUser getOrCreate(UUID id) {
        return users.computeIfAbsent(id, RemoteUser::new);
    }

    @NotNull
    private static RemoteUser getOrCreate(PlayerInfo playerInfo) {
        return getOrCreate(playerInfo.getProfile().getId());
    }

    public boolean isOnline() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            return level.getPlayerByUUID(uuid()) != null;
        }
        return false;
    }

    public boolean isOffline() {
        return !isOnline();
    }

    @Nullable
    @Override
    public GameProfile getProfile() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            PlayerInfo playerInfo = connection.getPlayerInfo(id);
            if (playerInfo != null) {
                return playerInfo.getProfile();
            }
        }

        return null;
    }

    @Override
    public boolean hasPermission(Permission perm) {
        if (LocalUser.get().isRoot()) {
            return true;
        }

        if (permissions.stream().anyMatch(permission -> permission.isChildOf(Permissions.MASTER) || permission.equals(Permissions.MASTER)) ||
                permissions.stream().anyMatch(permission -> permission.isChildOf(perm) || permission.equals(perm))) {
            return true;
        }

        for (ClientRole role : roles) {
            if (role.hasPermission(perm)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasExactPermission(Permission permission) {
        if (LocalUser.get().isRoot()) {
            return true;
        }

        return permissions.contains(permission);
    }

    @Override
    public boolean hasOwnPermission(Permission perm) {
        if (LocalUser.get().isRoot()) {
            return true;
        }

        return permissions.stream().anyMatch(permission -> permission.isChildOf(Permissions.MASTER) || permission.equals(Permissions.MASTER)) ||
                permissions.stream().anyMatch(permission -> permission.isChildOf(perm) || permission.equals(perm));
    }

    @Override
    public boolean hasRolePermission(Permission perm) {
        if (LocalUser.get().isRoot()) {
            return true;
        }

        for (ClientRole role : roles) {
            if (role.hasPermission(perm)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void addPermission(Permission perm) {
        Networking.get().sendToServer(new SetUserPermissionPacket(this, perm, true));
    }

    @Override
    public void removePermission(Permission perm) {
        Networking.get().sendToServer(new SetUserPermissionPacket(this, perm, false));
    }

    /**
     * Computer says no
     *
     * @return no
     */
    @Override
    protected boolean isRoot() {
        return false;
    }

    @NotNull
    @Override
    public Set<Permission> getEnabledPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    public PlayerInfo getNetworkInfo() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            return connection.getPlayerInfo(id);
        }

        return null;
    }

    @Override
    public void requestTeleportHere() {
        ClientTeleportManager.get().requestTeleportFrom(id);
    }

    @Override
    public void requestTeleportTo() {
        ClientTeleportManager.get().requestTeleportTo(id);
    }

    @Override
    public void teleportThere() {
        ClientTeleportManager.get().teleportMeTo(id);
    }

    @Override
    public void teleportHere() {
        ClientTeleportManager.get().teleportToMe(id);
    }

    @Override
    public void ban(@Nullable String reason) {
        Networking.get().sendToServer(new BanPlayerPacket(id, reason));
    }

    @Override
    public void kick(@Nullable String reason) {
        Networking.get().sendToServer(new KickPlayerPacket(id, reason));
    }

    public void fullFeed() {
        Networking.get().sendToServer(new FullFeedPlayerPacket(id));
    }

    @Override
    public void showError(String title, String description) {
        showError(Component.literal(title), Component.literal(description));
    }

    @Override
    public void showError(Component title, Component description) {
        Minecraft.getInstance().setScreen(new ErrorScreen(title, description));
    }

    public void fullHeal() {
        Networking.get().sendToServer(new FullHealPlayerPacket(id));
    }

    public void op() {
        Networking.get().sendToServer(new OpPlayerPacket(id));
    }

    public void deop() {
        Networking.get().sendToServer(new DeOpPlayerPacket(id));
    }

    public void handlePacket(SetUserPermissionPacket setUserPermissionPacket) {
        Permission perm = new Permission(setUserPermissionPacket.getPermission());
        if (setUserPermissionPacket.isEnable()) {
            permissions.add(perm);
        } else {
            permissions.removeIf(permission -> permission.equals(perm));
        }
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void setBalance(double amount) {
        Networking.get().sendToServer(new SetBalancePacket(balance));
    }

    public void reduceMoney(double amount) {
        Networking.get().sendToServer(new ReduceMoneyPacket(id, amount));
    }

    @Override
    public void addMoney(double amount) {
        Networking.get().sendToServer(new AddMoneyPacket(id, amount));
    }

    public void handlePacket(SetBalancePacket setBalancePacket) {
        balance = setBalancePacket.getAmount();
    }

    @Nullable
    public RemotePlayer player() {
        ClientLevel level = Minecraft.getInstance().level;

        if (level == null) {
            return null;
        }

        Player player = Minecraft.getInstance().level.getPlayerByUUID(uuid());
        if (player instanceof RemotePlayer remotePlayer) {
            return remotePlayer;
        }

        return null;
    }
}
