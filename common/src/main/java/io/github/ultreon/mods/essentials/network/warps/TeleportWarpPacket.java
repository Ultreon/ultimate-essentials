package io.github.ultreon.mods.essentials.network.warps;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.network.NetworkUtils;
import io.github.ultreon.mods.essentials.util.warps.ServerWarps;
import io.github.ultreon.mods.essentials.warps.WarpReference;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

@Getter
@SuppressWarnings("unused")
public class TeleportWarpPacket extends PacketToServer<TeleportWarpPacket> {
    private final WarpReference warp;

    public TeleportWarpPacket(FriendlyByteBuf buffer) {
        warp = NetworkUtils.readWarpReference(buffer);
    }

    public TeleportWarpPacket(WarpReference warp) {
        this.warp = warp;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerWarps warps = UEssentials.getWarps();

        if (warps.has(this.warp)) {
            warps.teleport(sender, this.warp);
        } else {
            UEssentials.LOGGER.warn("Illegal warp packet: " + warp);
        }
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        NetworkUtils.writeWarpReference(buffer, this.warp);
    }
}
