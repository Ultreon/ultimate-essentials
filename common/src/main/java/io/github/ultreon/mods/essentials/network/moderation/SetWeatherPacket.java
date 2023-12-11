package io.github.ultreon.mods.essentials.network.moderation;

import com.ultreon.mods.lib.network.api.packet.PacketToServer;
import io.github.ultreon.mods.essentials.user.Permissions;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.util.Weather;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

@Getter
public class SetWeatherPacket extends PacketToServer<SetWeatherPacket> {
    private final Weather weather;

    public SetWeatherPacket(FriendlyByteBuf buf) {
        weather = buf.readEnum(Weather.class);
    }

    public SetWeatherPacket(Weather weather) {
        this.weather = weather;
    }

    @Override
    protected void handle(@NotNull ServerPlayer sender) {
        ServerUser user = ServerUser.get(sender);
        if (!user.hasPermission(Permissions.WEATHER_SET)) {
            user.handleIllegalPacket();
            return;
        }

        switch (weather) {
            case CLEAR -> sender.serverLevel().setWeatherParameters(6000, 0, false, false); // 6000 is the default weather duration.
            case RAIN -> sender.serverLevel().setWeatherParameters(0, 6000, true, false); // Same here
            case STORM -> sender.serverLevel().setWeatherParameters(0, 6000, true, true); // Also the same here
        }
        user.getLevel().isRaining();
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeEnum(weather);
    }
}
