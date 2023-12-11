package io.github.ultreon.mods.essentials.client.gui;

import io.github.ultreon.mods.essentials.client.UEssentialsClient;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class InternalListScreen extends Screen {
    protected Component title = Component.empty();

    protected static final Component SEARCH_HINT = (Component.translatable("gui.socialInteractions.search_hint")).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);
    protected static final Component TAB_TITLE = (Component.translatable("gui.socialInteractions.search_empty")).withStyle(ChatFormatting.GRAY);

    public static final ResourceLocation GUI_LOCATION = UEssentialsClient.res("textures/gui/list.png");
    public static final ResourceLocation LIST_ICONS = UEssentialsClient.res("textures/gui/list_icons.png");

    public InternalListScreen(Component title) {
        super(title);
    }
}
