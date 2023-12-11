package io.github.ultreon.mods.essentials.mixin;

import com.mojang.authlib.GameProfile;
import io.github.ultreon.mods.essentials.security.AccessManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Inject(at = @At("HEAD"), method = "canPlayerLogin", cancellable = true)
    public void uEssentials$canPlayerLogin(SocketAddress address, GameProfile profile, CallbackInfoReturnable<Component> cir) {
        if (!AccessManager.get().hasAccess(profile.getId())) {
            cir.setReturnValue(Component.literal("You haven't got access to the server."));
        }
    }
}
