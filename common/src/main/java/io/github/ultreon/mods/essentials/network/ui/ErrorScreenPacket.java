package io.github.ultreon.mods.essentials.network.ui;

import com.ultreon.mods.lib.network.api.packet.PacketToClient;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import io.github.ultreon.mods.essentials.client.gui.screens.message.ErrorScreen;
import io.github.ultreon.mods.essentials.user.LocalUser;
import io.github.ultreon.mods.essentials.util.LevelUtils;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

@Getter
public class ErrorScreenPacket extends PacketToClient<ErrorScreenPacket> {
    private final Component title;
    private final Component description;
    private final boolean disconnect;

    public ErrorScreenPacket(FriendlyByteBuf buffer) {
        title = buffer.readComponent();
        description = buffer.readComponent();
        disconnect = buffer.readBoolean();
    }

    public ErrorScreenPacket(String title, String description) {
        this(Component.literal(title), Component.literal(description), false);
    }

    public ErrorScreenPacket(Component title, Component description) {
        this(title, description, false);
    }

    public ErrorScreenPacket(String title, String description, boolean disconnect) {
        this(Component.literal(title), Component.literal(description), disconnect);
    }

    public ErrorScreenPacket(Component title, Component description, boolean disconnect) {
        this.title = title;
        this.description = description;
        this.disconnect = disconnect;
    }

    public ErrorScreenPacket(String title, Component description) {
        this(Component.literal(title), description);
    }

    public ErrorScreenPacket(String title, Component description, boolean disconnect) {
        this(Component.literal(title), description, disconnect);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeComponent(title);
        buf.writeComponent(description);
        buf.writeBoolean(disconnect);
    }

    @Override
    protected void handle() {
        EnvExecutor.runInEnv(Env.CLIENT, () -> () -> {
            if (disconnect) {
                LevelUtils.saveLevelThen(() -> new ErrorScreen(title, description).open());
            } else {
                LocalUser.get().showError(title, description);
            }
        });
    }
}
