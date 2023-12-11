package io.github.ultreon.mods.essentials.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public abstract class BaseWidget extends AbstractWidget {
    protected final Minecraft minecraft;
    protected final Font font;

    public BaseWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);

        this.minecraft = Minecraft.getInstance();
        this.font = minecraft.font;
    }
}
