package io.github.ultreon.mods.essentials.client.gui.screens.teleportation;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.ultreon.mods.lib.client.gui.widget.Button;
import io.github.ultreon.mods.essentials.client.gui.InternalListScreen;
import io.github.ultreon.mods.essentials.client.gui.widget.ImageButton;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.warps.DeleteWarpPacket;
import io.github.ultreon.mods.essentials.network.warps.ListWarpsPacket;
import io.github.ultreon.mods.essentials.network.warps.TeleportWarpPacket;
import io.github.ultreon.mods.essentials.user.LocalUser;
import io.github.ultreon.mods.essentials.user.Permissions;
import io.github.ultreon.mods.essentials.warps.WarpReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class WarpsScreen extends InternalListScreen {
    private static final List<WarpReference> warps = new ArrayList<>();
    private static final Component SEARCH_HINT = Component.literal("Search");
    private ListWidget list;
    private EditBox searchBox;
    private String searchTerms = "";
    private int homeCount = -1;
    private boolean initialized;
    private final List<WarpReference> homes;

    public WarpsScreen(List<WarpReference> homes) {
        super(Component.empty());
        this.homes = homes;
        this.initPageTitle();
    }

    public static void handleListStart() {
        warps.clear();
    }

    public static void handleWarp(WarpReference home) {
        WarpsScreen.warps.add(home);
    }

    public static void handleListEnd() {
        Minecraft.getInstance().setScreen(new WarpsScreen(warps));
    }

    public static void open() {
        Networking.get().sendToServer(new ListWarpsPacket());
    }

    private int windowHeight() {
        return Math.max(52, this.height - 128 - 16);
    }

    private int backgroundUnits() {
        return this.windowHeight() / 16;
    }

    private int listEnd() {
        return 80 + this.backgroundUnits() * 16 - 8;
    }

    private int left() {
        return (this.width - 238) / 2;
    }

    public void tick() {
        super.tick();
        if (searchBox != null) {
            this.searchBox.tick();
        }
    }

    protected void init() {
        if (this.initialized) {
            this.list.updateSize(this.width, this.height, 88, this.listEnd());
        } else {
            this.list = new ListWidget(this, this.homes, this.minecraft, this.width, this.height, 88, this.listEnd(), 36);
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
        RenderSystem.setShaderTexture(0, GUI_LOCATION);
        gfx.blit(GUI_LOCATION, i, 64, 1, 1, 236, 8);
        int j = this.backgroundUnits();

        for (int k = 0; k < j; ++k) {
            gfx.blit(GUI_LOCATION, i, 72 + 16 * k, 1, 10, 236, 16);
        }

        gfx.blit(GUI_LOCATION, i, 72 + 16 * j, 1, 27, 236, 8);
        gfx.blit(GUI_LOCATION, i + 10, 76, 243, 1, 12, 12);
    }

    public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        this.initPageTitle();
        Objects.requireNonNull(this.minecraft);
        this.renderBackground(gfx);
        gfx.drawString(this.minecraft.font, this.title, this.left() + 8, 35, -1);

        if (!this.homes.isEmpty() && this.list != null) {
            this.list.render(gfx, mouseX, mouseY, partialTicks);
        } else if (searchBox != null && !this.searchBox.getValue().isEmpty()) {
            gfx.drawCenteredString(this.minecraft.font, title, this.width / 2, (78 + this.listEnd()) / 2, -1);
        }

        if (searchBox != null && (!this.searchBox.isFocused() && this.searchBox.getValue().isEmpty())) {
            gfx.drawString(this.minecraft.font, SEARCH_HINT, this.searchBox.getX(), this.searchBox.getY(), -1);
        } else if (searchBox != null) {
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

    private void initPageTitle() {
        if (this.homeCount != homes.size()) {
            LocalPlayer player = Minecraft.getInstance().player;
            String s = "Homes of " + (player != null ? player.getName().getString() : "Void");

            if (homes.size() != 1) {
                this.title = Component.translatable("gui.ultimate_essentials.homeList.title.multiple", s, homes.size());
            } else {
                this.title = Component.translatable("gui.ultimate_essentials.homeList.title.single", s, homes.size());
            }

            this.homeCount = homes.size();
        }

    }

    public static class ListWidget extends ContainerObjectSelectionList<ListWidget.Entry> {
        private final Minecraft mc;
        private final List<WarpReference> warps;
        private final WarpsScreen screen;

        public ListWidget(WarpsScreen screen, List<WarpReference> warps, Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight) {
            super(minecraft, width, height, top, bottom, itemHeight);
            this.mc = minecraft;

            for (WarpReference warp : warps) {
                this.addEntry(new Entry(this.mc, screen, warp.title(), warp.description(), warp));
            }

            this.warps = warps;
            this.screen = screen;

            this.setRenderBackground(false);
            this.setRenderTopAndBottom(false);
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
            this.clearEntries();

            for (WarpReference warp : this.warps) {
                if (StringUtils.contains(warp.title(), text)) {
                    this.addEntry(new Entry(this.mc, screen, warp.title(), warp.description(), warp));
                }
            }
        }

        @Environment(EnvType.CLIENT)
        public static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
            private final Minecraft mc;
            private final List<GuiEventListener> buttons;
            private final String entryTitle;
            private ImageButton teleportBtn;
            private ImageButton deleteBtn = null;
            private float ticksTooltip;
            public static final int field_244617_b = FastColor.ARGB32.color(255, 74, 74, 74);
            public static final int field_244618_c = FastColor.ARGB32.color(255, 48, 48, 48);
            public static final int field_244619_d = FastColor.ARGB32.color(255, 255, 255, 255);
            public static final int field_244741_e = FastColor.ARGB32.color(140, 255, 255, 255);
            private final Component description;

            public Entry(Minecraft minecraft, WarpsScreen screen, String title, String description, WarpReference warp) {
                this.mc = minecraft;
                this.entryTitle = title;
                this.description = Component.literal(description);
                this.teleportBtn = new ImageButton(4, 28, 16, 16, 64, 0, 16, LIST_ICONS, 256, 256, (p_244751_4_) -> {
                    screen.onClose();
                    if (teleportBtn.visible) {
                        Networking.get().sendToServer(new TeleportWarpPacket(warp));
                    }
                }, Component.literal("Teleport"));
                if (LocalUser.get().hasPermission(Permissions.DELETE_WARPS)) {
                    this.deleteBtn = new ImageButton(20, 28, 16, 16, 48, 0, 16, LIST_ICONS, 256, 256, (p_244751_4_) -> {
                        screen.onClose();
                        if (deleteBtn.visible) {
                            Networking.get().sendToServer(new DeleteWarpPacket(warp));
                        }
                    }, Component.literal("Delete"));
                    this.deleteBtn.visible = false;
                }
                this.teleportBtn.visible = false;
                if (deleteBtn != null) {
                    this.buttons = ImmutableList.of(this.teleportBtn, this.deleteBtn);
                } else {
                    this.buttons = ImmutableList.of(this.teleportBtn);
                }
            }

            public void render(@NotNull GuiGraphics gfx, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                int i = left + 4;
                Objects.requireNonNull(teleportBtn).visible = true;
                if (deleteBtn != null) deleteBtn.visible = true;

                Component description = this.getDescription();
                int l;
                if (description == Component.empty()) {
                    gfx.fill(left, top, left + width, top + height, field_244617_b);
                    l = top + (height - 9) / 2;
                } else {
                    gfx.fill(left, top, left + width, top + height, field_244618_c);
                    l = top + (height - (9 + 9)) / 2;
                    gfx.drawString(this.mc.font, description, i, l + 12, field_244741_e);
                }

                gfx.drawString(this.mc.font, this.entryTitle, i, l, field_244619_d);
                float f = this.ticksTooltip;
                this.teleportBtn.setX(deleteBtn != null ? left + (width - this.teleportBtn.getWidth() - this.deleteBtn.getWidth() - 4) : left + (width - this.teleportBtn.getWidth() - 4));
                this.teleportBtn.setY((top + (height - teleportBtn.getHeight()) / 2));
                this.teleportBtn.render(gfx, mouseX, mouseY, partialTicks);
                if (deleteBtn != null) {
                    this.deleteBtn.setX(left + (width - this.deleteBtn.getWidth() - 4));
                    this.deleteBtn.setY((top + (height - deleteBtn.getHeight()) / 2));
                    this.deleteBtn.render(gfx, mouseX, mouseY, partialTicks);
                }
                if (f == this.ticksTooltip) {
                    this.ticksTooltip = 0.0F;
                }
            }

            public @NotNull List<? extends GuiEventListener> children() {
                return this.buttons;
            }

            public String getTitle() {
                return this.entryTitle;
            }

            private Component getDescription() {
                return description;
            }

            @NotNull
            @Override
            public List<? extends NarratableEntry> narratables() {
                return new ArrayList<>();
            }
        }
    }
}
