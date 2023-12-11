package io.github.ultreon.mods.essentials.client.gui.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class LabelItem extends MenuItem {
    public LabelItem(ContextMenu menu, Component message) {
        super(13, menu, message);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float frameTime) {
        gfx.drawString(this.font, getMessage(), getX(), (int) (getY() + 6 - font.lineHeight / 2f), 0xffffff);
        if (isHoveredOrFocused()) {
            int x1 = getX() + width;
            int y1 = getY() + height;
            gfx.fill(getX(), getY(), x1, getY(), 0xffffff);   // top
            gfx.fill(getX(), y1, x1, y1, 0xffffff); // bottom
            gfx.fill(getX(), getY(), getX(), y1, 0xffffff);   // left
            gfx.fill(x1, getY(), x1, y1, 0xffffff); // right
        }
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput narrator) {
        narrator.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.active) {
            if (this.isFocused()) {
                narrator.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.focused"));
            } else {
                narrator.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.hovered"));
            }
        }
    }
}
