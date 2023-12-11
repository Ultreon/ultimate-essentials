package io.github.ultreon.mods.essentials.client.gui.widget;

import com.ultreon.mods.lib.client.gui.widget.BaseButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ImageButton extends BaseButton {
    protected final ResourceLocation texture;
    protected final int texU;
    protected final int texV;
    protected final int hoverOffsetV;
    protected final int texWidth;
    protected final int texHeight;

    public ImageButton(int x, int y, int width, int height, int u, int v, ResourceLocation texture, CommandCallback onPress) {
        this(x, y, width, height, u, v, height, texture, 256, 256, onPress);
    }

    public ImageButton(int x, int y, int width, int height, int u, int v, int hoverOffsetV, ResourceLocation texture, CommandCallback onPress) {
        this(x, y, width, height, u, v, hoverOffsetV, texture, 256, 256, onPress);
    }

    public ImageButton(int x, int y, int width, int height, int u, int v, int hoverOffsetV, ResourceLocation texture, int texWidth, int texHeight, CommandCallback onPress) {
        this(x, y, width, height, u, v, hoverOffsetV, texture, texWidth, texHeight, onPress, CommonComponents.EMPTY);
    }

    public ImageButton(int x, int y, int width, int height, int u, int v, int hoverOffsetV, ResourceLocation texture, int texWidth, int texHeight, CommandCallback onPress, Component component) {
        super(x, y, width, height, component, onPress);
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.texU = u;
        this.texV = v;
        this.hoverOffsetV = hoverOffsetV;
        this.texture = texture;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        this.renderTexture(gfx, this.texture, this.getX(), this.getY(), this.texU, this.texV, this.hoverOffsetV, this.width, this.height, this.texWidth, this.texHeight);
    }
}
