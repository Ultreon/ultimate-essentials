package io.github.ultreon.mods.essentials.mixin;

import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.data.DataManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {
    @Inject(at = @At("HEAD"), method = "saveAllChunks")
    private void uEssentials$save0(boolean suppressLog, boolean flush, boolean forced, CallbackInfoReturnable<Boolean> cir) {
        try {
            DataManager.save();
        } catch (Throwable t) {
            UEssentials.LOGGER.error("Failed to save user data!", t);
        }
    }
}
