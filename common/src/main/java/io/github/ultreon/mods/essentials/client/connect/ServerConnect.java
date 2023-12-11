package io.github.ultreon.mods.essentials.client.connect;

import dev.architectury.event.events.common.PlayerEvent;
import io.github.ultreon.mods.essentials.client.gui.screens.message.ErrorScreen;
import io.github.ultreon.mods.essentials.text.Translations;
import io.github.ultreon.mods.essentials.user.LocalAccountInfo;
import io.github.ultreon.mods.essentials.user.LocalUser;
import io.github.ultreon.mods.essentials.util.LevelUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.security.SecureRandom;

@Environment(EnvType.CLIENT)
public class ServerConnect {
    private static final ServerConnect instance = new ServerConnect();
    private final Minecraft mc = Minecraft.getInstance();

    @Getter
    private ConnectionState state = ConnectionState.DISCONNECTED;

    public enum ConnectionState {
        DISCONNECTED, CONNECTING, LOGGING_IN, CONNECTED
    }

    public static ServerConnect get() {
        return instance;
    }

    private ServerConnect() {
        PlayerEvent.PLAYER_JOIN.register(this::whenLoggedIn);
    }

    @SuppressWarnings("ConstantConditions")
    @SneakyThrows
    public boolean connect(String address) {
        String ip = address == null ? "localhost" : address;

        // Todo: receive server name and description.
        ServerData data = new ServerData(Long.toHexString(new SecureRandom().nextLong()), ip, false);
        data.motd = Component.literal(Long.toHexString(new SecureRandom().nextLong()));
        data.setResourcePackStatus(ServerData.ServerPackStatus.ENABLED);

        // Setting state to connecting.
        state = ConnectionState.CONNECTING;
        ConnectScreen.startConnecting(mc.screen, Minecraft.getInstance(), ServerAddress.parseString(data.ip), data, false);

        return true;
    }

    private void whenLoggedIn(ServerPlayer serverPlayer) {
        this.state = ConnectionState.LOGGING_IN;

        LocalUser.get().login(new LocalAccountInfo("<<undefined>>"));
    }

    public void loginDenied(Component reason) {
        LevelUtils.saveLevelThen(() -> {
            state = ConnectionState.DISCONNECTED;
            mc.setScreen(new ErrorScreen(Translations.SCREEN_LOGIN_DENIED, reason));
        });
    }

    public void loginAccepted() {
        state = ConnectionState.CONNECTED;
        mc.setScreen(null);
    }
}
