package io.github.ultreon.mods.essentials.util.mixin;

import io.github.ultreon.mods.essentials.UEssentials;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.FileExistsException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    @Inject(at = @At("HEAD"), method = "saveEverything", cancellable = true)
    private void save(boolean suppressLog, boolean flush, boolean forced, CallbackInfoReturnable<Boolean> cir) {
        try {
            File dataFolder = UEssentials.getDataFolder();
            if (dataFolder.exists()) {
                if (dataFolder.isFile()) {
                    throw new IOError(new FileExistsException("Expected directory, got a file: " + dataFolder.getAbsolutePath()));
                }
            } else {
                if (!dataFolder.mkdirs()) {
                    throw new IOError(new FileNotFoundException("Couldn't make directories: " + dataFolder.getAbsolutePath()));
                }
            }

//            SU_User.saveAll();
//            ServerShop.get().save();
//            AccessManager.get().save();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
