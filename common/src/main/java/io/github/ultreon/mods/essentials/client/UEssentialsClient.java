package io.github.ultreon.mods.essentials.client;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.ultreon.mods.essentials.client.gui.UIManager;
import io.github.ultreon.mods.essentials.client.input.ModControls;
import io.github.ultreon.mods.essentials.user.LocalUser;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.function.Supplier;

import static io.github.ultreon.mods.essentials.UEssentials.MOD_ID;
import static io.github.ultreon.mods.essentials.UEssentials.MOD_NAME;

public class UEssentialsClient {
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    private static UEssentialsClient instance;

    static {
        LoadingOverlay.LOGO_BACKGROUND_COLOR = 0xff333333;
    }

    @Getter
    private static final boolean clientSide;
    @Getter
    private static final boolean serverSide;

    @SuppressWarnings("ConstantConditions")
    private static final Supplier<Boolean> getClientSide = () -> {
        try {
            return Minecraft.getInstance() != null; // This is null when running runData.
        } catch (Throwable t) {
            return false;
        }
    };

    static {
        clientSide = getClientSide.get();

        boolean s;
        try {
            Class.forName("net.minecraft.server.MinecraftServer");
            s = true;
        } catch (ClassNotFoundException e) {
            s = false;
        }
        serverSide = s;
    }

    private LocalUser user;
    private UIManager uiManager;

    public UEssentialsClient() {
        instance = this;

        if (Platform.getEnvironment() != Env.CLIENT) {
            return;
        }

        ModControls.register();

        ClientLifecycleEvent.CLIENT_SETUP.register(this::clientSetup);

        ClientGuiEvent.SET_SCREEN.register(ClientEvents::onScreenOpen);
        ClientTickEvent.CLIENT_POST.register(ModControls::clientTick);
    }

    public static ResourceLocation res(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static File getDataFolder() {
        return new File(".", "uessentials-data");
    }

    public static File getDataFile(String subPath) {
        return new File(getDataFolder(), subPath);
    }

    public static UEssentialsClient get() {
        return instance;
    }

    public static ResourceLocation gui(String name) {
        return res("textures/gui/" + name + ".png");
    }

    @ExpectPlatform
    public static void showModList() {

    }

    /**
     * Sets up the mod on the client side.
     * @param minecraft the Minecraft client instance.
     */
    private void clientSetup(Minecraft minecraft) {
        user = LocalUser.create(Minecraft.getInstance().getUser());
    }

    public LocalUser user() {
        return user;
    }
}
