package io.github.ultreon.mods.essentials.client.gui.screens.message;

import com.ultreon.mods.lib.client.gui.screen.PanoramaScreen;
import com.ultreon.mods.lib.client.gui.widget.Button;
import io.github.ultreon.mods.essentials.client.UEssentialsClient;
import io.github.ultreon.mods.essentials.util.GuiUtil;
import io.github.ultreon.mods.essentials.util.TextUtils;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WarningScreen extends PanoramaScreen {
    public static final ResourceLocation BACKGROUND_TEXTURE = UEssentialsClient.res("textures/gui/message_screen.png");
    private final Component description;
    @Getter
    private Button backButton;

    public WarningScreen(Component title, Component description) {
        this(null, title, description);
    }

    public WarningScreen(Screen back, Component title, Component description) {
        super(title);
        this.description = description;
    }

    @Override
    protected void init() {
        clearWidgets();

        super.init();

        this.backButton = new Button(
                left() + 6, bottom() - 6 - 20,
                (width() - 6 - 6), 20,
                CommonComponents.GUI_BACK, (button) -> {
            if (button.visible && button.active) back();
        });

        addRenderableWidget(backButton);
    }

    protected int width() {
        return 192;
    }

    protected int height() {
        return 93;
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

    public void renderBackground(@NotNull GuiGraphics gfx) {
        int i = this.left();
        gfx.blit(BACKGROUND_TEXTURE, i, this.top(), 0, 0, width(), height());
    }

    @Override
    public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        this.renderPanorama(gfx, partialTicks);
        this.renderBackground(gfx);

        assert this.minecraft != null;
        gfx.drawCenteredString(this.minecraft.font, Component.literal(ChatFormatting.GOLD + ChatFormatting.BOLD.toString() + TextUtils.trimFormatting(title.getString())), left() + width() / 2, top() + 5/* + 9*/, 0xffffff);
        GuiUtil.renderCenteredWrappedText(gfx, description.getString(), left() + width() / 2, top() + 5 + 20/* + 9*/, width() - 16, 0xffffff);

        super.render(gfx, mouseX, mouseY, partialTicks);
    }

    @Override
    public @Nullable Vec2 getCloseButtonPos() {
        return null;
    }

}
