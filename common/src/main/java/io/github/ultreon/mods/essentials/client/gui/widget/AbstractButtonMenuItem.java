package io.github.ultreon.mods.essentials.client.gui.widget;

import net.minecraft.network.chat.Component;

public abstract class AbstractButtonMenuItem extends MenuItem {
    public AbstractButtonMenuItem(ContextMenu menu, Component message) {
        super(20, menu, message);
    }

    protected abstract void onPress();

    public void onClick(double p_93371_, double p_93372_) {
        this.onPress();
    }
}
