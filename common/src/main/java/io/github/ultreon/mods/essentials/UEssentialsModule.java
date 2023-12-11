package io.github.ultreon.mods.essentials;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public abstract class UEssentialsModule {
    public final Marker marker = MarkerFactory.getMarker(this.getClass().getName());

    public abstract void onEnable();
    public abstract void onDisable();
}
