package io.github.ultreon.mods.essentials.client.gui.screens.connect;

import com.ultreon.mods.lib.client.gui.screen.TitleStyle;
import com.ultreon.mods.lib.client.gui.widget.BaseButton;
import com.ultreon.mods.lib.client.gui.widget.Button;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.client.connect.ServerConnect;
import io.github.ultreon.mods.essentials.client.gui.screens.UEssentialsMenuScreen;
import io.github.ultreon.mods.essentials.text.Translations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ConnectServerScreen extends UEssentialsMenuScreen {
    private static String serverIp = "";

    private final EditBox serverIpField;

    public ConnectServerScreen() {
        super(new Properties().titleLang("gui.ultimate_essentials.connect.title").titleStyle(TitleStyle.DETACHED));

        serverIpField = new EditBox(Minecraft.getInstance().font, 0, 0, 0, 0, Component.literal("..."));

//        serverIpField.setValue(serverIp);
        serverIpField.setResponder(s -> serverIp = s);

        addInputRow(serverIpField);
        addButtonRow(
                Translations.GUI_NEXT, Button.Type.PRIMARY, this::next,
                Translations.GUI_BACK, btn -> this.back()
        );

        if (UEssentials.isDevMode()) {
            addButtonRow(Component.literal("Debugger Option"), button -> {
                String serverIp = "localhost:36686";
                serverIpField.setValue(serverIp);
                next(null);
            });
        }
    }

    @Override
    protected void init() {
        super.init();

        serverIpField.setValue(serverIp);
    }

    public void next(@Nullable BaseButton button) {
        String serverIp = serverIpField.getValue();
        ConnectServerScreen.serverIp = serverIp;
        ServerConnect.get().connect(serverIp);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 && this.serverIpField.isFocused()) {
            next(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
