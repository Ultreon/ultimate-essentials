package io.github.ultreon.mods.essentials.client.gui.screens.moderation;

import com.ultreon.mods.lib.client.gui.widget.Button;
import io.github.ultreon.mods.essentials.client.gui.screens.TextInsertScreen;
import io.github.ultreon.mods.essentials.client.gui.screens.users.UserScreen;
import io.github.ultreon.mods.essentials.user.RemoteUser;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class UserModerationScreen extends UserScreen {
    public UserModerationScreen(RemoteUser user) {
        super(Component.translatable("gui.ultimate_essentials.doUserMod.title", user.getName()), user);

        addButtonRow(Component.literal("TP to user"), (button) -> {
            if (!button.visible) return;
            if (!button.active) return;
            user.teleportThere();
        },Component.literal("TP user here"), (button) -> {
            if (!button.visible) return;
            if (!button.active) return;
            user.teleportHere();
        });
        addButtonRow(Component.literal("Ban"), this::ban, Component.literal("Kick"), this::kick);
        addButtonRow(Component.literal("Timeout"), this::timeout);
        addButtonRow(Component.literal("Make Op"), this::op, Component.literal("Revoke Op"), this::deop);
        addButtonRow(Component.literal("Insta-kill"), this::instaKill);
    }

    private void timeout() {
        RemoteUser user = (RemoteUser) user();
    }

    private void teleportToUser() {
        RemoteUser user = (RemoteUser) user();
        user.teleportThere();
    }

    private void teleportUserHere() {
        RemoteUser user = (RemoteUser) user();
        user.teleportHere();
    }

    private void kick() {
        RemoteUser user = (RemoteUser) user();
        TextInsertScreen.open("Enter the reason to kick.", "").done((scr, text) -> user.kick(text)).execute();
    }

    private void ban() {
        RemoteUser user = (RemoteUser) user();
        TextInsertScreen.open("Enter the reason to kick.", "").done((scr, text) -> user.ban(text)).execute();
    }

    private void op() {
        RemoteUser user = (RemoteUser) user();
        TextInsertScreen.open("Enter the reason to kick.", "").done((scr, text) -> user.op()).execute();
    }

    private void deop() {
        RemoteUser user = (RemoteUser) user();
        TextInsertScreen.open("Enter the reason to kick.", "").done((scr, text) -> user.deop()).execute();
    }

    private void toggleGodMode(Button button) {
        RemoteUser user = (RemoteUser) user();
        user.setGodMode(!user.hasGodMode());
        button.setMessage(Component.literal("God Mode: ").append(user.hasGodMode() ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF));
    }

    private void toggleFlyMode(Button button) {
        RemoteUser user = (RemoteUser) user();
        user.setFlyMode(!user.hasFlyMode());
        button.setMessage(Component.literal("Fly Mode: ").append(user.hasGodMode() ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF));
    }

    private void instaKill() {
        user().instaKill();
    }

    public static void open(RemoteUser user) {
        Minecraft.getInstance().setScreen(new UserModerationScreen(user));
    }
}
