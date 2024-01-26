package io.github.ultreon.mods.essentials.client.gui.screens.users;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.ultreon.mods.lib.UltreonLib;
import com.ultreon.mods.lib.UltreonLibConfig;
import com.ultreon.mods.lib.client.gui.Theme;
import com.ultreon.mods.lib.client.gui.Themed;
import com.ultreon.mods.lib.client.gui.screen.BaseScreen;
import dev.architectury.platform.Platform;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.user.AbstractClientUser;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({"FieldCanBeLocal", "UnnecessaryLocalVariable"})
public class UserListWidget extends AbstractWidget implements ContainerEventHandler, Themed {
    public static final ResourceLocation TEXTURE_DARK = UltreonLib.res("textures/gui/widgets/list/dark.png");
    public static final ResourceLocation TEXTURE_NORMAL = UltreonLib.res("textures/gui/widgets/list/normal.png");
    public static final ResourceLocation TEXTURE_LIGHT = UltreonLib.res("textures/gui/widgets/list/light.png");
    public static final ResourceLocation LIST_ICONS = UltreonLib.res("textures/gui/list_icons.png");

    private static final int ICON_SIZE = 12;
    private static final int TEX_W = 64;
    private static final int TEX_H = 64;

    private static final int ENTRY_HEIGHT = 28;
    private static final int LIST_BORDER_WIDTH = 7;
    private static final Component SEARCH_HINT = Component.literal("Search...");
    private final int headerHeight;

    private final List<GuiEventListener> children;
    private ResourceLocation guiTexture;
    private EditBox searchBox;
    private final WrappedList list;
    private final Font font;
    private GuiEventListener focused;
    private Consumer<WrappedList.Entry> onClick;
    private BiConsumer<WrappedList.Entry, Button> onClickButton;
    @Getter
    private final BaseScreen screen;
    private final Minecraft mc;
    @Getter
    private final int count;
    private final boolean hasSearch;
    private boolean isDragging;
    private Theme theme;

    public UserListWidget(BaseScreen screen, int x, int y, int width, int height, int count, Component title, List<AbstractClientUser> users) {
        this(screen, x, y, width, height, count, true, title, users);
    }

    public UserListWidget(BaseScreen screen, int x, int y, int width, int height, int count, boolean hasSearch, Component title, List<AbstractClientUser> users) {
        this(screen, x, y, width, height, count, hasSearch, title, UltreonLibConfig.THEME.get(), users);
    }

    public UserListWidget(BaseScreen screen, int x, int y, int width, int height, int count, boolean hasSearch, Component title, Theme theme, @NotNull List<AbstractClientUser> users) {
        super(x, y, width, 0, title);
        this.screen = screen;
        this.count = count;
        this.hasSearch = hasSearch;
        this.mc = Minecraft.getInstance();
        this.font = mc.font;
        this.theme = theme;

        switch (theme) {
            case DARK -> guiTexture = TEXTURE_DARK;
            case LIGHT, MIX -> guiTexture = TEXTURE_LIGHT;
            default -> guiTexture = TEXTURE_NORMAL;
        }

        this.headerHeight = hasSearch ? 18 : 0;

        if (hasSearch) {
            this.searchBox = new EditBox(this.font, x + LIST_BORDER_WIDTH + 28, y + LIST_BORDER_WIDTH + 78, width - 28 - LIST_BORDER_WIDTH * 2, 16, SEARCH_HINT) {
                @Override
                public void renderWidget(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
                    this.setX(UserListWidget.this.getX() + LIST_BORDER_WIDTH + 4 + 12 + 4);
                    this.setY(UserListWidget.this.getY() + LIST_BORDER_WIDTH + 4 + 1);

                    super.renderWidget(gfx, mouseX, mouseY, partialTicks);
                }
            };
            this.searchBox.setMaxLength(32);
            this.searchBox.setBordered(false);
            this.searchBox.setVisible(true);
            this.searchBox.setTextColor(0xffffff);
            this.searchBox.setValue("");
            this.searchBox.setResponder(this::search);
        }

        this.height = height;

        this.list = new WrappedList(this, users, mc, screen.width, screen.height, y + LIST_BORDER_WIDTH, y + height - LIST_BORDER_WIDTH * 2 + headerHeight, ENTRY_HEIGHT) {
            @Override
            public int getRowLeft() {
                return UserListWidget.this.getX() + LIST_BORDER_WIDTH;
            }

            @Override
            public int getRowWidth() {
                return UserListWidget.this.width - LIST_BORDER_WIDTH * 2;
            }

            @Override
            public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
                this.y0 = UserListWidget.this.getY() + LIST_BORDER_WIDTH + UserListWidget.this.headerHeight;
                this.y1 = UserListWidget.this.getY() + LIST_BORDER_WIDTH + UserListWidget.this.height - LIST_BORDER_WIDTH * 2;
                this.x0 = UserListWidget.this.getX() + LIST_BORDER_WIDTH + 4;
                this.x1 = UserListWidget.this.getX() + LIST_BORDER_WIDTH + 4 + 220;

                super.render(gfx, mouseX, mouseY, partialTicks);
            }
        };

