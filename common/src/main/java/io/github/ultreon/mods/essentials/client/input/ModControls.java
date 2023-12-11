package io.github.ultreon.mods.essentials.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import io.github.ultreon.mods.essentials.client.gui.screens.MainScreen;
import io.github.ultreon.mods.essentials.client.gui.screens.ShopScreen;
import io.github.ultreon.mods.essentials.user.LocalUser;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * The ModControls class provides key mappings for various actions in the mod.
 * It also provides a method to register these key mappings and a method to handle
 * client tick events.
 *
 * @author XyperCode
 */
public class ModControls {
    public static final List<KeyMapping> KEY_MAPPINGS = new ArrayList<>();
    public static final KeyMapping MAIN_SCREEN = add(new KeyMapping("key.ultimate_essentials.main_screen", InputConstants.KEY_PERIOD, "key.categories.ultimate_essentials"));
    public static final KeyMapping SHOP = add(new KeyMapping("key.ultimate_essentials.shop", InputConstants.KEY_Z, "key.categories.ultimate_essentials"));

    @ApiStatus.Internal
    public static void register() {
        for (KeyMapping keyMapping : KEY_MAPPINGS) {
            KeyMappingRegistry.register(keyMapping);
        }
    }

    private static KeyMapping add(KeyMapping keyBinding) {
        KEY_MAPPINGS.add(keyBinding);
        return keyBinding;
    }

    @ApiStatus.Internal
    public static void clientTick(Minecraft minecraft) {
        if (ModControls.MAIN_SCREEN.consumeClick()) {
            minecraft.setScreen(new MainScreen());
        }
        if (ModControls.SHOP.consumeClick()) {
            minecraft.setScreen(new ShopScreen(LocalUser.get()));
        }
    }
}
