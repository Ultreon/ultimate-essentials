package io.github.ultreon.mods.essentials.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ultreon.mods.essentials.client.UEssentialsClient;
import io.github.ultreon.mods.essentials.user.LocalUser;
import io.github.ultreon.mods.essentials.util.MoneyUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class OnlineBankingScreen extends Screen {
    private static final ResourceLocation TEX = UEssentialsClient.gui("banking");

    private static final int NAME_COLOR = FastColor.ARGB32.color(0, 255, 255, 255);
    private static final int UUID_COLOR = FastColor.ARGB32.color(0, 128, 128, 128);

    private final Supplier<ResourceLocation> texture;
    private final Minecraft mc = Minecraft.getInstance();
    private final LocalUser user = LocalUser.get();

    protected OnlineBankingScreen() {
        super(Component.literal("Online Banking"));

        this.texture = () -> LocalUser.get().getSkinLocation();
    }

    public void open() {
        Minecraft.getInstance().setScreen(this);
    }

    public int width() {
        return 176;
    }

    public int height() {
        return 179;
    }

    public int left() {
        return (width - width()) / 2;
    }

    public int top() {
        return (height - height()) / 2;
    }

    public int right() {
        return (width + width()) / 2;
    }

    public int bottom() {
        return (height + height()) / 2;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void renderBackground(GuiGraphics gfx) {
        super.renderBackground(gfx);

        gfx.blit(TEX, left(), top(), width(), height(), 0, 0, width(), height(), 256, 256);
    }

    @Override
    public void render(@NotNull GuiGraphics gfx, int mx, int my, float pt) {
        renderBackground(gfx);

        super.render(gfx, mx, my, pt);

        if (isPointBetween(mx, my, left() + 5, top() + 47, 54, 34))
            gfx.blit(TEX, left() + 5, top() + 47, 54, 34, 176, 151, 54, 34, 256, 256);
        else
            gfx.blit(TEX, left() + 5, top() + 47, 54, 34, 5, 219, 54, 34, 256, 256);

        if (isPointBetween(mx, my, left() + 61, top() + 47, 54, 34))
            gfx.blit(TEX, left() + 61, top() + 47, 54, 34, 176, 185, 54, 34, 256, 256);
        else
            gfx.blit(TEX, left() + 61, top() + 47, 54, 34, 61, 219, 54, 34, 256, 256);

        if (isPointBetween(mx, my, left() + 117, top() + 47, 54, 34))
            gfx.blit(TEX, left() + 117, top() + 47, 54, 34, 176, 219, 54, 34, 256, 256);
        else
            gfx.blit(TEX, left() + 117, top() + 47, 54, 34, 117, 219, 54, 34, 256, 256);

        int iconX = left() + 6;
        int iconY = top() + 27;

        gfx.drawString(this.mc.font, Component.literal(this.mc.font.plainSubstrByWidth("Welcome " + user.getName(), width() - 24 - 2 - 6 - 6)), (int) ((float) iconX + 19), (int) ((float) iconY - 1), NAME_COLOR);
        gfx.drawString(this.mc.font, Component.literal(this.mc.font.plainSubstrByWidth("Balance: " + MoneyUtils.format(user.getBalance()), width() - 24 - 2 - 6 - 6)), (int) ((float) iconX + 19), (int) ((float) iconY + 9f), UUID_COLOR);

        ResourceLocation texture = this.texture.get();
        gfx.blit(texture, iconX, iconY, 16, 16, 8.0F, 8.0F, 8, 8, 64, 64);
        RenderSystem.enableBlend();
        gfx.blit(texture, iconX, iconY, 16, 16, 40.0F, 8.0F, 8, 8, 64, 64);
        RenderSystem.disableBlend();
    }/**/

    public boolean isPointBetween(int px, int py, int x, int y, int w, int h) {
        int x2 = x + w;
        int y2 = y + h;
        return x <= px && y <= py && x2 >= px && y2 >= py;
    }
}
