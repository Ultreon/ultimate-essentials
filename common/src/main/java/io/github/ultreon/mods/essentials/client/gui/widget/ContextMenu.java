package io.github.ultreon.mods.essentials.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ultreon.mods.essentials.client.UEssentialsClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ContextMenu extends AbstractContainerWidget {
    private static final int BORDER_WIDTH = 5;
    private static final ResourceLocation WIDGETS = UEssentialsClient.res("textures/gui/widgets/context_menu");

    private List<MenuItem> entries;

    public ContextMenu(int x, int y, Component title) {
        super(x, y, BORDER_WIDTH * 2, BORDER_WIDTH * 2, title);
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput narrationOutput) {

    }

    @Override
    public void renderWidget(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float frameTime) {
        RenderSystem.setShaderTexture(0, WIDGETS);

        gfx.blit(WIDGETS, getX(), getY(), 5, 5, 0, 0, 5, 5, 16, 16);
        gfx.blit(WIDGETS, getX() + width - 5, getY(), 5, 5, 6, 0, 5, 5, 16, 16);
        gfx.blit(WIDGETS, getX(), getY() + height, 5, 5, 6, 0, 5, 5, 16, 16);

        super.render(gfx, mouseX, mouseY, frameTime);
    }

    public <T extends MenuItem> T add(T menuItem) {
        entries.add(menuItem);
        return menuItem;
    }

    void invalidateSize() {
        width = BORDER_WIDTH * 2 + entries.stream().mapToInt(MenuItem::getMinWidth).max().orElse(1);
        height = BORDER_WIDTH * 2 + entries.stream().mapToInt(MenuItem::getHeight).sum();
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return entries;
    }
}
