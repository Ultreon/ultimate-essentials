package io.github.ultreon.mods.essentials.client.gui.widget;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseContainerWidget extends AbstractContainerWidget {
    private final List<? extends GuiEventListener> children = new ArrayList<>();

    public BaseContainerWidget(int p_93629_, int p_93630_, int p_93631_, int p_93632_, Component p_93633_) {
        super(p_93629_, p_93630_, p_93631_, p_93632_, p_93633_);
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return children;
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput p_169152_) {

    }
}
