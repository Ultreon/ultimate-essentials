package io.github.ultreon.mods.essentials.client.gui.screens.users;

import io.github.ultreon.mods.essentials.client.gui.screens.moderation.PermissionsScreen;
import io.github.ultreon.mods.essentials.user.LocalUser;
import io.github.ultreon.mods.essentials.user.Permissions;
import io.github.ultreon.mods.essentials.user.RemoteUser;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class GenericUserScreen extends UserScreen {
    public GenericUserScreen(RemoteUser user) {
        super(Component.translatable("gui.ultimate_essentials.doUser.title", user.getName()), user);

        addButtonRow(Component.literal("Ask TP there"), (button) -> {
            if (!button.visible) return;
            if (!button.active) return;
            onClose();
            user.requestTeleportTo();
        }, Component.literal("Ask TP here"), (button) -> {
            if (!button.visible) return;
            if (!button.active) return;
            onClose();
            user.requestTeleportHere();
        });
        if (LocalUser.get().isModerator() || LocalUser.get().hasPermission(Permissions.CHANGE_USER_PERMISSIONS)) {
            addButtonRow(Component.literal("Permissions"), (button) -> {
                if (!button.visible) return;
                if (!button.active) return;
                Minecraft.getInstance().setScreen(new PermissionsScreen(Minecraft.getInstance(), user));
            });
        }
    }

    public static void open(@NotNull RemoteUser user) {
        Minecraft.getInstance().setScreen(new GenericUserScreen(user));
    }
}
