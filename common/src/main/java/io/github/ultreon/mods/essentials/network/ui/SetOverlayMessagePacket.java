package io.github.ultreon.mods.essentials.network.ui;

import com.ultreon.mods.lib.network.api.packet.PacketToClient;
import io.github.ultreon.mods.essentials.client.gui.UIManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class SetOverlayMessagePacket extends PacketToClient<SetOverlayMessagePacket> {
    @Nullable
    private final Component title;
    @Nullable
    private final Component message;

    public SetOverlayMessagePacket(@Nullable Component title, @Nullable Component message) {
        this.title = title;
        this.message = message;
    }

    public SetOverlayMessagePacket(FriendlyByteBuf buffer) {
        byte b = buffer.readByte();
        title = (b & 0b01) != 0 ? buffer.readComponent() : null;
        message = (b & 0b10) != 0 ? buffer.readComponent() : null;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        byte b = 0;
        if (title != null) b |= 0b01;
        if (message != null) b |= 0b10;
        buffer.writeByte(b);
        if (title != null) buffer.writeComponent(title);
        if (message != null) buffer.writeComponent(message);
    }

    @Override
    protected void handle() {
        if (title != null && message != null) UIManager.updateOverlay(title, message);
        else if (title != null) UIManager.updateOverlayTitle(title);
        else if (message != null) UIManager.updateOverlayMessage(message);
        else UIManager.hideOverlay();
    }
}