        if (hasSearch) {
            this.children = List.of(searchBox, list);
        } else {
            this.children = List.of(list);
        }
    }

    @Override
    public void setFocused(boolean focus) {
        super.setFocused(focus);

        this.searchBox.setFocused(focus);
        this.list.setFocused(focus);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderTexture(0, guiTexture);
        // List border
        final int lb = LIST_BORDER_WIDTH; // lb == List Border

        // End pos
        final int x1 = getX() + width;
        final int y1 = getY() + height;

        // Pos X
        final int sx = getX();
        final int mx = getX() + lb;
        final int ex = x1 - lb;

        // Pos Y
        final int ty = getY();
        final int my = getY() + lb;
        final int by = y1 - lb;

        // Inner size
        final int iw = width - lb * 2;
        final int ih = height - lb * 2;

        // Texture V
        final int tv = 0;
        final int mv = lb;
        final int bv = lb + lb;

        // Texture U
        final int su = 0;
        final int eu = lb + lb;

        // Render
        gfx.blit(guiTexture, sx, ty, lb, lb, su, tv, lb, lb, TEX_W, TEX_H); // Top left
        gfx.blit(guiTexture, mx, ty, iw, lb, lb, tv, lb, lb, TEX_W, TEX_H); // Top
        gfx.blit(guiTexture, ex, ty, lb, lb, eu, tv, lb, lb, TEX_W, TEX_H); // Top right
        gfx.blit(guiTexture, sx, my, lb, ih, su, mv, lb, lb, TEX_W, TEX_H); // Middle left
        gfx.blit(guiTexture, mx, my, iw, ih, lb, mv, lb, lb, TEX_W, TEX_H); // Middle
        gfx.blit(guiTexture, ex, my, lb, ih, eu, mv, lb, lb, TEX_W, TEX_H); // Middle right
        gfx.blit(guiTexture, sx, by, lb, lb, su, bv, lb, lb, TEX_W, TEX_H); // Bottom left
        gfx.blit(guiTexture, mx, by, iw, lb, lb, bv, lb, lb, TEX_W, TEX_H); // Bottom
        gfx.blit(guiTexture, ex, by, lb, lb, eu, bv, lb, lb, TEX_W, TEX_H); // Bottom right

        // Search glass
        gfx.blit(guiTexture, getX() + lb + 3, getY() + lb + 3, 12, 12, 51, 1, 12, 12, TEX_W, TEX_H);

        this.list.render(gfx, mouseX, mouseY, partialTicks);
        if (searchBox != null) {
            this.searchBox.render(gfx, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (this.list.isMouseOver(x, y) && this.list.mouseClicked(x, y, button)) {
            setFocused(list);
            return true;
        }
        if (this.searchBox != null && this.searchBox.isMouseOver(x, y) && this.searchBox.mouseClicked(x, y, button)) {
            setFocused(searchBox);
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        if (this.list.isMouseOver(x, y) && this.list.mouseReleased(x, y, button)) return true;
        return this.searchBox != null && this.searchBox.isMouseOver(x, y) && this.searchBox.mouseReleased(x, y, button);
    }

    @Override
    public void mouseMoved(double x, double y) {
        if (this.list.isMouseOver(x, y)) this.list.mouseMoved(x, y);
        if (this.searchBox != null && this.searchBox.isMouseOver(x, y)) this.searchBox.mouseMoved(x, y);

        super.mouseMoved(x, y);
    }

    @Override
    public boolean mouseDragged(double x, double y, int button, double fx, double fy) {
        if (this.list.isMouseOver(fx, fy) && this.list.mouseDragged(x, y, button, fx, fy)) return true;
        if (this.searchBox != null && this.searchBox.isMouseOver(fx, fy) && this.searchBox.mouseDragged(x, y, button, fx, fy))
            return true;

        return super.mouseDragged(x, y, button, fx, fy);
    }

    @Override
    public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
        return ContainerEventHandler.super.mouseScrolled(p_94686_, p_94687_, p_94688_);
    }

    @Override
    public boolean charTyped(char c, int modifiers) {
        if (searchBox.charTyped(c, modifiers)) return true;
        return ContainerEventHandler.super.charTyped(c, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (list.keyPressed(keyCode, scanCode, modifiers) || searchBox.keyPressed(keyCode, scanCode, modifiers))
            return true;
        return ContainerEventHandler.super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (searchBox.keyReleased(keyCode, scanCode, modifiers)) return true;
        return ContainerEventHandler.super.keyReleased(keyCode, scanCode, modifiers);
    }

    public void search(String query) {
        this.list.search(query);
    }

    public String getQuery() {
        return this.list.query;
    }

    public void onClick(Consumer<WrappedList.Entry> consumer) {
        if (this.onClick == null) {
            this.onClick = consumer;
        } else {
            Consumer<WrappedList.Entry> onClick = this.onClick;
            this.onClick = entry -> {
                onClick.accept(entry);
                consumer.accept(entry);
            };
        }
    }

    public void onClickButton(BiConsumer<WrappedList.Entry, Button> consumer) {
        if (this.onClickButton == null) {
            this.onClickButton = consumer;
        } else {
            BiConsumer<WrappedList.Entry, Button> onClickButton = this.onClickButton;
            this.onClickButton = (entry, btn) -> {
                onClickButton.accept(entry, btn);
                consumer.accept(entry, btn);
            };
        }
    }

    @NotNull
    @Override
    public List<? extends @NotNull GuiEventListener> children() {
        return children;
    }

    @Override
    public boolean isDragging() {
        return isDragging;
    }

    @Override
    public void setDragging(boolean yes) {
        this.isDragging = yes;
    }

    @Nullable
    @Override
    public GuiEventListener getFocused() {
        return focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        this.focused = focused;
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput p_169152_) {
        // No narration
    }

    public int getCount() {
        return count;
    }

    public BaseScreen getScreen() {
        return screen;
    }

    public boolean hasSearch() {
        return hasSearch;
    }

    @Nullable
    public AbstractClientUser getSelected() {
        WrappedList.Entry selected = list.getSelected();
        return selected == null ? null : selected.user;
    }

    @Override
    public void reloadTheme() {
        this.theme = UltreonLib.getTheme();
        switch (theme) {
            case DARK -> guiTexture = TEXTURE_DARK;
            case LIGHT, MIX -> guiTexture = TEXTURE_LIGHT;
            default -> guiTexture = TEXTURE_NORMAL;
        }

        if (list != null) {
            list.reloadTheme();
        }
    }

    public static class WrappedList extends ContainerObjectSelectionList<WrappedList.Entry> {
        private final Minecraft mc;
        private final UserListWidget widget;
        private final Object entriesLock = new Object();
        private ResourceLocation guiTexture;
        private String query;
        private Consumer<WrappedList> addEntries;
        private final Object usersLock = new Object();
        private List<AbstractClientUser> users;

        public WrappedList(UserListWidget widget, List<AbstractClientUser> users, Minecraft mc, int width, int height, int top, int bottom, int itemHeight) {
            super(mc, width, height, top, bottom, itemHeight);
            this.mc = mc;
            this.users = users;

            this.widget = widget;
            this.guiTexture = widget.guiTexture;

            this.setRenderSelection(true);
            this.setRenderBackground(false);
            this.setRenderTopAndBottom(false);
        }

        @Override
        protected int getRowTop(int index) {
            return this.y0 - (int) this.getScrollAmount() + index * this.itemHeight + this.headerHeight;
        }

        @Override
        protected int getScrollbarPosition() {
            return getRowRight(); // 124 default
        }

        public int getMaxScroll() {
            return Math.max(0, this.getMaxPosition() - (this.y1 - this.y0));
        }

        @Override
        public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
            double scaleFactor = this.mc.getWindow().getGuiScale();

            int yi = y0 + 60 - 18; // Idk anymore
            int yj = y1 - y0;

            RenderSystem.enableScissor(
                    (int) (this.getRowLeft() * scaleFactor),
                    (int) ((widget.screen.height - yi) * scaleFactor),
                    (int) ((this.getRowWidth() + 6) * scaleFactor),
                    (int) (yj * scaleFactor)
            );
            synchronized (entriesLock) {
                super.render(gfx, mouseX, mouseY, partialTicks);
            }
            RenderSystem.disableScissor();
        }

        @Override
        public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
            return super.mouseClicked(pMouseX, pMouseY, pButton);
        }

        public void search(String text) {
            query = text;
            reloadEntries();
        }

        public void update(@NotNull Collection<? extends @NotNull AbstractClientUser> users) {
            synchronized (usersLock) {
                this.users.clear();
                this.users.addAll(users);
            }
            reloadEntries();
        }

        private void reloadEntries() {
            synchronized (entriesLock) {
                this.clearEntries();

                // Debug Log Users
                for (AbstractClientUser user : users) {
                    if (Platform.isDevelopmentEnvironment()) {
                        UEssentials.LOGGER.info("User: " + user.getName() + " " + user.getSkinLocation().toString());
                    }
                }

                synchronized (usersLock) {
                    for (AbstractClientUser user : this.users) {
                        String name = user.getName();
                        if (query.isBlank() || name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                            this.addEntry(new Entry(this.mc, this, user.getName(), null, user::getSkinLocation, user, guiTexture));
                        }
                    }
                }
            }
        }

        public void reloadTheme() {
            this.guiTexture = widget.guiTexture;
            this.reloadEntries();
        }

        @SuppressWarnings("unused")
        @Environment(EnvType.CLIENT)
        public static class Entry extends ContainerObjectSelectionList.Entry<UserListWidget.WrappedList.Entry> {
            public static final int NO_DESC_TEXT_COLOR = FastColor.ARGB32.color(255, 74, 74, 74);
            public static final int DESC_COLOR = FastColor.ARGB32.color(255, 48, 48, 48);
            public static final int TITLE_COLOR = FastColor.ARGB32.color(255, 255, 255, 255);
            public static final int TEXT_COLOR = FastColor.ARGB32.color(140, 255, 255, 255);

            private final Minecraft mc;
            private final @NotNull UserListWidget.WrappedList list;
            private final String entryTitle;
            private final Supplier<ResourceLocation> texture;
            private final AbstractClientUser user;
            private final Component description;
            private final List<Button> buttons;
            private float ticksTooltip;
            private final ResourceLocation guiTexture;

            public Entry(@NotNull Minecraft minecraft, @NotNull WrappedList list, @NotNull String title, @Nullable String description, @NotNull Supplier<@NotNull ResourceLocation> texture, @NotNull AbstractClientUser user, @NotNull ResourceLocation guiTexture) {
                this.mc = minecraft;
                this.list = list;
                this.entryTitle = title;
                this.description = description == null ? Component.empty() : Component.literal(description);
                this.texture = texture;
                this.buttons = ImmutableList.of();
                this.user = user;
                this.guiTexture = guiTexture;
            }

            @SuppressWarnings("UnnecessaryLocalVariable")
            public void render(@NotNull GuiGraphics gfx, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
//                height = list.itemHeight;

//                gfx.drawString(mc.font, "index = " + index + ", top = " + top + ", left = " + left + ", width = " + width + ", height = " + height, left, top + 5, TITLE_COLOR);
//                gfx.fill(0, 0, 900, 900, 0xffffffff);
//                gfx.fill(mouseX, mouseY, 50, 50, 0xffffffff);

                left+=4;

                final int i = left + 1;
                final int j = top + (height - ICON_SIZE) / 2;
                final int k = i + 8 + 4 + 2;
                final int l = top + (height - mc.font.lineHeight) / 2;

                // Entry section
                final int es = 4;
                final int in = list.getSelected() == this ? 33 : 21;

                // End pos
                final int x1 = left + width;
                final int y1 = top + height;

                // Pos X
                final int sx = left;
                final int mx = left + es;
                final int ex = x1 - es;

                // Pos Y
                final int ty = top;
                final int my = top + es;
                final int by = y1 - es;

                // Inner size
                final int iw = width - es * 2;
                final int ih = height - es * 2;

                // Texture V
                final int tv = in;
                final int mv = in + es;
                final int bv = in + es + es;

                // Texture U
                final int su = 0;
                final int eu = es + es;

                // Render
                gfx.blit(guiTexture, sx, ty, es, es, su, tv, es, es, TEX_W, TEX_H); // Top left
                gfx.blit(guiTexture, mx, ty, iw, es, es, tv, es, es, TEX_W, TEX_H); // Top
                gfx.blit(guiTexture, ex, ty, es, es, eu, tv, es, es, TEX_W, TEX_H); // Top right
                gfx.blit(guiTexture, sx, my, es, ih, su, mv, es, es, TEX_W, TEX_H); // Middle left
                gfx.blit(guiTexture, mx, my, iw, ih, es, mv, es, es, TEX_W, TEX_H); // Middle
                gfx.blit(guiTexture, ex, my, es, ih, eu, mv, es, es, TEX_W, TEX_H); // Middle right
                gfx.blit(guiTexture, sx, by, es, es, su, bv, es, es, TEX_W, TEX_H); // Bottom left
                gfx.blit(guiTexture, mx, by, iw, es, es, bv, es, es, TEX_W, TEX_H); // Bottom
                gfx.blit(guiTexture, ex, by, es, es, eu, bv, es, es, TEX_W, TEX_H); // Bottom right

                RenderSystem.enableBlend();
                gfx.blit(guiTexture, i, j, ICON_SIZE, ICON_SIZE, 40.0F, 8.0F, 8, 8, 64, 64);
                RenderSystem.disableBlend();
                gfx.drawString(this.mc.font, this.entryTitle, left, (int) ((float) top + 1), list.widget.theme.getTextColor(), false);

                float f = this.ticksTooltip;

                if (f == this.ticksTooltip) {
                    this.ticksTooltip = 0.0F;
                }
            }

            public @NotNull List<? extends GuiEventListener> children() {
                return this.buttons;
            }

            @NotNull
            public String getTitle() {
                return this.entryTitle;
            }

            @NotNull
            private Component getDescription() {
                return description;
            }

            public @NotNull UserListWidget.WrappedList getList() {
                return list;
            }

            @Override
            public @NotNull List<? extends NarratableEntry> narratables() {
                return new ArrayList<>();
            }

            @Override
            public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
                return true;
            }

            public boolean click() {
                list.setSelected(this);
                return true;
            }
        }
    }
}
