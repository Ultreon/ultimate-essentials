package io.github.ultreon.mods.essentials.client.gui.screens.moderation;

import com.ultreon.mods.lib.client.gui.screen.TitleStyle;
import io.github.ultreon.mods.essentials.client.gui.screens.UEssentialsMenuScreen;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.moderation.SetWeatherPacket;
import io.github.ultreon.mods.essentials.util.Weather;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class WeatherSetScreen extends UEssentialsMenuScreen {
    public WeatherSetScreen() {
        super(new Properties().titleText("Set weather mode").titleStyle(TitleStyle.DETACHED));

        addButtonRow(Component.literal("Clear"), this::sendClearWeather);
        addButtonRow(Component.literal("Raining"), this::sendRainWeather, Component.literal("Storm"), this::sendStormWeather);
    }

    private void sendClearWeather() {
        Networking.get().sendToServer(new SetWeatherPacket(Weather.CLEAR));
    }

    private void sendRainWeather() {
        Networking.get().sendToServer(new SetWeatherPacket(Weather.RAIN));
    }

    private void sendStormWeather() {
        Networking.get().sendToServer(new SetWeatherPacket(Weather.STORM));
    }

    public void open() {
        Minecraft.getInstance().setScreen(new WeatherSetScreen());
    }
}
