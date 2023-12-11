package io.github.ultreon.mods.essentials.util;

import com.mojang.realmsclient.RealmsMainScreen;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;

import java.util.Objects;

@SuppressWarnings("UnusedReturnValue")
@UtilityClass
public final class LevelUtils {
    public static boolean saveLevelThenOpenTitle() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null) return false;

        boolean flag = mc.isLocalServer();
        boolean flag1 = mc.isConnectedToRealms();
        clearLevelAndDisconnect(mc, flag);
        gotoDefaultReturnScreen(mc, flag, flag1);

        return true;
    }

    public static boolean saveLevelThen(Runnable runnable) {
        if (saveLevel0()) return false;

        runnable.run();
        return true;
    }

    public static boolean saveLevelThenOpen(Screen screen) {
        if (saveLevel0()) return false;

        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(screen);
        return true;
    }

    public static void saveLevelThenQuitGame() {
        saveLevelThen(() -> Minecraft.getInstance().stop());
    }

    private static void clearLevelAndDisconnect(Minecraft mc, boolean flag) {
        Objects.requireNonNull(mc.level).disconnect();
        if (flag) {
            mc.clearLevel(new GenericDirtMessageScreen(Component.translatable("menu.savingLevel")));
        } else {
            mc.clearLevel();
        }
    }

    private static boolean saveLevel0() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null) return true;

        boolean flag = mc.isLocalServer();
        clearLevelAndDisconnect(mc, flag);
        return false;
    }

    private static void gotoDefaultReturnScreen(Minecraft mc, boolean flag, boolean flag1) {
        TitleScreen titlescreen = new TitleScreen();
        if (flag) {
            mc.setScreen(titlescreen);
        } else if (flag1) {
            mc.setScreen(new RealmsMainScreen(titlescreen));
        } else {
            mc.setScreen(new JoinMultiplayerScreen(titlescreen));
        }
    }
}
