package io.github.ultreon.mods.essentials.client.gui.screens.teleportation;

import io.github.ultreon.mods.essentials.client.gui.screens.TextInsertScreen;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.homes.SetHomePacket;
import io.github.ultreon.mods.essentials.user.LocalUser;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public final class SetHome {
    public static void open() {
        TextInsertScreen.open(Component.translatable("gui.ultimate_essentials.setHome.title"), I18n.get("gui.ultimate_essentials.setHome.nameField")).done((scr, text) -> {
            if (!text.isBlank()) Networking.get().sendToServer(new SetHomePacket(text));
            else LocalUser.get().showError("Naming Error", "Expected to get a non-blank name for a home.");
        }).validate(s -> !s.isBlank()).execute();
    }
}
