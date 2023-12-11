package io.github.ultreon.mods.essentials.client.gui.screens.users;

import com.mojang.datafixers.util.Pair;
import com.ultreon.mods.lib.client.gui.screen.TitleStyle;
import com.ultreon.mods.lib.client.gui.widget.BaseButton;
import io.github.ultreon.mods.essentials.client.gui.screens.UEssentialsMenuScreen;
import io.github.ultreon.mods.essentials.client.gui.screens.moderation.UserModerationScreen;
import io.github.ultreon.mods.essentials.client.gui.screens.users.cheats.UserCheatsScreen;
import io.github.ultreon.mods.essentials.user.AbstractClientUser;
import io.github.ultreon.mods.essentials.user.RemoteUser;
import net.minecraft.network.chat.Component;

public class OtherUsersScreen extends UEssentialsMenuScreen {
    private final UserListWidget userListWidget;
    private final BaseButton btn1;
    private final Pair<BaseButton, BaseButton> btn2and3;

    public OtherUsersScreen() {
        super(new Properties().titleText("User Selection").titleStyle(TitleStyle.DETACHED));

        userListWidget = addUserListRow(true, RemoteUser.remotes().stream().map(AbstractClientUser.class::cast).toList());
        btn1 = addButtonRow(Component.literal("Interaction"), this::openInteraction);
        btn2and3 = addButtonRow(Component.literal("Moderation"), this::openModeration, Component.literal("Cheats"), this::openCheats);
    }

    @Override
    public void onPreRender() {
        boolean b = userListWidget.getSelected() instanceof RemoteUser remoteUser && remoteUser.player() != null;
        btn1.active = b;
        btn2and3.getFirst().active = b && userListWidget.getSelected().isModerator();
        btn2and3.getSecond().active = b && userListWidget.getSelected().isModerator();
    }

    private void openInteraction() {
        AbstractClientUser selected = userListWidget.getSelected();
        if (selected == null) return;

        GenericUserScreen.open((RemoteUser) selected);
    }

    private void openModeration() {
        AbstractClientUser selected = userListWidget.getSelected();
        if (selected == null) return;

        UserModerationScreen.open((RemoteUser) selected);
    }

    private void openCheats() {
        AbstractClientUser selected = userListWidget.getSelected();
        if (selected == null) return;

        UserCheatsScreen.open(selected);
    }
}
