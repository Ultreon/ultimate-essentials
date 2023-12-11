package io.github.ultreon.mods.essentials.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ultreon.mods.lib.UltreonLib;
import com.ultreon.mods.lib.client.gui.Theme;
import io.github.ultreon.mods.essentials.user.AbstractClientUser;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class UserWidget extends BaseWidget {
    private final Minecraft mc = Minecraft.getInstance();
    private final Supplier<ResourceLocation> texture;
    private final AbstractClientUser user;
    @Getter
    @Setter
    private Theme theme;
    private final int nameColor;
    private final int uuidColor;
    private final int borderColor;

    public UserWidget(AbstractClientUser user) {
        this(0, 0, 0, 0, user, UltreonLib.getTheme());
    }

    public UserWidget(AbstractClientUser user, Theme theme) {
        this(0, 0, 0, 0, user, theme);
    }

    public UserWidget(int x, int y, int width, int height, AbstractClientUser user) {
        this(x, y, width, height, user, UltreonLib.getTheme());
    }

    public UserWidget(int x, int y, int width, int height, AbstractClientUser user, Theme theme) {
        super(x, y, Math.max(width, 26), Math.max(height, 26), Component.literal(user.getProfile().getName()));
        this.texture = user::getSkinLocation;
        this.user = user;
        this.theme = theme;

        this.nameColor = theme.getTextColor();
        this.uuidColor = theme.getTextColor();
        this.borderColor = theme.getTextColor();
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        gfx.fill(getX(), getY(), 26, 26, borderColor);

        gfx.drawString(this.mc.font, Component.literal(this.mc.font.plainSubstrByWidth(user.getName(), width - 24 - 2 - 4)), getX() + 24 + 2 + 4, getY() + 4, nameColor);
        gfx.drawString(this.mc.font, Component.literal(this.mc.font.plainSubstrByWidth(user.uuid().toString(), width - 24 - 2 - 4)), getX() + 2 + 24 + 4, getY() + 14, uuidColor);

        ResourceLocation texture = this.texture.get();
        gfx.blit(texture, getX(), getY() + 1, 24, 24, 8.0F, 8.0F, 8, 8, 64, 64);
        RenderSystem.enableBlend();
        gfx.blit(texture, getX(), getY() + 1, 24, 24, 40.0F, 8.0F, 8, 8, 64, 64);
        RenderSystem.disableBlend();

    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput p_169152_) {
        p_169152_.add(NarratedElementType.TITLE, user.getName());
    }
}
