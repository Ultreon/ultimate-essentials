package io.github.ultreon.mods.essentials.client.gui.screens.users;

import com.ultreon.mods.lib.client.gui.screen.TitleStyle;
import io.github.ultreon.mods.essentials.client.UEssentialsClient;
import io.github.ultreon.mods.essentials.client.gui.screens.UEssentialsMenuScreen;
import io.github.ultreon.mods.essentials.user.AbstractClientUser;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class UserScreen extends UEssentialsMenuScreen {
    public static final ResourceLocation background = UEssentialsClient.res("textures/gui/user_info.png");
    private final AbstractClientUser user;

    public UserScreen(Component title, AbstractClientUser user) {
        super(new Properties().title(title).titleStyle(TitleStyle.DETACHED));
        this.user = user;

        addUserRow(this.user);
    }

    private int topExtension() {
        return 23;
    }

    private int windowHeight() {
        return height() - topExtension();
    }

    private int top2() {
        return top() + 30;
    }

    private int topHeight() {
        return 53;
    }

    private int rowHeight() {
        return 22;
    }

    private int rowWidth() {
        return width();
    }

    private int rowU() {
        return 0;
    }

    private int rowV() {
        return 53;
    }

    private int bottomRowHeight() {
        return 27;
    }

    private int bottomRowWidth() {
        return rowWidth();
    }

    private int bottomRowU() {
        return 0;
    }

    private int bottomRowV() {
        return 75;
    }

    private int bottomHeight() {
        return 5;
    }

    private int bottomWidth() {
        return width();
    }

    private int bottomU() {
        return 0;
    }

    private int bottomV() {
        return 97;
    }

    public void tick() {
        super.tick();
    }

    @Override
    protected void init() {
        super.init();
    }

    public void renderBackground(@NotNull GuiGraphics gfx) {
        int i = this.left();
        super.renderBackground(gfx);
    }

    public AbstractClientUser user() {
        return user;
    }
}
