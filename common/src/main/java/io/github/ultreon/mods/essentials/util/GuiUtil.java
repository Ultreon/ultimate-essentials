package io.github.ultreon.mods.essentials.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringDecomposer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GuiUtil {
    private static List<String> wrapContent(List<String> lines, int maxWidth) {
        Font font = Minecraft.getInstance().font;
        List<String> ret = new ArrayList<>();
        for (String line : lines) {
            if (line == null) {
                ret.add(null);
                continue;
            }

            Component chat = Component.literal(line);
            if (maxWidth >= 0) {
                ret.addAll(font.getSplitter().splitLines(chat, maxWidth, Style.EMPTY).stream().map(StringDecomposer::getPlainText).toList());
            }
        }
        return ret;
    }

    public static void renderWrappedText(GuiGraphics gfx, String text, int x, int y, int maxWidth) {
        Font font = Minecraft.getInstance().font;
        for (String line : wrapContent(Collections.singletonList(text), maxWidth)) {
            if (line != null) {
                gfx.drawString(font, line, x, y, 0xFFFFFF);
            }
            y += font.lineHeight;
        }
    }

    public static void renderCenteredWrappedText(GuiGraphics gfx, String text, int x, int y, int maxWidth, int color) {
        Font font = Minecraft.getInstance().font;
        for (String line : wrapContent(Collections.singletonList(text), maxWidth)) {

            if (line != null) {
                gfx.drawString(font, line, (int) (x - font.width(line) / 2f), y, color);
            }
            y += font.lineHeight;
        }
    }
}
