package io.github.ultreon.mods.essentials.client.gui.screens;

import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.ultreon.mods.lib.client.gui.widget.Button;
import io.github.ultreon.mods.essentials.client.UEssentialsClient;
import io.github.ultreon.mods.essentials.client.gui.InternalListScreen;
import io.github.ultreon.mods.essentials.client.input.Keyboard;
import io.github.ultreon.mods.essentials.teleport.ClientTeleportManager;
import io.github.ultreon.mods.essentials.teleport.TeleportRequest;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

public class TpRequestsScreen extends InternalListScreen {
    private static final List<TeleportRequest> teleportRequests = new ArrayList<>();
    private final List<? extends TeleportRequest> users;
    private boolean initialized;
    private Component title = Component.empty();
    private ListWidget listWidget;
    private EditBox searchBox;
    private String searchTerms = "";

    public TpRequestsScreen(List<? extends TeleportRequest> users) {
        super(Component.literal(""));
        this.users = users;
        this.initPageTitle();
        this.minecraft = Minecraft.getInstance();
    }

    public static void handleListStart() {
        teleportRequests.clear();
    }

    public static void handleEntry(TeleportRequest teleportRequest) {
        teleportRequests.add(teleportRequest);
    }

    public static void handleListEnd() {
        Minecraft.getInstance().setScreen(new TpRequestsScreen(teleportRequests));
    }

    private int windowHeight() {
        return Math.max(52, this.height - 128 - 16);
    }

    private int backgroundUnits() {
        return this.windowHeight() / 16;
    }

    private int listBottom() {
        return 80 + this.backgroundUnits() * 16 - 8;
    }

    private int left() {
        return (this.width - 238) / 2;
    }

    @Override
    public void tick() {
        super.tick();
        this.searchBox.tick();
    }

