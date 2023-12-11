package io.github.ultreon.mods.essentials.client.gui.screens.moderation;

import com.ultreon.mods.lib.client.gui.screen.TitleStyle;
import io.github.ultreon.mods.essentials.client.gui.screens.UEssentialsMenuScreen;
import net.minecraft.client.Minecraft;

public class TimeSetScreen extends UEssentialsMenuScreen {
    public TimeSetScreen() {
        super(new Properties().titleText("Set time").titleStyle(TitleStyle.DETACHED));
    }

    public void open() {
        Minecraft.getInstance().setScreen(new TimeSetScreen());
    }
}
