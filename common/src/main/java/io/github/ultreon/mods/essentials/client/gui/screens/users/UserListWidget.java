package io.github.ultreon.mods.essentials.client.gui.screens.users;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.ultreon.mods.lib.client.gui.widget.Button;
import io.github.ultreon.mods.essentials.client.UEssentialsClient;
import io.github.ultreon.mods.essentials.client.gui.screens.UEssentialsMenuScreen;
import io.github.ultreon.mods.essentials.user.AbstractClientUser;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
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
public class UserListWidget extends AbstractWidget implements ContainerEventHandler {
    public static final ResourceLocation GUI_LOCATION = UEssentialsClient.res("textures/gui/widgets/list.png");
    public static final ResourceLocation LIST_ICONS = UEssentialsClient.res("textures/gui/list_icons.png");

    private static final int ICON_SIZE = 12;
    private static final int TEX_W = 64;
    private static final int TEX_H = 64;

    private static final int ENTRY_HEIGHT = 14;
    private static final int LIST_BORDER_WIDTH = 7;
    private static final Component SEARCH_HINT = Component.literal("Search...");
    private final int headerHeight;

    private final List<GuiEventListener> children;
    private EditBox searchBox;
    private final ListWidget list;
    private final Font font;
    private GuiEventListener focused;
    private Consumer<ListWidget.Entry> onClick;
    private BiConsumer<ListWidget.Entry, Button> onClickButton;
    @Getter
    private final UEssentialsMenuScreen screen;
    private final Minecraft mc;
    @Getter
    private final int count;
    private final boolean hasSearch;
    private boolean isDragging;

    public UserListWidget(UEssentialsMenuScreen screen, int x, int y, int width, int count, Component title, List<AbstractClientUser> users) {
        this(screen, x, y, width, count, true, title, users);
    }

    public UserListWidget(UEssentialsMenuScreen screen, int x, int y, int width, int count, boolean hasSearch, Component title, List<AbstractClientUser> users) {
        super(x, y, width, width, title);
        this.screen = screen;
        this.count = count;
        this.hasSearch = hasSearch;
        this.mc = Minecraft.getInstance();
        this.font = mc.font;

        this.headerHeight = hasSearch ? 18 : 0;

        if (hasSearch) {
            this.searchBox = new EditBox(this.font, x + LIST_BORDER_WIDTH + 28, y + LIST_BORDER_WIDTH + 78, width - 28 - LIST_BORDER_WIDTH * 2, 16, SEARCH_HINT) {
                @Override
                public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float frameTime) {
                    this.setX(UserListWidget.this.getX() + LIST_BORDER_WIDTH + 4 + 12 + 4);
                    this.setY(UserListWidget.this.getY() + LIST_BORDER_WIDTH + 4 + 1);

                    super.render(gfx, mouseX, mouseY, frameTime);
                }
            };
            this.searchBox.setMaxLength(32);
            this.searchBox.setBordered(false);
            this.searchBox.setVisible(true);
            this.searchBox.setTextColor(0xffffff);
            this.searchBox.setValue("");
            this.searchBox.setResponder(this::search);
        }

        this.height = count * ENTRY_HEIGHT + headerHeight + LIST_BORDER_WIDTH * 2;

