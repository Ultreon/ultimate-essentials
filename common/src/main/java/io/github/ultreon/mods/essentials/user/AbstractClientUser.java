package io.github.ultreon.mods.essentials.user;

import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.SetUserFlagPacket;
import io.github.ultreon.mods.essentials.network.cheats.BurnPlayerPacket;
import io.github.ultreon.mods.essentials.network.cheats.FlyModePacket;
import io.github.ultreon.mods.essentials.network.cheats.GodModePacket;
import io.github.ultreon.mods.essentials.network.moderation.InstaKillPacket;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractClientUser extends User {
    private PlayerInfo playerInfo;
    @Getter
    @Setter
    private boolean admin;
    @Setter
    private boolean moderator;

    public AbstractClientUser(UUID id) {
        super(id);
    }
    public static @Nullable RemoteUser remote(String name) {
        return RemoteUser.get(name);
    }

    public static @NotNull RemoteUser remote(UUID uuid) {
        return RemoteUser.get(uuid);
    }

    public static @NotNull RemoteUser remote(RemotePlayer player) {
        return RemoteUser.get(player);
    }

    public static ArrayList<AbstractClientUser> all() {
        ArrayList<AbstractClientUser> users = new ArrayList<>(RemoteUser.remotes().stream().filter(user -> user.uuid() != LocalUser.get().uuid()).toList());
        users.addAll(List.of(LocalUser.get()));
        return users;
    }

    public ResourceLocation getSkinLocation() {
        return getNetworkInfo().getSkinLocation();
    }

    @SuppressWarnings("ConstantConditions")
    protected PlayerInfo getNetworkInfo() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();

        if (this.playerInfo == null) {
            this.playerInfo = connection.getPlayerInfo(this.uuid());
        }

        return this.playerInfo;
    }

    private AbstractClientPlayer player() {
        if (this instanceof LocalUser user) {
            return user.player();
        } else if (this instanceof RemoteUser user) {
            return user.player();
        }

        throw new ClassCastException("Class " + getClass().getName() + " can't be cast to LocalUser or RemoteUser from the Minecraft Client API.");
    }

    public void burn(int seconds) {
        Networking.get().sendToServer(new BurnPlayerPacket(uuid(), seconds));
    }

    public void makeMod() {
        Networking.get().sendToServer(new SetUserFlagPacket(UserFlags.MODERATOR, true));
    }

    public void revokeMod() {
        Networking.get().sendToServer(new SetUserFlagPacket(UserFlags.MODERATOR, false));
    }

    public void instaKill() {
        Networking.get().sendToServer(new InstaKillPacket(uuid()));
    }

    public boolean hasGodMode() {
        AbstractClientPlayer player = player();
        assert player != null;
        return player.getAbilities().invulnerable;
    }

    public void setGodMode(boolean enabled) {
        Networking.get().sendToServer(new GodModePacket(this, enabled));
    }

    public boolean hasFlyMode() {
        AbstractClientPlayer player = player();
        assert player != null;
        return player.getAbilities().mayfly;
    }

    public void setFlyMode(boolean enabled) {
        Networking.get().sendToServer(new FlyModePacket(this, enabled));
    }

    @Override
    public boolean isModerator() {
        return moderator;
    }
}
