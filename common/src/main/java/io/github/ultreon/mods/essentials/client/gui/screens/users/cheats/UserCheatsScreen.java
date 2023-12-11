package io.github.ultreon.mods.essentials.client.gui.screens.users.cheats;

import com.ultreon.mods.lib.client.gui.widget.BaseButton;
import io.github.ultreon.mods.essentials.client.gui.screens.TextInsertScreen;
import io.github.ultreon.mods.essentials.client.gui.screens.users.UserScreen;
import io.github.ultreon.mods.essentials.user.AbstractClientUser;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class UserCheatsScreen extends UserScreen {
    private static final Pattern ALL_DIGITS = Pattern.compile("^\\d+$");

    public UserCheatsScreen(@NotNull AbstractClientUser user) {
        super(Component.translatable("gui.ultimate_essentials.doUser.title", user.getName()), user);

        addButtonRow(Component.literal("Full Feed"), (button) -> {
            if (!button.visible) return;
            if (!button.active) return;
            user.fullFeed();
        }, Component.literal("Full Heal"), (button) -> {
            if (!button.visible) return;
            if (!button.active) return;
            user.fullHeal();
        });

        addButtonRow(
                Component.literal("God Mode: ").append(user.hasGodMode() ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF), this::toggleGodMode,
                Component.literal("Fly Mode: ").append(user.hasFlyMode() ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF), this::toggleFlyMode
        );

        addButtonRow(Component.literal("Burn"), btn -> {
            if (btn.visible && btn.active) {
                TextInsertScreen.open("Seconds to burn user.", "Seconds")
                        .validate(s -> {
                            boolean found = ALL_DIGITS.matcher(s).find();
                            if (found) {
                                try {
                                    Integer.parseInt(s);
                                    return true;
                                } catch (NumberFormatException e) {
                                    return false;
                                }
                            }
                            return false;
                        })
                        .done((scr, text) -> user.burn(Integer.parseInt(text)))
                        .execute();
            }
        });
    }

    public static void open(AbstractClientUser user) {
        Minecraft.getInstance().setScreen(new UserCheatsScreen(user));
    }

    private void toggleGodMode(BaseButton button) {
        AbstractClientUser user = user();
        button.setMessage(Component.literal("God Mode: ").append(!user.hasGodMode() ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF));
        user.setGodMode(!user.hasGodMode());
    }

    private void toggleFlyMode(BaseButton button) {
        AbstractClientUser user = user();
        button.setMessage(Component.literal("Fly Mode: ").append(!user.hasFlyMode() ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF));
        user.setFlyMode(!user.hasFlyMode());
    }
}
