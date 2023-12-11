package io.github.ultreon.mods.essentials.user;

import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import dev.architectury.event.events.client.ClientPlayerEvent;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.client.UEssentialsClient;
import io.github.ultreon.mods.essentials.client.connect.ServerConnect;
import io.github.ultreon.mods.essentials.client.connect.ServerConnect.ConnectionState;
import io.github.ultreon.mods.essentials.client.gui.screens.message.ErrorScreen;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.economy.AddMoneyPacket;
import io.github.ultreon.mods.essentials.network.economy.ReduceMoneyPacket;
import io.github.ultreon.mods.essentials.network.economy.SetBalancePacket;
import io.github.ultreon.mods.essentials.network.permission.SetUserPermissionPacket;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class LocalUser extends AbstractClientUser {
    private static LocalUser instance;
    @Nullable
    private LocalUserData data;
    @Getter
    private LocalAccountInfo accountInfo;

    private LocalUser(UUID playerUUID) {
        super(playerUUID);

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(this::onJoin);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(this::onQuit);
    }

    public static LocalUser create(User user) {
        if (instance != null) {
            return instance;
        }
        return instance =  new LocalUser(UUIDTypeAdapter.fromString(user.getUuid()));
    }

    public Set<ClientRole> getRoles() {
        return Collections.unmodifiableSet(data().roles);
    }

    public static LocalUser get() {
        return UEssentialsClient.get().user();
    }

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

        if (data().permissions.stream().anyMatch(permission -> permission.isChildOf(Permissions.MASTER) || permission.equals(Permissions.MASTER)) ||
                data().permissions.stream().anyMatch(permission -> permission.isChildOf(perm) || permission.equals(perm))) {
            return true;
        }

        for (ClientRole role : data().roles) {
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

        return data().permissions.contains(permission);
    }

    @Override
    public boolean hasOwnPermission(Permission perm) {
        if (LocalUser.get().isRoot()) {
            return true;
        }

        return data().permissions.stream().anyMatch(permission -> permission.isChildOf(Permissions.MASTER) || permission.equals(Permissions.MASTER)) ||
                data().permissions.stream().anyMatch(permission -> permission.isChildOf(perm) || permission.equals(perm));
    }

    @Override
    public boolean hasRolePermission(Permission perm) {
        if (LocalUser.get().isRoot()) {
            return true;
        }

        for (ClientRole role : data().roles) {
            if (role.hasPermission(perm)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isRoot() {
        LocalPlayer player = player();
        return player != null || isDeveloperAccount();
    }

    private boolean isDeveloperAccount() {
        LocalPlayer player = player();
        return player != null && player.getName().getString().equals("Dev") && UEssentials.isDevMode();
    }

    @Override
    public Set<Permission> getEnabledPermissions() {
        return Collections.unmodifiableSet(data().permissions);
    }

    @Override
    public void showError(String title, String description) {
        showError(Component.literal(title), Component.literal(description));
    }

    @Override
    public void showError(Component title, Component description) {
        Minecraft.getInstance().setScreen(new ErrorScreen(title, description));
    }

    public void handlePacket(SetUserPermissionPacket setUserPermissionPacket) {
        Permission perm = new Permission(setUserPermissionPacket.getPermission());
        if (setUserPermissionPacket.isEnable()) {
            data().permissions.add(perm);
        } else {
            data().permissions.removeIf(permission -> permission.equals(perm));
        }
    }

    @Override
    public double getBalance() {
        return data().balance;
    }

    @Override
    public void setBalance(double amount) {
        Networking.get().sendToServer(new SetBalancePacket(amount));
    }

    public void reduceMoney(double amount) {
        Networking.get().sendToServer(new ReduceMoneyPacket(id, amount));
    }

    @Override
    public void addMoney(double amount) {
        Networking.get().sendToServer(new AddMoneyPacket(id, amount));
    }

    public void handlePacket(SetBalancePacket setBalancePacket) {
        data().balance = setBalancePacket.getAmount();
    }

    @NotNull
    private LocalUserData data() {
        return Objects.requireNonNull(data);
    }

    @Nullable
    public LocalPlayer player() {
        return Minecraft.getInstance().player;
    }


    public boolean isModerator() {
        return data().moderator || isRoot() || isAdmin();
    }

    public void setModerator(boolean set) {
        data().moderator = set;
    }

    public boolean isAdmin() {
        return data().admin || isRoot();
    }

    public void setAdmin(boolean set) {
        data().admin = set;
    }

    private void onJoin(LocalPlayer localPlayer) {
        data = new LocalUserData();
    }

    private void onQuit(LocalPlayer localPlayer) {
        this.data = null;
    }

    public boolean isConnected() {
        return player() != null && data != null;
    }

    public boolean isLoggedIn() {
        return ServerConnect.get().getState() == ConnectionState.CONNECTED;
    }

    public void login(LocalAccountInfo accountInfo) {
        if (!isLoggedIn()) {
            this.accountInfo = accountInfo;
            return;
        }

        throw new IllegalStateException("ERROR[000002]");
    }

    public boolean canExchangeExp(int amount) {
        LocalPlayer player = player();
        if (player == null) {
            return false;
        }

        return getTotalExpPoints(player) >= amount;
    }

    public boolean isValid() {
        return player() != null;
    }

    public int getExpLevel() {
        LocalPlayer player = player();
        return player == null ? -1 : Math.max(player.experienceLevel, 0);
    }

    public int getExpPoints() {
        LocalPlayer player = player();
        return player == null ? -1 : Math.max(Mth.floor(player.experienceProgress * player.getXpNeededForNextLevel()), 0);
    }

    public int getExpNeededForLevelUp() {
        LocalPlayer player = player();
        return player == null ? -1 : Math.max(player.getXpNeededForNextLevel(), 0);
    }

    public int getTotalExpPoints() {
        LocalPlayer player = player();
        return player == null ? -1 : getTotalExpPoints(player);
    }

    public float getExpProgress() {
        LocalPlayer player = player();
        return player == null ? -1 : player.experienceProgress;
    }
}