    @Override
    protected void init() {
        if (this.initialized) {
            this.listWidget.updateSize(this.width, this.height, 88, this.listBottom());
        } else {
            this.listWidget = new DefaultListWidget(this, this.users, this.minecraft, this.width, this.height, 88, this.listBottom(), 36);
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
        this.addRenderableWidget(this.listWidget);
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

        if (!this.users.isEmpty()) {
            this.listWidget.render(gfx, mouseX, mouseY, partialTicks);
        } else if (!this.searchBox.getValue().isEmpty()) {
            gfx.drawCenteredString(this.minecraft.font, title, this.width / 2, (78 + this.listBottom()) / 2, -1);
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

        return super.mouseClicked(mouseX, mouseY, button) || this.listWidget.mouseClicked(mouseX, mouseY, button);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean isPauseScreen() {
        return false;
    }

    protected void search(String text) {
        text = text.toLowerCase(Locale.ROOT);
        if (!text.equals(this.searchTerms)) {
            this.listWidget.search(text);
            this.searchTerms = text;
        }

    }

    private void initPageTitle() {
        this.title = generateTitle();
    }

    public Component generateTitle() {
        Component t = Component.translatable("gui." + getResourceId().getNamespace() + "." + getResourceId().getPath().replace("/", ".") + ".title.pre");

        if (this.users.size() != 1) {
            return Component.translatable("gui." + getResourceId().getNamespace() + "." + getResourceId().getPath().replace("/", ".") + ".title.multiple", t, this.users.size());
        } else {
            return Component.translatable("gui." + getResourceId().getNamespace() + "." + getResourceId().getPath().replace("/", ".") + ".title.single", t, this.users.size());
        }
    }

    @NotNull
    public final ResourceLocation getResourceId() {
        return UEssentialsClient.res("tp_requests");
    }

    public abstract static class ListWidget extends ContainerObjectSelectionList<ListWidget.Entry> {
        private final Minecraft mc;
        private final List<? extends TeleportRequest> requests;
        private final TpRequestsScreen screen;

        public ListWidget(TpRequestsScreen screen, List<? extends TeleportRequest> requests, Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight) {
            super(minecraft, width, height, top, bottom, itemHeight);
            this.mc = minecraft;

            ClientPacketListener connection = Minecraft.getInstance().getConnection();

            if (connection != null) {
                for (TeleportRequest request : requests) {
                    if (request == null) {
                        throw new NullPointerException("Teleport request doesn't seem to exist \uD83E\uDD14");
                    }
                    PlayerInfo info = connection.getPlayerInfo(request.getSender());
                    if (info != null) {
                        GameProfile gameProfile = info.getProfile();
                        this.addEntry(new DefaultListWidget.Entry(this.minecraft, screen, gameProfile.getName(), gameProfile.getId().toString(), info::getSkinLocation, request) {
                        });
                    }
                }
            }

            this.requests = requests;
            this.screen = screen;

            this.setRenderBackground(false);
            this.setRenderTopAndBottom(false);
        }

        @Override
        protected int getScrollbarPosition() {
            return this.width / 2 + 105; // 124 default
        }

        @Override
        public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
            double scaleFactor = this.mc.getWindow().getGuiScale();
            RenderSystem.enableScissor((int) ((double) this.getRowLeft() * scaleFactor), (int) ((double) (this.height - this.y1) * scaleFactor), (int) ((double) (this.getScrollbarPosition() + 6) * scaleFactor), (int) ((double) (this.height - (this.height - this.y1) - this.y0 - 4) * scaleFactor));
            super.render(gfx, mouseX, mouseY, partialTicks);
            RenderSystem.disableScissor();
        }

        public void search(String text) {
            this.clearEntries();

            ClientPacketListener connection = Minecraft.getInstance().getConnection();

            if (connection != null) {
                for (TeleportRequest request : requests) {
                    PlayerInfo info = connection.getPlayerInfo(request.getSender());
                    if (info != null) {
                        GameProfile profile = info.getProfile();
//                        this.addEntry(new Entry(this.minecraft, screen, profile.getName(), null, info::getSkinLocation, request));
                        this.addEntry(new DefaultListWidget.Entry(this.minecraft, screen, profile.getName(), profile.getId().toString(), info::getSkinLocation, request));
                    }
                }
            }
        }

        @Environment(EnvType.CLIENT)
        public abstract static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
            private final Minecraft mc;
            @Getter
            private final TpRequestsScreen screen;
            @Getter
            private final String title;
            private final Supplier<ResourceLocation> texture;
            @Getter
            private final TeleportRequest tpRequest;

            private final Button acceptBtn;
            private final Button denyBtn;

            private float ticksTooltip;
            public static final int EMPTY_BG = FastColor.ARGB32.color(255, 74, 74, 74);
            public static final int DEFAULT_BG = FastColor.ARGB32.color(255, 48, 48, 48);
            public static final int TITLE_COLOR = FastColor.ARGB32.color(255, 255, 255, 255);
            public static final int DESC_COLOR = FastColor.ARGB32.color(140, 255, 255, 255);
            private final Component description;
            private final List<Button> buttons;

            public Entry(Minecraft minecraft, TpRequestsScreen screen, String title, @Nullable String description, Supplier<ResourceLocation> texture, TeleportRequest tpReq) {
                this.mc = minecraft;
                this.screen = screen;
                this.title = title;
                this.description = description == null ? Component.empty() : Component.literal(description);
                this.texture = texture;
                this.tpRequest = tpReq;
                this.acceptBtn = new Button(38, 30, 49, 20,
                        Component.literal("Accept"),
                        button -> {
                            if (button.visible) {
                                screen.onClose();
                                ClientTeleportManager.get().accept(tpReq.getSender());
                            }
                        }
                );
                this.denyBtn = new Button(38, 30, 49, 20,
                        Component.literal("Deny"),
                        button -> {
                            if (button.visible) {
                                screen.onClose();
                                ClientTeleportManager.get().deny(tpReq.getSender());
                            }
                        }
                );
                this.acceptBtn.visible = false;
                this.denyBtn.visible = false;
                this.buttons = ImmutableList.of(this.acceptBtn, this.denyBtn);

            }

            public void render(@NotNull GuiGraphics gfx, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                int i = left + 4;
                int j = top + (height - 24) / 2;
                int k = i + 24 + 4;

                acceptBtn.visible = !Keyboard.isHoldingShift();
                denyBtn.visible = Keyboard.isHoldingShift();

                Component description = this.getDescription();
                int l;
                if (Objects.equals(description, Component.empty())) {
                    gfx.fill(left, top, left + width, top + height, EMPTY_BG);
                    l = top + (height - 9) / 2;
                } else {
                    gfx.fill(left, top, left + width, top + height, DEFAULT_BG);
                    l = top + (height - (9 + 9)) / 2;
                    gfx.drawString(this.mc.font, description, k, l + 12, DESC_COLOR);
                }

                ResourceLocation texture = this.texture.get();
                gfx.blit(texture, i, j, 24, 24, 8.0F, 8.0F, 8, 8, 64, 64);
                RenderSystem.enableBlend();
                gfx.blit(texture, i, j, 24, 24, 40.0F, 8.0F, 8, 8, 64, 64);
                RenderSystem.disableBlend();
                gfx.drawString(this.mc.font, this.title, k, l, TITLE_COLOR);

                float f = this.ticksTooltip;
                this.acceptBtn.setX(left + width - this.acceptBtn.getWidth() - 4);
                this.acceptBtn.setY(top + height / 2 - this.acceptBtn.getHeight());
                this.acceptBtn.render(gfx, mouseX, mouseY, partialTicks);
                if (f == this.ticksTooltip) {
                    this.ticksTooltip = 0.0F;
                }
            }

            public @NotNull List<? extends GuiEventListener> children() {
                return this.buttons;
            }

            private Component getDescription() {
                return description;
            }

        }
    }

    public static class DefaultListWidget extends ListWidget {
        public DefaultListWidget(TpRequestsScreen screen, List<? extends TeleportRequest> users, Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight) {
            super(screen, users, minecraft, width, height, top, bottom, itemHeight);
        }

        public static class Entry extends ListWidget.Entry {
            public Entry(Minecraft minecraft, TpRequestsScreen screen, String title, @Nullable String description, Supplier<ResourceLocation> texture, TeleportRequest user) {
                super(minecraft, screen, title, description, texture, user);
            }

            @NotNull
            @Override
            public List<? extends NarratableEntry> narratables() {
                return new ArrayList<>();
            }
        }
    }
}
