package io.github.ultreon.mods.essentials.util;

import com.mojang.blaze3d.platform.InputConstants;
import lombok.experimental.UtilityClass;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

/**
 * Keyboard helper.
 * Check for holding shift, ctrl or alt.
 *
 * @author XyperCode
 */
@SuppressWarnings("unused")
@UtilityClass
public class KeyboardHelper {
    private static final long WINDOW = Minecraft.getInstance().getWindow().getWindow();

    @Environment(EnvType.CLIENT)
    public static boolean isHoldingShift() {
        return InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Environment(EnvType.CLIENT)
    public static boolean isHoldingCtrl() {
        return InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_CONTROL) || InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_CONTROL);
    }

    @Environment(EnvType.CLIENT)
    public static boolean isHoldingAlt() {
        return InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_ALT) || InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_ALT);
    }
}
