package io.github.ultreon.mods.essentials.client.gui.widget;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.Component;

@Getter
public abstract class MenuItem extends BaseWidget {
    private final ContextMenu menu;
    private int minWidth;
    @Setter
    private int maxWidth;

    public MenuItem(int height, ContextMenu menu, Component message) {
        super(menu.getX(), menu.getY(), menu.getWidth(), height, message);
        this.menu = menu;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        menu.invalidateSize();
    }

}
