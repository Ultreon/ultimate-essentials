package io.github.ultreon.mods.essentials.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ultreon.mods.lib.client.gui.screen.BaseScreen;
import io.github.ultreon.mods.essentials.client.UEssentialsClient;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.economy.BuyPacket;
import io.github.ultreon.mods.essentials.network.economy.ListShopPagePacket;
import io.github.ultreon.mods.essentials.network.economy.SetBalancePacket;
import io.github.ultreon.mods.essentials.shop.ShopItem;
import io.github.ultreon.mods.essentials.shop.ShopPage;
import io.github.ultreon.mods.essentials.user.AbstractClientUser;
import io.github.ultreon.mods.essentials.user.LocalUser;
import io.github.ultreon.mods.essentials.util.MoneyUtils;
import it.unimi.dsi.fastutil.ints.Int2ReferenceArrayMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class ShopScreen extends BaseScreen {
    private static final ResourceLocation GUI_TEXTURE = UEssentialsClient.res("textures/gui/shop.png");
    private static final ResourceLocation LOADING_TEXTURE = UEssentialsClient.res("textures/gui/shop_loading.png");
    private static final ResourceLocation GUI_ICONS = UEssentialsClient.res("textures/gui/icons.png");
    private static final Minecraft mc = Minecraft.getInstance();
    private static final String COMPASS_ANIM = "textures/item/compass_";
    private static final int ITEM_SPACING = 3;
    private static final int GUI_PADDING = 6;
    private static final ResourceLocation SHOP_ICONS = UEssentialsClient.res("textures/gui/icons/shop.png");

    private final AbstractClientUser user;

    private boolean loaded = false;
    private boolean loading = false;
    private int hoveredSlot = -1;
    private int loadingPage;
    private int pages;
    private int page;
    private double balance;
    private float ticks = 0f;
    private MutableComponent title1;

    private final Int2ReferenceArrayMap<Point> slotPositions = new Int2ReferenceArrayMap<>();
    private final Int2ReferenceArrayMap<ShopItem> slots = new Int2ReferenceArrayMap<>();
    private Int2ReferenceArrayMap<List<ShopItem>> items = new Int2ReferenceArrayMap<>();

    private final Object slotLock = new Object();
    private final Object itemLock = new Object();
    private final Logger logger = LogManager.getLogger("UltimateEssentials");

    public ShopScreen(AbstractClientUser user) {
        super(Component.translatable("gui.ultimate_essentials.shop.title", "???"));

        this.user = user;
    }

    private int width() {
        return 256;
    }

    private int height() {
        return 213;
    }

    private int left() {
        return (this.width - width()) / 2;
    }

    @SuppressWarnings("unused")
    private int right() {
        return (this.width + width()) / 2;
    }

    private int top() {
        return (this.height - height()) / 2;
    }

    @SuppressWarnings("unused")
    private int bottom() {
        return (this.height + height()) / 2;
    }

    public void renderBackground(@NotNull GuiGraphics gfx) {
        super.renderBackground(gfx);
        int i = this.left();
        gfx.blit(this.page < 0 || this.page >= pages || !loaded ? LOADING_TEXTURE : GUI_TEXTURE, i, this.top(), 0, 0, width(), height());
    }

    @Override
    public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(gfx);

        super.render(gfx, mouseX, mouseY, partialTicks);

        if (!loaded) {
            renderLoadingPhase(gfx, partialTicks);
            return;
        }

        RenderSystem.setShaderTexture(0, SHOP_ICONS);

        // Errors that disallows navigation
        if (renderForEmptyShop(gfx)) return;

        // Navigation
        renderNextBtn(gfx, mouseX, mouseY);
        renderBackBtn(gfx, mouseX, mouseY);

        // Errors that allows navigation.
        if (renderForInvalidPage(gfx)) return;

        renderTitleBar(gfx);

        renderShopItems(gfx, mouseX, mouseY);
    }

    private void renderShopItems(@NotNull GuiGraphics gfx, int mouseX, int mouseY) {
        final int a = 16;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        int i = 0;

        hoveredSlot = -1;

        synchronized (itemLock) {
            List<ShopItem> fullPage = items.get(this.page);
            int x = 0;
            int y = 0;

            if (fullPage == null) {
                return;
            }

            int tempHoveredSlot = -1;
            int tempSlotX = -1;
            int tempSlotY = -1;
            ShopItem tempSlotItem = null;

            gfx.pose().pushPose();
            RenderSystem.enableDepthTest();

            for (ShopItem item : fullPage) {
                int slot = i;
                int itemX = left() + GUI_PADDING + (x * (a + ITEM_SPACING)) + 38;
                int itemY = top() + GUI_PADDING + 18 + 2 + (y * (16 + ITEM_SPACING)) + GUI_PADDING + 2 + GUI_PADDING - 1;

                if (!item.isEmpty()) {
                    ItemStack stack = item.getItem();

                    RenderSystem.disableDepthTest();
                    gfx.blit(GUI_TEXTURE, itemX - 1, itemY - 1, 18, 18, 0, 214, 18, 18, 256, 257);
                    RenderSystem.enableDepthTest();

                    gfx.renderItem(stack, itemX, itemY);

                    if (this.isPointInRegion(itemX, itemY, 16, 16, mouseX, mouseY)) {
                        tempHoveredSlot = slot;
                        tempSlotX = itemX;
                        tempSlotY = itemY;
                        tempSlotItem = item;

                        if (item.canBuyWith(balance)) {
                            RenderSystem.disableDepthTest();
                            RenderSystem.colorMask(true, true, true, false);
                            int slotColor = this.getSlotColor();
                            gfx.fillGradient(itemX, itemY, itemX + 16, itemY + 16, slotColor, slotColor);
                            RenderSystem.colorMask(true, true, true, true);
                            RenderSystem.enableDepthTest();
                        }
                    }

                    if (!item.canBuyWith(balance)) {
                        RenderSystem.disableDepthTest();
                        RenderSystem.colorMask(true, true, true, false);
                        int slotColor = 0x40ff0000;
                        gfx.fillGradient(itemX, itemY, itemX + 16, itemY + 16, slotColor, slotColor);
                        RenderSystem.colorMask(true, true, true, true);
                        RenderSystem.enableDepthTest();
                    }
                } else {
                    gfx.blit(GUI_TEXTURE, itemX - 1, itemY - 1, 18, 18, 18, 214, 18, 18, 256, 257);
                }

                x++;
                if (x == ShopPage.COLUMNS) {
                    x = 0;
                    y++;
                }
                i++;
            }

            if (y > ShopPage.ROWS || (x > 0 && y == ShopPage.ROWS)) {
                logger.error("Shop page row overflow: " + y);
            }

            this.hoveredSlot = tempHoveredSlot;
            if (hoveredSlot >= 0) {
                renderTooltip(gfx, tempSlotItem, tempSlotX + 9, tempSlotY + 15);
            }

            if (isPointBetween(mouseX, mouseY, right() - GUI_PADDING - 16 - 9 - 5, top() + GUI_PADDING + 3, 16, 8)) {
                gfx.renderTooltip(this.font, List.of(
                        Component.literal("Navigation").withStyle(ChatFormatting.BLUE),
                        Component.literal("Page ").withStyle(ChatFormatting.GRAY)
                                .append(Component.literal(Integer.toString(page + 1)).withStyle(ChatFormatting.WHITE))
                                .append(Component.literal(" of ").withStyle(ChatFormatting.GRAY))
                                .append(Component.literal(Integer.toString(pages)).withStyle(ChatFormatting.WHITE))
                                .append(Component.literal(".").withStyle(ChatFormatting.GRAY))
                        )
                , Optional.empty(), (right() - GUI_PADDING - 16 - 9 - 5 - 7), (top() + GUI_PADDING + 3) + 8 + 17);
            }

            gfx.pose().popPose();
        }
    }

    private boolean renderForInvalidPage(@NotNull GuiGraphics gfx) {
        if (this.page < 0 || this.page >= pages) {
            RenderSystem.setShaderTexture(0, GUI_ICONS);
            gfx.blit(GUI_ICONS, this.width / 2 - 8, this.height / 2 - 24, 16, 16, 16, 16, 16, 16, 256, 256);

            drawFlatCenteredString(gfx, mc.font, Component.translatable("gui.ultimate_essentials.shop.error.invalid_page"), this.width / 2, this.height / 2 + 5, 0xbbbbbb);
            return true;
        }
        return false;
    }

    private void renderTitleBar(@NotNull GuiGraphics gfx) {
        ResourceLocation locationSkin = user.getSkinLocation();

        gfx.blit(locationSkin, left() + GUI_PADDING + 1 - 2, top() + GUI_PADDING + 1 - 2, 16, 16, 8.0F, 8.0F, 8, 8, 64, 64);
        RenderSystem.enableBlend();
        gfx.blit(locationSkin, left() + GUI_PADDING + 1 - 2, top() + GUI_PADDING + 1 - 2, 16, 16, 40.0F, 8.0F, 8, 8, 64, 64);
        RenderSystem.disableBlend();

        drawFlatString(gfx, mc.font, title1, left() + GUI_PADDING + 18 + 2, top() + GUI_PADDING + 1 + 4 - 1, 0xffffff);
    }

    private boolean renderForEmptyShop(@NotNull GuiGraphics gfx) {
        if (pages == 0) {
            gfx.blit(GUI_ICONS, this.width / 2 - 8, this.height / 2 - 24, 16, 16, 16, 16, 16, 16, 256, 256);

            drawFlatCenteredString(gfx, mc.font, Component.translatable("gui.ultimate_essentials.shop.error.empty"), this.width / 2, this.height / 2 + 5, 0xbbbbbb);
            return true;
        }
        return false;
    }

    private void renderLoadingPhase(@NotNull GuiGraphics gfx, float partialTicks) {
        ticks += partialTicks;
        final float loadingIconFPT = 0.5f; // FPT = Frames per tick
        final float dotsTextFPT = 10; // Dots per tick

        int frame = (int) Math.abs((ticks / loadingIconFPT) % 32);
        int titleFrame = (int) Math.abs((ticks / dotsTextFPT) % 4);

        String ext = (frame <= 9 ? "0" + frame : "" + frame) + ".png";

        if (partialTicks >= 1f) {
            System.out.println(partialTicks);
        }

        gfx.blit(new ResourceLocation(COMPASS_ANIM + ext), this.width / 2 - 8, this.height / 2 - 24, 16, 16, 0, 0, 16, 16, 16, 16);

        if (!loading) {
            drawFlatCenteredString(gfx, mc.font, Component.translatable("gui.ultimate_essentials.shop.waiting_for_server." + titleFrame), this.width / 2, this.height / 2 + 5, 0xbbbbbb);
        } else {
            drawFlatCenteredString(gfx, mc.font, Component.translatable("gui.ultimate_essentials.shop.loading." + titleFrame), this.width / 2, this.height / 2 + 5, 0xbbbbbb);
            if (pages > 0) {
                drawFlatCenteredString(gfx, mc.font, Component.translatable("guiho.ultimate_essentials.shop.loading.message", loadingPage, pages), this.width / 2, this.height / 2 + 5 + 11, 0xbbbbbb);
            }
        }
    }

    private void renderNextBtn(@NotNull GuiGraphics gfx, int mouseX, int mouseY) {
        if (page >= pages - 1)
            gfx.blit(SHOP_ICONS, right() - GUI_PADDING - 8 - 9 - 5, top() + GUI_PADDING + 3, 8, 8, 8, 16, 8, 8, 32, 32);
        else if (isHoveringNext(mouseX, mouseY))
            gfx.blit(SHOP_ICONS, right() - GUI_PADDING - 8 - 9 - 5, top() + GUI_PADDING + 3, 8, 8, 24, 16, 8, 8, 32, 32);
        else
            gfx.blit(SHOP_ICONS, right() - GUI_PADDING - 8 - 9 - 5, top() + GUI_PADDING + 3, 8, 8, 16, 16, 8, 8, 32, 32);
    }

    private void renderBackBtn(@NotNull GuiGraphics gfx, int mouseX, int mouseY) {
        if (page <= 0)
            gfx.blit(SHOP_ICONS, right() - GUI_PADDING - 16 - 9 - 5, top() + GUI_PADDING + 3, 8, 8, 8, 24, 8, 8, 32, 32);
        else if (isHoveringPrev(mouseX, mouseY))
            gfx.blit(SHOP_ICONS, right() - GUI_PADDING - 16 - 9 - 5, top() + GUI_PADDING + 3, 8, 8, 24, 24, 8, 8, 32, 32);
        else
            gfx.blit(SHOP_ICONS, right() - GUI_PADDING - 16 - 9 - 5, top() + GUI_PADDING + 3, 8, 8, 16, 24, 8, 8, 32, 32);
    }

    private void renderTooltip(GuiGraphics gfx, ShopItem item, int x, int y) {
        assert this.minecraft != null;
        List<Component> components = getTooltipFromItem(this.minecraft, item.getItem());
        if (components.isEmpty()) {
            components.add(Component.literal(MoneyUtils.format(item.getPrice())).withStyle(item.canBuyWith(balance) ? ChatFormatting.GREEN :  ChatFormatting.RED));
        } else {
            components.add(1, Component.literal(MoneyUtils.format(item.getPrice())).withStyle(item.canBuyWith(balance) ? ChatFormatting.GREEN :  ChatFormatting.RED));
        }

        gfx.renderTooltip(this.font, components, item.getItem().getTooltipImage(), x, y);
    }

    @Override
    public Vec2 getCloseButtonPos() {
        return new Vec2( right() - 9 - 5, top() + 13 - 4);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) return true;

        SoundManager soundMan = Minecraft.getInstance().getSoundManager();
        itemBlock:
        if (hoveredSlot >= 0) {
            synchronized (slotLock) {
                ShopItem shopItem = slots.get(hoveredSlot);
                if (shopItem == null) {
                    break itemBlock;
                }

                if (!shopItem.isEmpty()) {
                    UUID uniqueId = shopItem.getUUID();
                    Networking.get().sendToServer(new BuyPacket(uniqueId));
                }
            }
        }

        if (page < pages - 1) {
            if (isHoveringNext(mouseX, mouseY)) {
                soundMan.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                changePage(page + 1);
            }
        }

        if (page > 0) {
            if (isHoveringPrev(mouseX, mouseY)) {
                soundMan.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                changePage(page - 1);
            }
        }

        return true;
    }

    private boolean isHoveringNext(double mouseX, double mouseY) {
        return mouseX >= right() - GUI_PADDING - 8 - 9 - 5 && mouseX <= right() - GUI_PADDING - 9 - 5 &&
                mouseY >= top() - GUI_PADDING + 15 && mouseY <= top() - GUI_PADDING + 23;
    }

    private boolean isHoveringPrev(double mouseX, double mouseY) {
        return mouseX >= right() - GUI_PADDING - 16 - 9 - 5 && mouseX <= right() - GUI_PADDING - 8 - 9 - 5 &&
                mouseY >= top() - GUI_PADDING + 15 && mouseY <= top() - GUI_PADDING + 23;
    }

    @Override
    protected void init() {
        Networking.get().sendToServer(new ListShopPagePacket());
    }

    private void changePage(int page) {
        if (this.page == page) return;

        this.page = Mth.clamp(page, 0, pages);

        postInit();
    }

    public boolean isSlotSelected(int slot, double mouseX, double mouseY) {
        Point point = slotPositions.get(slot);

        if (point == null) {
            return false;
        }

        return this.isPointInRegion(point.x, point.y, 16, 16, mouseX, mouseY);
    }

    @SuppressWarnings("SameParameterValue")
    private boolean isPointInRegion(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= (double) (x - 1) && mouseX < (double) (x + width + 1) && mouseY >= (double) (y - 1) && mouseY < (double) (y + height + 1);
    }

    public static void drawFlatCenteredString(GuiGraphics gfx, Font font, String text, int x, int y, int color) {
        gfx.drawString(font, text, x - font.width(text) / 2, y, color);
    }

    public static void drawFlatCenteredString(GuiGraphics gfx, Font font, Component component, int x, int y, int color) {
        FormattedCharSequence charSequence = component.getVisualOrderText();
        gfx.drawString(font, component, x - font.width(charSequence) / 2, y, color);
    }

    public static void drawFlatString(GuiGraphics gfx, Font font, String text, int x, int y, int color) {
        gfx.drawString(font, text, x, y, color);
    }

    public static void drawFlatString(GuiGraphics gfx, Font font, Component text, int x, int y, int color) {
        gfx.drawString(font, text, x, y, color);
    }

    public void open() {
        Minecraft.getInstance().setScreen(new ShopScreen(LocalUser.get()));
    }

    public void handlePacket(ListShopPagePacket.Begin pak) {
        this.loading = true;
        this.loadingPage = 0;
        this.pages = pak.getSize();
        this.balance = pak.getBalance();

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        this.title1 = Component.translatable("gui.ultimate_essentials.shop.title", df.format(balance).replaceAll(",00", ",-"));
        this.items = new Int2ReferenceArrayMap<>();
    }

    public void handlePacket(ListShopPagePacket.Page pak) {
        List<ShopItem> items = pak.getItems();
        int page = pak.getPage();
        this.loadingPage++;
        this.items.put(page, items);
    }

    @SuppressWarnings("unused")
    public void handlePacket(ListShopPagePacket.End pak) {
        loading = false;
        loaded = true;

        postInit();
    }

    private synchronized void postInit() {
        synchronized (slotLock) {
            synchronized (itemLock) {
                List<ShopItem> fullPage = items.get(this.page);

                if (fullPage == null) {
                    return;
                }

                slotPositions.clear();
                slots.clear();

                final int topSpacing = 80;
                int x = 0;
                int y = 0;
                int i = 0;
                for (ShopItem item : fullPage) {
                    int slot = i;
                    int itemX = left() + GUI_PADDING + (x * (topSpacing + ITEM_SPACING));
                    int itemY = top() + GUI_PADDING + 18 + 2 + (y * (16 + ITEM_SPACING)) + GUI_PADDING + 2 + GUI_PADDING - 1;

                    slotPositions.put(slot, new Point(itemX, itemY));

                    slots.put(slot, item);

                    x++;
                    if (x == 3) {
                        x = 0;
                        y++;
                    }
                    i++;
                }
            }
        }
    }

    public int getSlotColor() {
        return 0x80ffffff;
    }

    public void handlePacket(SetBalancePacket pak) {
        balance = pak.getAmount();

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        title1 = Component.translatable("gui.ultimate_essentials.shop.title", df.format(balance).replaceAll(",00", ",-"));
    }
}
