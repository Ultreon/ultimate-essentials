package io.github.ultreon.mods.essentials.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ultreon.mods.lib.client.gui.screen.PanoramaScreen;
import com.ultreon.mods.lib.client.gui.widget.Button;
import io.github.ultreon.mods.essentials.client.UEssentialsClient;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class BaseConnectScreen extends PanoramaScreen {
    private static final int TITLE_COLOR = Objects.requireNonNull(ChatFormatting.WHITE.getColor()); // FastColor.ARGB32.color(255, 208, 208, 208);

    public static final ResourceLocation BACKGROUND_TEXTURE = UEssentialsClient.res("textures/gui/loader_connect.png");
    public static final Component SEARCH_BACKDROP = Component.translatable("gui.ultimate_essentials.setHome.nameField");

    protected final String defaultInput;
    protected String placeholder = "";

    protected EditBox inputBox;
    protected Button connectButton;
    protected Button quitButton;
    protected Button optionsButton;
    protected Button modsButton;

    protected BaseConnectScreen(Component title, String defaultInput) {
        super(title);
        this.defaultInput = defaultInput;
    }

    public void tick() {
        super.tick();
        this.inputBox.tick();
    }

    protected void init() {
        this.inputBox = new EditBox(this.font, this.left() + 7, this.top() + 19, 196, 16, SEARCH_BACKDROP);
        this.inputBox.setMaxLength(64);
        this.inputBox.setBordered(false);
        this.inputBox.setVisible(true);
        this.inputBox.setTextColor(16777215);
        this.inputBox.setValue(defaultInput);

        setInitialFocus(this.inputBox);

        this.connectButton = new Button(
                left() + 6, top() + 33,
                (width() / 2 - 2 - 6), 20,
                Component.translatable("gui.next"), (button) -> {
            if (button.visible && button.active) next();
        }, Button.Type.PRIMARY);
        this.quitButton = new Button(
                left() + 6 + ((width() / 2 + 2 - 6)), top() + 33,
                (width() / 2 - 2 - 6), 20,
                CommonComponents.GUI_BACK, (button) -> {
            if (button.visible && button.active) back();
        });
        this.optionsButton = new Button(
                left() + 6, top() + 33 + 4 + 20,
                (width() / 2 - 2 - 6), 20,
                Component.translatable("menu.options"), (button) -> {
            if (button.visible && button.active) options();
        });
        this.modsButton = new Button(
                left() + 6 + ((width() / 2 + 2 - 6)), top() + 33 + 4 + 20,
                (width() / 2 - 2 - 6), 20,
                Component.translatable("fml.menu.mods"), (button) -> {
            if (button.visible && button.active) mods();
        });

        this.addRenderableWidget(this.inputBox);
        this.addRenderableWidget(this.connectButton);
        this.addRenderableWidget(this.quitButton);
        this.addRenderableWidget(this.optionsButton);
        this.addRenderableWidget(this.modsButton);
    }

    protected int width() {
        return 176;
    }

    protected int height() {
        return 83;
    }

    protected int left() {
        return (this.width - width()) / 2;
    }

    @SuppressWarnings("unused")
    protected int right() {
        return (this.width + width()) / 2;
    }

    protected int top() {
        return (this.height - height()) / 2;
    }

    @SuppressWarnings("unused")
    protected int bottom() {
        return (this.height + height()) / 2;
    }

    protected abstract void back();

    protected abstract void next();

    private void mods() {
        UEssentialsClient.showModList();
    }

    public void options() {
        Minecraft.getInstance().setScreen(new OptionsScreen(this, Minecraft.getInstance().options));
    }

    public void renderBackground(@NotNull GuiGraphics gfx) {
        int i = this.left();
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        gfx.blit(BACKGROUND_TEXTURE, i, this.top(), 0, 0, width(), height());
    }

    public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        this.renderPanorama(gfx, partialTicks);
        this.renderBackground(gfx);

        assert this.minecraft != null;
        gfx.drawCenteredString(this.minecraft.font, title.getString(), left() + width() / 2, top() + 5/* + 9*/, TITLE_COLOR);

        if (!this.inputBox.isFocused() && this.inputBox.getValue().isEmpty()) {
            gfx.drawString(this.minecraft.font, placeholder, this.inputBox.getX(), this.inputBox.getY(), -1);
        } else {
            this.inputBox.render(gfx, mouseX, mouseY, partialTicks);
        }

        super.render(gfx, mouseX, mouseY, partialTicks);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.inputBox.isFocused()) {
            this.inputBox.mouseClicked(mouseX, mouseY, button);
        }

        return super.mouseClicked(mouseX, mouseY, button)/* || this.filterList.mouseClicked(mouseX, mouseY, button)*/;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 && this.inputBox.isFocused()) {
            next();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean isPauseScreen() {
        return false;
    }
}
