package io.github.ultreon.mods.essentials.client.gui;

import dev.architectury.event.events.client.ClientGuiEvent;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.UEssentialsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class UIManager {
    private static final ResourceLocation OVERLAY_LOCATION = UEssentials.res("textures/gui/message_overlay.png");
    private static Component overlayTitle;
    private static Component overlayMessage;
    private static boolean overlayShown;

    static {
        ClientGuiEvent.RENDER_HUD.register(UIManager::onRender);
    }

    private static void onRender(GuiGraphics graphics, float tickDelta) {
        Minecraft mc = Minecraft.getInstance();

        if (overlayShown) {
            renderOverlay(mc, graphics);
        }
    }

    private static void renderOverlay(Minecraft mc, GuiGraphics graphics) {
        // Blit overlay (150x50)
        graphics.blit(OVERLAY_LOCATION, graphics.guiWidth() / 2 - 75, graphics.guiHeight() / 2 - 25, 150, 50, 0, 0, 150, 50, 150, 50);

        // Render title
        graphics.pose().pushPose();
        graphics.pose().scale(2,2, 1);

        graphics.drawCenteredString(mc.font, overlayTitle, graphics.guiWidth() / 4, graphics.guiHeight() / 4 - 50, 0xffffff);

        graphics.pose().popPose();

        // Render message
        graphics.drawCenteredString(mc.font, overlayMessage, graphics.guiWidth() / 2, graphics.guiHeight() / 2, 0x808080);
    }

    public static void showOverlay(Component title, Component message) {
        if (UEssentialsConfig.UI_SHOW_OVERLAY.get()) {
            overlayTitle = title;
            overlayMessage = message;
            overlayShown = true;
            return;
        }

        ChatComponent chat = Minecraft.getInstance().gui.getChat();
        chat.addMessage(Component.empty().append(title));
        chat.addMessage(Component.empty().append(message));
    }

    public static void updateOverlay(Component title, Component message) {
        overlayTitle = title;
        overlayMessage = message;
    }

    public static void updateOverlayTitle(Component title) {
        overlayTitle = title;
        overlayShown = true;
    }

    public static void updateOverlayMessage(Component message) {
        overlayMessage = message;
        overlayShown = true;
    }

    public static void hideOverlay() {
        overlayShown = false;
    }
}
