package io.github.ultreon.mods.essentials.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import lombok.experimental.UtilityClass;
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
public class Keyboard {
    private static final long WINDOW = Minecraft.getInstance().getWindow().getWindow();

    public static boolean isHoldingShift() {
        return InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    public static boolean isHoldingCtrl() {
        return InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_CONTROL) || InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_CONTROL);
    }

    public static boolean isHoldingAlt() {
        return InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_ALT) || InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_ALT);
    }
}
