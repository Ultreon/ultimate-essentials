package io.github.ultreon.mods.essentials.client.gui.widget.list;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ultreon.mods.lib.client.gui.widget.Button;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("DuplicatedCode")
public class ListWidget extends ContainerObjectSelectionList<ListWidget.Entry> {
    private final Minecraft mc;
    private final ListScreen screen;
    private final List<Entry> defaultEntries = new ArrayList<>();
    private String search;

    public ListWidget(ListScreen screen, Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraft, width, height, top, bottom, itemHeight);
        this.mc = minecraft;

        this.screen = screen;

        for (Entry cachedEntry : screen.cachedEntries) {
            addEntry(cachedEntry);
        }

        this.setRenderBackground(false);
        this.setRenderTopAndBottom(false);
    }

    @Override
    protected int addEntry(@NotNull ListWidget.Entry entry) {
        defaultEntries.add(entry);
        if (search != null) {
            super.addEntry(entry);
        }
        return this.defaultEntries.size() - 1;
    }

    protected int getScrollbarPosition() {
        return this.width / 2 + 105; // 124 default
    }

    public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        double scaleFactor = this.mc.getWindow().getGuiScale();
        RenderSystem.enableScissor((int) ((double) this.getRowLeft() * scaleFactor), (int) ((double) (this.height - this.y1) * scaleFactor), (int) ((double) (this.getScrollbarPosition() + 6) * scaleFactor), (int) ((double) (this.height - (this.height - this.y1) - this.y0 - 4) * scaleFactor));
        super.render(gfx, mouseX, mouseY, partialTicks);
        RenderSystem.disableScissor();
    }

    public void search(String text) {
        this.search = Objects.equals(text, "") ? null : text;

        this.clearEntries();

        for (Entry entry : this.defaultEntries) {
            if (screen.listFilter.filter(text, entry.getId(), entry.getTitle(), entry.getDescription().getString())) {
                super.addEntry(entry);
            }
        }
    }

    public boolean isEmpty() {
        return defaultEntries.isEmpty();
    }

    public void loadCache() {

    }

    @Environment(EnvType.CLIENT)
    public static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
        private static final int BUTTON_WIDTH = 60;
        private final Minecraft mc;
        private final List<GuiEventListener> buttons;
        private final String entryTitle;
        @Getter
        private final ListScreen screen;
        private float ticksTooltip;

        public static final int TITLE_COLOR = FastColor.ARGB32.color(255, 255, 255, 255);
        public static final int DESCRIPTION_COLOR = FastColor.ARGB32.color(140, 255, 255, 255);
        public static final int EMPTY_COLOR = FastColor.ARGB32.color(255, 74, 74, 74);
        public static final int NON_EMPTY_COLOR = FastColor.ARGB32.color(255, 48, 48, 48);

        @Getter
        private final Component description;
        @Getter
        private final String id;

        public Entry(Minecraft minecraft, ListScreen screen, String title, String description, String id, Button... buttons) {
            this.mc = minecraft;
            this.entryTitle = title;
            this.description = Component.literal(description);
            this.id = id;
            this.screen = screen;

            this.buttons = Arrays.asList(buttons);
        }

        public void render(@NotNull GuiGraphics gfx, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
            int i = left + 4;

            Component itextcomponent = this.getDescription();
            int l;
            if (Objects.equals(itextcomponent, Component.empty())) {
                gfx.fill(left, top, left + width, top + height, EMPTY_COLOR);
                l = top + (height - 9) / 2;
            } else {
                gfx.fill(left, top, left + width, top + height, NON_EMPTY_COLOR);
                l = top + (height - (9 + 9)) / 2;
                gfx.drawString(this.mc.font, itextcomponent, i, l + 12, DESCRIPTION_COLOR);
            }

            gfx.drawString(this.mc.font, this.entryTitle, i, l, TITLE_COLOR);
            float ticksUntilTooltip = this.ticksTooltip;

            int buttonIndex = 0;
            for (GuiEventListener guiEventListener : buttons) {
                if (guiEventListener instanceof Button button) {
                    button.setX(left + width - 8 - (BUTTON_WIDTH + 4) * (buttonIndex + 1));
                    button.setY(top + height / 2 - 10);
                    button.render(gfx, mouseX, mouseY, partialTicks);
                    buttonIndex++;
                }
            }

            if (ticksUntilTooltip == this.ticksTooltip) {
                this.ticksTooltip = 0.0F;
            }
        }

        public String getTitle() {
            return this.entryTitle;
        }

        @NotNull
        @Override
        public List<? extends NarratableEntry> narratables() {
            return Collections.emptyList();
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return buttons;
        }
    }
}