        this.list = new ListWidget(this, users, mc, screen.width, screen.height, y + LIST_BORDER_WIDTH, y + height - LIST_BORDER_WIDTH * 2 + headerHeight, ENTRY_HEIGHT) {
            @Override
            public int getRowLeft() {
                return UserListWidget.this.getX() + LIST_BORDER_WIDTH;
            }

            @Override
            public int getRowWidth() {
                return UserListWidget.this.width - LIST_BORDER_WIDTH * 2;
            }

            @Override
            public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float frameTime) {
//                this.width = screen.width;
//                this.height = UserListWidget.this.height - LIST_BORDER_WIDTH * 2 + UserListWidget.this.headerHeight;
                this.y0 = UserListWidget.this.getY() + LIST_BORDER_WIDTH + UserListWidget.this.headerHeight;
                this.y1 = UserListWidget.this.getY() + LIST_BORDER_WIDTH + UserListWidget.this.height - LIST_BORDER_WIDTH * 2;

                super.render(gfx, mouseX, mouseY, frameTime);
            }
        };
        this.list.setFocused(true);

        if (hasSearch) {
            this.children = ImmutableList.of(searchBox, list);
        } else {
            this.children = ImmutableList.of(list);
        }
    }

    @Override
    public void setFocused(boolean focus) {
        super.setFocused(focus);

        this.searchBox.setFocused(focus);
        this.list.setFocused(focus);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float frameTime) {
        RenderSystem.setShaderTexture(0, GUI_LOCATION);
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
        gfx.blit(GUI_LOCATION, sx, ty, lb, lb, su, tv, lb, lb, TEX_W, TEX_H); // Top left
        gfx.blit(GUI_LOCATION, mx, ty, iw, lb, lb, tv, lb, lb, TEX_W, TEX_H); // Top
        gfx.blit(GUI_LOCATION, ex, ty, lb, lb, eu, tv, lb, lb, TEX_W, TEX_H); // Top right
        gfx.blit(GUI_LOCATION, sx, my, lb, ih, su, mv, lb, lb, TEX_W, TEX_H); // Middle left
        gfx.blit(GUI_LOCATION, mx, my, iw, ih, lb, mv, lb, lb, TEX_W, TEX_H); // Middle
        gfx.blit(GUI_LOCATION, ex, my, lb, ih, eu, mv, lb, lb, TEX_W, TEX_H); // Middle right
        gfx.blit(GUI_LOCATION, sx, by, lb, lb, su, bv, lb, lb, TEX_W, TEX_H); // Bottom left
        gfx.blit(GUI_LOCATION, mx, by, iw, lb, lb, bv, lb, lb, TEX_W, TEX_H); // Bottom
        gfx.blit(GUI_LOCATION, ex, by, lb, lb, eu, bv, lb, lb, TEX_W, TEX_H); // Bottom right

        // Search glass
        gfx.blit(GUI_LOCATION, getX() + lb + 3, getY() + lb + 3, 12, 12, 51, 1, 12, 12, TEX_W, TEX_H);
        
        // Widget render.
//        super.render(pose, mouseX, mouseY, frameTime);

        this.list.render(gfx, mouseX, mouseY, frameTime);
        if (searchBox != null) {
            this.searchBox.render(gfx, mouseX, mouseY, frameTime);
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (this.list.isMouseOver(x, y) && this.list.mouseClicked(x, y, button)) {
            setFocused(list);
            return true;
        }
        if (this.searchBox.isMouseOver(x, y) && this.searchBox.mouseClicked(x, y, button)) {
            setFocused(searchBox);
            return true;
        }
        
        return super.mouseClicked(x, y, button);
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        if (this.list.isMouseOver(x, y) && this.list.mouseReleased(x, y, button)) return true;
        if (this.searchBox.isMouseOver(x, y) && this.searchBox.mouseReleased(x, y, button)) return true;
        
        return super.mouseReleased(x, y, button);
    }
    
    @Override
    public void mouseMoved(double x, double y) {
        if (this.list.isMouseOver(x, y)) this.list.mouseMoved(x, y);
        if (this.searchBox.isMouseOver(x, y)) this.searchBox.mouseMoved(x, y);
        
        super.mouseMoved(x, y);
    }
    
    @Override
    public boolean mouseDragged(double x, double y, int button, double fx, double fy) {
        if (this.list.isMouseOver(fx, fy) && this.list.mouseDragged(x, y, button, fx, fy)) return true;
        if (this.searchBox.isMouseOver(fx, fy) && this.searchBox.mouseDragged(x, y, button, fx, fy)) return true;
        
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
        if (list.keyPressed(keyCode, scanCode, modifiers) || searchBox.keyPressed(keyCode, scanCode, modifiers)) return true;
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

    public void update(List<? extends AbstractClientUser> users) {
        this.list.update(users);
    }

    public void onClick(Consumer<ListWidget.Entry> consumer) {
        if (this.onClick == null) {
            this.onClick = consumer;
        } else {
            Consumer<ListWidget.Entry> onClick = this.onClick;
            this.onClick = entry -> {
                onClick.accept(entry);
                consumer.accept(entry);
            };
        }
    }

    public void onClickButton(BiConsumer<ListWidget.Entry, Button> consumer) {
        if (this.onClickButton == null) {
            this.onClickButton = consumer;
        } else {
            BiConsumer<ListWidget.Entry, Button> onClickButton = this.onClickButton;
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

    }

    public boolean hasSearch() {
        return hasSearch;
    }

    @Nullable
    public AbstractClientUser getSelected() {
        ListWidget.Entry selected = list.getSelected();
        return selected == null ? null : selected.user;
    }

    public static class ListWidget extends ContainerObjectSelectionList<ListWidget.Entry> {
        private final Minecraft mc;
        private final List<AbstractClientUser> users;
        @Getter
        private final UserListWidget widget;
        private final Object entriesLock = new Object();
        private final Object usersLock = new Object();
        private String query;

        public ListWidget(UserListWidget widget, List<AbstractClientUser> users, Minecraft mc, int width, int height, int top, int bottom, int itemHeight) {
            super(mc, width, height, top, bottom, itemHeight);
            this.mc = mc;

            for (AbstractClientUser user : users) {
                this.addEntry(new Entry(this.mc, this, user.getName(), null, user::getSkinLocation, user));
            }

            this.users = users;
            this.widget = widget;

            this.setRenderSelection(false);
            this.setRenderBackground(false);
            this.setRenderTopAndBottom(false);
        }

        @Override
        protected int getRowTop(int index) {
            return this.y0 - (int)this.getScrollAmount() + index * this.itemHeight + this.headerHeight;
        }

        @Override
        protected int getScrollbarPosition() {
            return getRowRight(); // 124 default
        }

        public int getMaxScroll() {
            return Math.max(0, this.getMaxPosition() - (this.y1 - this.y0));
        }

        @Override
        public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float frameTime) {
            double scaleFactor = this.mc.getWindow().getGuiScale();

            int yi = y0 + (60 - 18); // Idk anymore
            int yj = y1 - y0;
            RenderSystem.enableScissor(
                    (int) ((double) (this.getRowLeft()) * scaleFactor),
                    (int) ((double) ((widget.screen.height - yi)) * scaleFactor),
                    (int) ((double) (this.getRowWidth() + 6) * scaleFactor),
                    (int) ((double) (yj) * scaleFactor)
            );
            synchronized (entriesLock) {
                super.render(gfx, mouseX, mouseY, frameTime);
//                this.renderBackground(pose);
//                int i = this.getScrollbarPosition();
//                int j = i + 6;
//                Tesselator tesselator = Tesselator.getInstance();
//                BufferBuilder bufferbuilder = tesselator.getBuilder();
//                RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
//                this.hovered = this.isMouseOver(mouseX, mouseY) ? this.getEntryAtPosition(mouseX, mouseY) : null;
//                if (this.renderBackground) {
//                    RenderSystem.setShaderTexture(0, GuiComponent.BACKGROUND_LOCATION);
//                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//                    float f = 32.0F;
//                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
//                    bufferbuilder.vertex(this.x0, this.y1, 0.0D).uv((float)this.x0 / 32.0F, (float)(this.y1 + (int)this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).endVertex();
//                    bufferbuilder.vertex(this.x1, this.y1, 0.0D).uv((float)this.x1 / 32.0F, (float)(this.y1 + (int)this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).endVertex();
//                    bufferbuilder.vertex(this.x1, this.y0, 0.0D).uv((float)this.x1 / 32.0F, (float)(this.y0 + (int)this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).endVertex();
//                    bufferbuilder.vertex(this.x0, this.y0, 0.0D).uv((float)this.x0 / 32.0F, (float)(this.y0 + (int)this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).endVertex();
//                    tesselator.end();
//                }
//
//                int j1 = this.getRowLeft();
//                int k = this.y0 - (int)this.getScrollAmount();
//                if (this.renderHeader) {
//                    this.renderHeader(pose, j1, k, tesselator);
//                }
//
//                this.renderList(pose, j1, k, mouseX, mouseY, frameTime);
//                if (this.renderTopAndBottom) {
//                    RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
//                    RenderSystem.setShaderTexture(0, GuiComponent.BACKGROUND_LOCATION);
//                    RenderSystem.enableDepthTest();
//                    RenderSystem.depthFunc(519);
//                    float f1 = 32.0F;
//                    int l = -100;
//                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
//                    bufferbuilder.vertex(this.x0, this.y0, -100.0D).uv(0.0F, (float)this.y0 / 32.0F).color(64, 64, 64, 255).endVertex();
//                    bufferbuilder.vertex(this.x0 + this.width, this.y0, -100.0D).uv((float)this.width / 32.0F, (float)this.y0 / 32.0F).color(64, 64, 64, 255).endVertex();
//                    bufferbuilder.vertex(this.x0 + this.width, 0.0D, -100.0D).uv((float)this.width / 32.0F, 0.0F).color(64, 64, 64, 255).endVertex();
//                    bufferbuilder.vertex(this.x0, 0.0D, -100.0D).uv(0.0F, 0.0F).color(64, 64, 64, 255).endVertex();
//                    bufferbuilder.vertex(this.x0, this.height, -100.0D).uv(0.0F, (float)this.height / 32.0F).color(64, 64, 64, 255).endVertex();
//                    bufferbuilder.vertex(this.x0 + this.width, this.height, -100.0D).uv((float)this.width / 32.0F, (float)this.height / 32.0F).color(64, 64, 64, 255).endVertex();
//                    bufferbuilder.vertex(this.x0 + this.width, this.y1, -100.0D).uv((float)this.width / 32.0F, (float)this.y1 / 32.0F).color(64, 64, 64, 255).endVertex();
//                    bufferbuilder.vertex(this.x0, this.y1, -100.0D).uv(0.0F, (float)this.y1 / 32.0F).color(64, 64, 64, 255).endVertex();
//                    tesselator.end();
//                    RenderSystem.depthFunc(515);
//                    RenderSystem.disableDepthTest();
//                    RenderSystem.enableBlend();
//                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
//                    RenderSystem.disableTexture();
//                    RenderSystem.setShader(GameRenderer::getPositionColorShader);
//                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
//                    bufferbuilder.vertex(this.x0, this.y0, 0.0D).color(0, 0, 0, 0).endVertex();
//                    bufferbuilder.vertex(this.x1, this.y0, 0.0D).color(0, 0, 0, 0).endVertex();
//                    bufferbuilder.vertex(this.x1, this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
//                    bufferbuilder.vertex(this.x0, this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
//                    bufferbuilder.vertex(this.x0, this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
//                    bufferbuilder.vertex(this.x1, this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
//                    bufferbuilder.vertex(this.x1, this.y1, 0.0D).color(0, 0, 0, 0).endVertex();
//                    bufferbuilder.vertex(this.x0, this.y1, 0.0D).color(0, 0, 0, 0).endVertex();
//                    tesselator.end();
//                }
//
//                int k1 = this.getMaxScroll();
//                if (k1 > 0) {
//                    RenderSystem.disableTexture();
//                    RenderSystem.setShader(GameRenderer::getPositionColorShader);
//                    int l1 = (int)((float)((this.y1 - this.y0) * (this.y1 - this.y0)) / (float)this.getMaxPosition());
//                    l1 = Mth.clamp(l1, 32, this.y1 - this.y0 - 8);
//                    int i2 = (int)this.getScrollAmount() * (this.y1 - this.y0 - l1) / k1 + this.y0;
//                    if (i2 < this.y0) {
//                        i2 = this.y0;
//                    }
//
//                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
//                    bufferbuilder.vertex(i, this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
//                    bufferbuilder.vertex(j, this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
//                    bufferbuilder.vertex(j, this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
//                    bufferbuilder.vertex(i, this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
//                    bufferbuilder.vertex(i, i2 + l1, 0.0D).color(128, 128, 128, 255).endVertex();
//                    bufferbuilder.vertex(j, i2 + l1, 0.0D).color(128, 128, 128, 255).endVertex();
//                    bufferbuilder.vertex(j, i2, 0.0D).color(128, 128, 128, 255).endVertex();
//                    bufferbuilder.vertex(i, i2, 0.0D).color(128, 128, 128, 255).endVertex();
//                    bufferbuilder.vertex(i, i2 + l1 - 1, 0.0D).color(192, 192, 192, 255).endVertex();
//                    bufferbuilder.vertex(j - 1, i2 + l1 - 1, 0.0D).color(192, 192, 192, 255).endVertex();
//                    bufferbuilder.vertex(j - 1, i2, 0.0D).color(192, 192, 192, 255).endVertex();
//                    bufferbuilder.vertex(i, i2, 0.0D).color(192, 192, 192, 255).endVertex();
//                    tesselator.end();
//                }
//
//                this.renderDecorations(pose, mouseX, mouseY);
//                RenderSystem.enableTexture();
//                RenderSystem.disableBlend();
//                UserListWidget.fill(pose, 0, 0, widget.screen.width, widget.screen.height, 0x7f007fff);
            }
            RenderSystem.disableScissor();

//            UserListWidget.fill(pose, this.getRowLeft(), yi, this.getRowLeft() + this.getRowWidth() + 6, yi + yj, 0x7f7f00ff);
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

                synchronized (usersLock) {
                    for (AbstractClientUser user : this.users) {
                        String name = user.getName();
                        if (name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                            this.addEntry(new Entry(this.mc, this, user.getName(), null, user::getSkinLocation, user));
                        }
                    }
                }
            }
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            // TODO: Port this if necessary.
//            switch (keyCode) {
//                case 264:
//                    this.setSelected();
//                    return true;
//                case 265:
//                    this.moveSelection(SelectionDirection.UP);
//                    return true;
//                default:
//                    return false;
//            }
            return false;
        }

        @Environment(EnvType.CLIENT)
        public static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
            public static final int NO_DESC_TEXT_COLOR = FastColor.ARGB32.color(255, 74, 74, 74);
            public static final int DESC_COLOR = FastColor.ARGB32.color(255, 48, 48, 48);
            public static final int TITLE_COLOR = FastColor.ARGB32.color(255, 255, 255, 255);
            public static final int TEXT_COLOR = FastColor.ARGB32.color(140, 255, 255, 255);

            private final Minecraft mc;
            private final @NotNull ListWidget list;
            private final String entryTitle;
            private final Supplier<ResourceLocation> texture;
            private final AbstractClientUser user;
            private final Component description;
            private final List<Button> buttons;
            private float ticksTooltip;

            public Entry(@NotNull Minecraft minecraft, @NotNull ListWidget list, @NotNull String title, @org.jetbrains.annotations.Nullable String description, @NotNull Supplier<@NotNull ResourceLocation> texture, @NotNull AbstractClientUser user) {
                this.mc = minecraft;
                this.list = list;
                this.entryTitle = title;
                this.description = description == null ? Component.empty() : Component.literal(description);
                this.texture = texture;
                this.buttons = ImmutableList.of();
                this.user = user;
            }

            @SuppressWarnings("UnnecessaryLocalVariable")
            public void render(@NotNull GuiGraphics gfx, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                height = list.itemHeight;

                final int i = left + 1;
                final int j = top + (height - ICON_SIZE) / 2;
                final int k = i + 8 + 4 + 2;
                final int l = top + (height - mc.font.lineHeight) / 2;

                RenderSystem.setShaderTexture(0, GUI_LOCATION);

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
                gfx.blit(GUI_LOCATION, sx, ty, es, es, su, tv, es, es, TEX_W, TEX_H); // Top left
                gfx.blit(GUI_LOCATION, mx, ty, iw, es, es, tv, es, es, TEX_W, TEX_H); // Top
                gfx.blit(GUI_LOCATION, ex, ty, es, es, eu, tv, es, es, TEX_W, TEX_H); // Top right
                gfx.blit(GUI_LOCATION, sx, my, es, ih, su, mv, es, es, TEX_W, TEX_H); // Middle left
                gfx.blit(GUI_LOCATION, mx, my, iw, ih, es, mv, es, es, TEX_W, TEX_H); // Middle
                gfx.blit(GUI_LOCATION, ex, my, es, ih, eu, mv, es, es, TEX_W, TEX_H); // Middle right
                gfx.blit(GUI_LOCATION, sx, by, es, es, su, bv, es, es, TEX_W, TEX_H); // Bottom left
                gfx.blit(GUI_LOCATION, mx, by, iw, es, es, bv, es, es, TEX_W, TEX_H); // Bottom
                gfx.blit(GUI_LOCATION, ex, by, es, es, eu, bv, es, es, TEX_W, TEX_H); // Bottom right

                ResourceLocation texture = this.texture.get();
                RenderSystem.setShaderTexture(0, texture);
                gfx.blit(texture, i, j, ICON_SIZE, ICON_SIZE, 8.0F, 8.0F, 8, 8, 64, 64);
                RenderSystem.enableBlend();
                gfx.blit(texture, i, j, ICON_SIZE, ICON_SIZE, 40.0F, 8.0F, 8, 8, 64, 64);
                RenderSystem.disableBlend();
                gfx.drawString(this.mc.font, this.entryTitle, k, l + 1, TITLE_COLOR);

                float f = this.ticksTooltip;

                if (f == this.ticksTooltip) {
                    this.ticksTooltip = 0.0F;
                }
            }

            public @NotNull List<? extends GuiEventListener> children() {
                //      return screen.getEntryButtons();
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

            public @NotNull ListWidget getList() {
                return list;
            }

            @NotNull
            public AbstractClientUser getUser() {
                return user;
            }

            @Override
            public @NotNull List<? extends NarratableEntry> narratables() {
                return new ArrayList<>();
            }

            @Override
            public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
                list.setSelected(this);
                super.mouseClicked(p_94695_, p_94696_, p_94697_);
                return true;
            }
        }
    }
}
