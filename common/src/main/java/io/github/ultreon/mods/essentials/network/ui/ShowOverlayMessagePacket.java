package io.github.ultreon.mods.essentials.network.ui;

import com.ultreon.mods.lib.network.api.packet.PacketToClient;
import io.github.ultreon.mods.essentials.client.gui.UIManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class ShowOverlayMessagePacket extends PacketToClient<ShowOverlayMessagePacket> {
    private final Component title;
    private final Component message;

    public ShowOverlayMessagePacket(Component title, Component message) {
        this.title = title;
        this.message = message;
    }

    public ShowOverlayMessagePacket(FriendlyByteBuf buffer) {
        this.title = buffer.readComponent();
        this.message = buffer.readComponent();
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeComponent(title);
        buffer.writeComponent(message);
    }

    @Override
    protected void handle() {
        UIManager.showOverlay(title, message);
    }
}
