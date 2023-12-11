package io.github.ultreon.mods.essentials.client.gui.screens;

import com.ultreon.mods.lib.client.gui.widget.BaseButton;
import io.github.ultreon.mods.essentials.client.gui.screens.users.UserListWidget;
import io.github.ultreon.mods.essentials.client.gui.widget.UserWidget;
import io.github.ultreon.mods.essentials.user.AbstractClientUser;
import net.minecraft.client.Minecraft;

import java.util.List;

@SuppressWarnings({"UnusedReturnValue", "unused", "SameParameterValue"})
public abstract class UEssentialsMenuScreen extends com.ultreon.mods.lib.client.gui.screen.GenericMenuScreen {
    protected UEssentialsMenuScreen(Properties properties) {
        super(properties.back(Minecraft.getInstance().screen));
    }

    @Override
    public void resize(Minecraft minecraft, int i, int j) {
        super.resize(minecraft, i, j);
    }

    @SuppressWarnings("unused")
    public UEssentialsMenuScreen addUserRow(AbstractClientUser user) {
        UserWidget userWidget = new UserWidget(user, this.getTheme());
        userWidget.setWidth(width() - 5 - 5);
        this.addRenderableWidget(userWidget, 26, width() - 5 - 5, 24, 5, 0, 4, 0, 226);
        addRenderableWidget(userWidget);
        return this;
    }

    @SuppressWarnings("unused")
    public UserListWidget addUserListRow(int count, List<AbstractClientUser> users) {
        return addUserListRow(count, false, users);
    }

    @SuppressWarnings("unused")
    public UserListWidget addUserListRow(List<AbstractClientUser> users) {
        return addUserListRow(false, users);
    }

    @SuppressWarnings("unused")
    public UserListWidget addUserListRow(boolean hasSearch, List<AbstractClientUser> users) {
        return addUserListRow(3, hasSearch, users);
    }

    @SuppressWarnings("unused")
    public UserListWidget addUserListRow(int count, boolean hasSearch, List<AbstractClientUser> users) {
        UserListWidget widget = new UserListWidget(this, 0, 0, width() - 6 - 7, count, hasSearch, title, users);
        widget.setWidth(width() - 6 - 6);
        this.addRenderableWidget(widget, 26, width() - 5 - 5, 24, 5, 0, 4, 0, 226);
        addRenderableWidget(widget);
        return widget;
    }
}
