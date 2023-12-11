package io.github.ultreon.mods.essentials.init;

import net.minecraft.sounds.SoundEvent;

import static io.github.ultreon.mods.essentials.UEssentials.res;

public class ModSounds {
    public static final SoundEvent SHOP_BUY = SoundEvent.createVariableRangeEvent(res("shop.buy"));
    public static final SoundEvent ERROR = SoundEvent.createVariableRangeEvent(res("shop.error"));

    public static void register() {
        // Do nothing here, only for initiating the ModSounds class.
    }
}
