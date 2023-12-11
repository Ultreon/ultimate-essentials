package io.github.ultreon.mods.essentials.client.gui.widget.list;

import io.github.ultreon.mods.essentials.client.UEssentialsClient;
import lombok.Setter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ListScreen extends Screen {
    @Setter
    public IListFilter listFilter;
    private ListWidget list;
    protected EditBox searchBox;
    private String searchTerms = "";
    private boolean initialized;

    protected static final Component SEARCH_HINT = (Component.translatable("gui.socialInteractions.search_hint")).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);
    protected static final Component SEARCH_EMPTY = (Component.translatable("gui.socialInteractions.search_empty")).withStyle(ChatFormatting.GRAY);
    protected static final ResourceLocation GUI_TEXTURE = UEssentialsClient.res("textures/gui/list.png");

    @Setter
    IListEntryClick onListEntryClick = (list, entry) -> {

    };

    @Setter
    IInitHandler onInit = () -> {

    };

    final List<ListWidget.Entry> cachedEntries = new ArrayList<>();

    public ListScreen(Component title) {
        super(title);
    }

    public void addEntry(ListWidget.Entry entry) {
        this.cachedEntries.add(entry);
        if (list != null) {
            this.list.addEntry(entry);
        }
    }

    private int getListHeight() {
        return Math.max(52, this.height - 128 - 16);
    }

    private int func0() {
        return this.getListHeight() / 16;
    }

    private int func1() {
        return 80 + this.func0() * 16 - 8;
    }

    private int left() {
        return (this.width - 238) / 2;
    }

    public void tick() {
        super.tick();
        this.searchBox.tick();
    }

    protected void init() {
        onInit.init();

        if (this.initialized && this.list != null) {
            this.list.updateSize(this.width, this.height, 88, this.func1());
        } else {
            this.list = new ListWidget(this, this.minecraft, this.width, this.height, 88, this.func1(), 36);
        }

        String s = this.searchBox != null ? this.searchBox.getValue() : "";
        this.searchBox = new EditBox(this.font, this.left() + 28, 78, 196, 16, SEARCH_HINT);
        this.searchBox.setMaxLength(16);
        this.searchBox.setBordered(false);
        this.searchBox.setVisible(true);
        this.searchBox.setTextColor(16777215);
        this.searchBox.setValue(s);
        this.searchBox.setResponder(this::search);
        this.addRenderableWidget(this.searchBox);
        this.addRenderableWidget(this.list);
        this.initialized = true;
    }

    public void renderBackground(@NotNull GuiGraphics gfx) {
        int i = this.left() + 3;
        super.renderBackground(gfx);

        gfx.blit(GUI_TEXTURE, i, 64, 1, 1, 236, 8);
        int j = this.func0();

        for (int k = 0; k < j; ++k) {
            gfx.blit(GUI_TEXTURE, i, 72 + 16 * k, 1, 10, 236, 16);
        }

        gfx.blit(GUI_TEXTURE, i, 72 + 16 * j, 1, 27, 236, 8);
        gfx.blit(GUI_TEXTURE, i + 10, 76, 243, 1, 12, 12);
    }

    public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        Objects.requireNonNull(this.minecraft);
        this.renderBackground(gfx);
        gfx.drawString(this.minecraft.font, this.title, this.left() + 8, 35, -1);

        if (!this.list.isEmpty()) {
            this.list.render(gfx, mouseX, mouseY, partialTicks);
        } else if (!this.searchBox.getValue().isEmpty()) {
            gfx.drawCenteredString(this.minecraft.font, SEARCH_EMPTY, this.width / 2, (78 + this.func1()) / 2, -1);
        }

        if (!this.searchBox.isFocused() && this.searchBox.getValue().isEmpty()) {
            gfx.drawString(this.minecraft.font, SEARCH_HINT, this.searchBox.getX(), this.searchBox.getY(), -1);
        } else {
            this.searchBox.render(gfx, mouseX, mouseY, partialTicks);
        }

        super.render(gfx, mouseX, mouseY, partialTicks);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.searchBox.isFocused()) {
            this.searchBox.mouseClicked(mouseX, mouseY, button);
        }

        return super.mouseClicked(mouseX, mouseY, button) || this.list.mouseClicked(mouseX, mouseY, button);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean isPauseScreen() {
        return false;
    }

    private void search(String text) {
        text = text.toLowerCase(Locale.ROOT);
        if (!text.equals(this.searchTerms)) {
            this.list.search(text);
            this.searchTerms = text;
        }

    }

    @FunctionalInterface
    public interface IListEntryClick {
        void call(ListWidget list, ListWidget.Entry entry);
    }

    @FunctionalInterface
    public interface IInitHandler {
        void init();
    }

    @FunctionalInterface
    public interface IListFilter {
        boolean filter(String query, String id, String title, String description);
    }
}
