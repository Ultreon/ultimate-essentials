package io.github.ultreon.mods.essentials.client.gui.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ButtonMenuItem extends AbstractButtonMenuItem {
    private OnPress onPress;

    public ButtonMenuItem(ContextMenu menu, Component message) {
        super(menu, message);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        // TODO: Wtf do I need to put here.
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput narrator) {
        this.defaultButtonNarrationText(narrator);
    }

    @Override
    protected void onPress() {
        this.onPress.run(this);
    }

    @FunctionalInterface
    public interface OnPress {
        void run(ButtonMenuItem menuItem);
    }
}
