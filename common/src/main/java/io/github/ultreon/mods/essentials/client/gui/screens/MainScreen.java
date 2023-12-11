package io.github.ultreon.mods.essentials.client.gui.screens;

import com.ultreon.mods.lib.client.gui.screen.TitleStyle;
import io.github.ultreon.mods.essentials.client.gui.screens.moderation.ServerModerationScreen;
import io.github.ultreon.mods.essentials.client.gui.screens.teleportation.TeleportationScreen;
import io.github.ultreon.mods.essentials.client.gui.screens.users.OtherUsersScreen;
import io.github.ultreon.mods.essentials.client.gui.screens.users.SelfScreen;
import io.github.ultreon.mods.essentials.user.LocalUser;
import net.minecraft.network.chat.Component;

public class MainScreen extends UEssentialsMenuScreen {
    public MainScreen() {
        super(new Properties()
                .titleText("Main Screen")
                .titleStyle(TitleStyle.DETACHED));

        addUserRow(LocalUser.get());
        addButtonRow(Component.literal("Teleportation"), btn -> new TeleportationScreen().open());
        addButtonRow(Component.literal("Others"), btn -> new OtherUsersScreen().open(), Component.literal("You"), btn -> new SelfScreen().open());
        if (LocalUser.get().isModerator()) {
            addButtonRow(Component.literal("Server Moderation"), btn -> new ServerModerationScreen().open());
        }
        addButtonRow(Component.literal("Shop"), btn -> new ShopScreen(LocalUser.get()).open());
        addButtonRow(Component.literal("Online Banking"), btn -> new OnlineBankingScreen().open());
    }
}
