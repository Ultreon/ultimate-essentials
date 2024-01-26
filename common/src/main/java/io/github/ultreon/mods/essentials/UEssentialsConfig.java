package io.github.ultreon.mods.essentials;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraftforge.common.ForgeConfigSpec;

public class UEssentialsConfig {
    public static final net.minecraftforge.fml.config.IConfigSpec<?> COMMON_SPEC;
    public static final net.minecraftforge.fml.config.IConfigSpec<?> CLIENT_SPEC;

    public static final ForgeConfigSpec.IntValue TP_DELAY;
    public static final ForgeConfigSpec.BooleanValue UI_SHOW_OVERLAY;

    static {
        ForgeConfigSpec.Builder common = new ForgeConfigSpec.Builder();
        common.push("teleportation");
        TP_DELAY = common.comment("Teleport delay in ticks (20 ticks = 1 second).").defineInRange("teleportDelay", secondsToTick(3), 0, secondsToTick(10));
        common.pop();

        COMMON_SPEC = common.build();

        // ----------------------------- //

        ForgeConfigSpec.Builder client = new ForgeConfigSpec.Builder();
        client.push("ui");
        UI_SHOW_OVERLAY = common.comment("Show overlay when teleporting. If false, a message will be shown instead.").define("showOverlay", true);
        client.pop();

        CLIENT_SPEC = client.build();
    }

    public static int secondsToTick(int tick) {
        return tick * 20;
    }

    @ExpectPlatform
    public static void init() {
        throw new AssertionError("Architectury failed to implement this method.");
    }
}
